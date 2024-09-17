import android.app.ProgressDialog
import android.bluetooth.*
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.module.connect.consts.IConsts
import com.module.connect.util.KeyValueUtils

object BluetoothHelper {

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothLeScanner: BluetoothLeScanner
    private var progressDialog: ProgressDialog? = null
    private var currentGatt: BluetoothGatt? = null

    private const val TAG = "BluetoothLEUtil"

    // 初始化蓝牙工具
    fun init(context: Context) {
        bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
    }

    // 扫描蓝牙设备
    fun startScan(onDeviceFound: (BluetoothDevice) -> Unit, onScanFailed: (Int) -> Unit) {
        bluetoothLeScanner.startScan(object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                result?.device?.let(onDeviceFound)
            }

            override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                results?.forEach { result ->
                    result.device?.let(onDeviceFound)
                }
            }

            override fun onScanFailed(errorCode: Int) {
                onScanFailed(errorCode)
            }
        })
    }

    // 停止扫描
    fun stopScan(callback: ScanCallback) {
        bluetoothLeScanner.stopScan(callback)
        hideProgressDialog()
    }

    // 尝试配对并连接设备
    fun connectDevice(
        context: Context,
        device: BluetoothDevice,
        onConnected: () -> Unit,
        onDisconnected: () -> Unit
    ) {
        currentGatt = device.connectGatt(context, false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    gatt.discoverServices()
                    onConnected()
                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    onDisconnected()
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.d(TAG, "服务发现成功，可以开始通信")
                    val services = gatt.services
                    for (service in services) {


                        // 获取服务中的所有特性
                        val characteristics = service.characteristics
                        for (characteristic in characteristics) {
                            val characteristicUUID = characteristic.uuid
//                            Log.d("BluetoothGatt", "发现特性: $characteristicUUID")


//                            // 检查特性是否支持写入
//                            if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_WRITE != 0) {
//                                Log.d("BluetoothGatt", "特性支持写入: $characteristicUUID")
//                            }
//
//                            // 检查特性是否支持读取
//                            if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_READ != 0) {
//                                Log.d("BluetoothGatt", "特性支持读取: $characteristicUUID")
//                            }

                            if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) {
                                Log.d("BluetoothGatt", "发现服务: ${service.uuid}")
                                Log.d("BluetoothGatt", "特性支持通知: $characteristicUUID")
                                val uuid = KeyValueUtils.getString(IConsts.KEY_CURRENT_WRITE_UUID)
                                val char = KeyValueUtils.getString(IConsts.KEY_CURRENT_WRITE_CHARACTERISTICS)
                                if (!TextUtils.isEmpty(uuid) && !TextUtils.isEmpty(char) ) {
                                    return
                                }
                                KeyValueUtils.setString(
                                    IConsts.KEY_CURRENT_WRITE_CHARACTERISTICS,
                                    characteristicUUID.toString()
                                )
                                KeyValueUtils.setString(
                                    IConsts.KEY_CURRENT_WRITE_UUID,
                                    service.uuid.toString()
                                )
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "服务发现失败，状态码: $status")
                }
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic
            ) {
                val response = characteristic.value?.toString(Charsets.UTF_8)?.trim()
                Log.d(TAG, "收到设备回复: $response")
            }

            override fun onCharacteristicWrite(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.d(TAG, "指令发送成功")
                } else {
                    Log.e(TAG, "指令发送失败，状态码: $status")
                }
            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    val response = characteristic?.value?.toString(Charsets.UTF_8)?.trim()
                    Log.d(TAG, "读取到的设备回复: $response")
                } else {
                    Log.e(TAG, "读取设备特性失败，状态码: $status")
                }
            }
        })
    }

    // 判断设备是否已配对
    fun isDevicePaired(device: BluetoothDevice): Boolean {
        return bluetoothAdapter.bondedDevices.contains(device)
    }

    fun isAnyDevicePaired(): Boolean {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        return bluetoothAdapter?.bondedDevices?.isNotEmpty() ?: false
    }

    // 判断设备是否已连接
    fun isDeviceConnected(device: BluetoothDevice): Boolean {
        val connectedDevices = bluetoothManager.getConnectedDevices(BluetoothGatt.GATT)
        return connectedDevices.contains(device)
    }

    fun isAnyDeviceConnected(context: Context): Boolean {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val connectedDevices = bluetoothManager.getConnectedDevices(BluetoothProfile.GATT)
        return connectedDevices.isNotEmpty()
    }

    // 显示进度弹框
    private fun showProgressDialog(context: Context, message: String) {
        progressDialog = ProgressDialog(context).apply {
            setMessage(message)
            setCancelable(false)
            show()
        }
    }

    fun getCurrentGate(): BluetoothGatt? {
        return currentGatt
    }

    // 隐藏进度弹框
    private fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    // 断开当前连接
    fun disconnect() {
        currentGatt?.disconnect()
        currentGatt?.close()
        currentGatt = null
    }
}
