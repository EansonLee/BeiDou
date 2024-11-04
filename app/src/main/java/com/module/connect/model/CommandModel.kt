package com.module.connect.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.module.connect.bean.CommandBean
import com.module.connect.bean.STYLE_FIVE
import com.module.connect.bean.STYLE_FOUR
import com.module.connect.bean.STYLE_ONE
import com.module.connect.bean.STYLE_SEVEN
import com.module.connect.bean.STYLE_SIX
import com.module.connect.bean.STYLE_THIRD
import com.module.connect.bean.STYLE_TWO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommandModel : ViewModel() {

    private val _commandList: MutableLiveData<List<CommandBean>> = MutableLiveData()
    val commandList: LiveData<List<CommandBean>> = _commandList

    private val _settingList: MutableLiveData<List<CommandBean>> = MutableLiveData()
    val settingList: LiveData<List<CommandBean>> = _settingList

    fun getAllCommand() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = mutableListOf<CommandBean>().apply {
                //1
                add(CommandBean("版本", "AT+VERSION=", "", STYLE_ONE,"","","","","","",""))
                //2
                add(CommandBean("各模块波特率", "AT+UART=", "", STYLE_SIX,"MCU","MCU-k803","UM980","4G","WIF","WIF",""))
                //3
                add(CommandBean("设备连接状态", "AT+STATUS=", "", STYLE_SIX,"定位","4GA连接","4GB连接","SocketA连接","SocketB连接","WIFI-STA-Socket",""))
                //4
                add(CommandBean("设备工作状态", "AT+STATE=", "", STYLE_FIVE,"输入电压","电池电压","工作电流","工作温度","工作湿度","",""))
                //5
                add(CommandBean("查询MEMS数据", "AT+MEMS=", "", STYLE_SEVEN,"X轴加速度","Y轴加速度","Z轴加速度","X轴角速度","Y轴角速度","Z轴角速度","工作湿度"))
                //7
                add(CommandBean("MEMS数据上报频率", "AT+MEMS_FRE=", "", STYLE_ONE,"","","","","","",""))
                //8
                add(CommandBean("电源使能", "AT+POWER=", "", STYLE_FIVE,"GNSS电源开关","4G电源开关","以太网模块电源开关","WIF模块电源开关","电台电源开关","",""))
                //9
                add(CommandBean("工作模式", "AT+MODE=", "", STYLE_FOUR,"GNSS模块工作模式","4G模块工作模式","以太网模块工作模式","以太网模块工作模式","","",""))
                //10
                add(CommandBean("SIM卡ICCID", "AT+ICCID=", "", STYLE_ONE,"","","","","","",""))
                //11
                add(CommandBean("时间和日期", "AT+CCLK=", "", STYLE_ONE,"","","","","","",""))
                //12
                add(CommandBean("4G网络信号强度", "AT+CSQ/4G=", "", STYLE_THIRD,"4G网络信号强度","4G模块误码率","4G模块网络模式","","","",""))
                //13
                add(CommandBean("4G TCP服务器参数", "AT+SOCK/4G=", "", STYLE_SIX,"A通信协议","A目标地址","A目标端口","B通信协议；","B目标地址","B目标端口",""))
                //14
                add(CommandBean("4G前段解算模式开启状态", "AT+FESLO/4G=", "", STYLE_ONE,"","","","","","",""))
                //15
                add(CommandBean("4G MQTT服务器参数", "AT+MQTTSVR/4G=", "", STYLE_FOUR,"服务器目标地址","服务器目标地址","客户用户名","客户密码","","",""))
                //16
                add(CommandBean("4G MQTT订阅主题", "AT+MQTT_SUB/4G=", "", STYLE_TWO,"主题1名称","主题2名称","","","","",""))
                //17
                add(CommandBean("4G MQTT发布主题", "AT+MQTT_PUB/4G=", "", STYLE_FIVE,"主题1名称","主题2名称","主题3名称","主题4名称","主题5名称","",""))
                //18
                add(CommandBean("MQTT 串口模式", "AT+MQTT_SERIAL_MODE/4G=", "", STYLE_ONE,"","","","","","",""))
                //19
                add(CommandBean("4G NTRIP服务器参数", "AT+NTRIPSVR/4G=", "", STYLE_FIVE,"目标地址","目标地址","用户名","客户密码","挂载点","",""))
                //20
                add(CommandBean("4G FTP服务器参数", "AT+FTPSVR/4G=", "", STYLE_FIVE,"目标地址","目标端口","用户名","密码","文件名","",""))
                //21
                add(CommandBean("以太网DHCP工作模式", "AT+DHCP/NET=", "", STYLE_ONE,"","","","","","",""))
                //22
                add(CommandBean("以太网本地的IP参数", "AT+LOCALIP/NET=", "", STYLE_THIRD,"本地IP地址","本地子网掩码","本地网关地址","","","",""))
                //23
                add(CommandBean("NET TCP/UDP服务器参数", "AT+SERVERIP/NET=", "", STYLE_SIX,"A通信协议","A目标地址","A目标端口","B通信协议","B目标地址","B目标端口",""))
                //24
                add(CommandBean("以太网 NTRIP服务器参数", "AT+NTRIPSVR/NET=", "", STYLE_FIVE,"目标地址","目标地址","目标地址","客户密码","挂载点","",""))
                //25
                add(CommandBean("WIFI热点的名称和密码", "AT+AP/WIFI=", "", STYLE_TWO,"WIFI模块AP模式热点名称","WIFI模块AP模式热点名称","","","","",""))
                //26
                add(CommandBean("WIFI连接热点的名称和密码", "AT+STA/WIFI=", "", STYLE_SIX,"热点名称","热点密码","开启状态","模块IP","子网掩码","模块网关",""))
                //27
                add(CommandBean("WIFI TCP/UDP服务器参数", "AT+SOCK/WIFI=", "", STYLE_THIRD,"是否立即重连","目标地址","目标端口","","","",""))
                //28
                add(CommandBean("WIFI MQTT服务器参数", "AT+MQTTSVR/WIFI=", "", STYLE_FIVE,"目标地址","目标端口","客户ID","用户名","密码","",""))
                //29
                add(CommandBean("WIFI MQTT订阅与发布的主题名", "AT+MQTT_SUB_PUB/WIFI=", "", STYLE_TWO,"WIFI模块MQTT服务器订阅的主题","WIFI模块MQTT服务器发布的主题","","","","",""))
                //30
                add(CommandBean("WIFI NTRIP服务器参数", "AT+NTRIPSVR/WIFI=", "", STYLE_FIVE,"目标地址","目标地址","用户名","密码","挂载点","",""))
                //31
                add(CommandBean("WIFI模块工作模式", "AT+BLEMODE/WIFI=", "", STYLE_ONE,"","","","","","",""))
                //32
                add(CommandBean("WIFI蓝牙名称", "AT+BLENAME/WIFI=", "", STYLE_ONE,"","","","","","",""))
                //33
                add(CommandBean("电台配置", "AT+CONFIG/DT=", "", STYLE_ONE,"","","","","","",""))
                //34
                add(CommandBean("电台空中传输速率", "AT+AIR_BAUD/DT=", "", STYLE_ONE,"","","","","","",""))
                //35
                add(CommandBean("电台ID配置", "AT+ID/DT=", "", STYLE_ONE,"","","","","","",""))
            }
            _commandList.postValue(result)
        }
    }


    fun getSettingCommand() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = mutableListOf<CommandBean>().apply {
                //2
                add(CommandBean("各模块波特率", "AT+UART=", "", STYLE_SIX,"MCU","MCU-k803","UM980","4G","WIF","WIF",""))
                //3
//                add(CommandBean("设备连接状态", "AT+STATUS=", "", STYLE_SIX,"定位","4GA连接","4GB连接","SocketA连接","SocketB连接","WIFI-STA-Socket",""))
                //4
//                add(CommandBean("设备工作状态", "AT+STATE=", "", STYLE_FIVE,"输入电压","电池电压","工作电流","工作温度","工作湿度","",""))
                //7
                add(CommandBean("MEMS数据上报频率", "AT+MEMS_FRE=", "", STYLE_ONE,"","","","","","",""))
                //8
                add(CommandBean("电源使能", "AT+POWER=", "", STYLE_FIVE,"GNSS电源开关","4G电源开关","以太网模块电源开关","WIF模块电源开关","电台电源开关","",""))
                //9
                add(CommandBean("工作模式", "AT+MODE=", "", STYLE_FOUR,"GNSS模块工作模式","4G模块工作模式","以太网模块工作模式","以太网模块工作模式","","",""))
                //13
                add(CommandBean("4G TCP服务器参数", "AT+SOCK/4G=", "", STYLE_SIX,"A通信协议","A目标地址","A目标端口","B通信协议；","B目标地址","B目标端口",""))
                //14
                add(CommandBean("4G前段解算模式开启状态", "AT+FESLO/4G=", "", STYLE_ONE,"","","","","","",""))
                //15
                add(CommandBean("4G MQTT服务器参数", "AT+MQTTSVR/4G=", "", STYLE_FOUR,"服务器目标地址","服务器目标地址","客户用户名","客户密码","","",""))
                //16
                add(CommandBean("4G MQTT订阅主题", "AT+MQTT_SUB/4G=", "", STYLE_TWO,"主题1名称","主题2名称","","","","",""))
                //17
                add(CommandBean("4G MQTT发布主题", "AT+MQTT_PUB/4G=", "", STYLE_FIVE,"主题1名称","主题2名称","主题3名称","主题4名称","主题5名称","",""))
                //18
                add(CommandBean("MQTT 串口模式", "AT+MQTT_SERIAL_MODE/4G=", "", STYLE_ONE,"","","","","","",""))
                //19
                add(CommandBean("4G NTRIP服务器参数", "AT+NTRIPSVR/4G=", "", STYLE_FIVE,"目标地址","目标地址","用户名","客户密码","挂载点","",""))
                //20
                add(CommandBean("4G FTP服务器参数", "AT++FTPSVR/4G=", "", STYLE_FIVE,"目标地址","目标端口","用户名","密码","文件名","",""))
                //21
                add(CommandBean("OTA升级", "AT+OTA/4G", "", STYLE_ONE,"","","","","","",""))
                //21
                add(CommandBean("以太网DHCP工作模式", "AT+DHCP/NET=", "", STYLE_ONE,"","","","","","",""))
                //22
                add(CommandBean("以太网本地的IP参数", "AT+LOCALIP/NET=", "", STYLE_THIRD,"本地IP地址","本地子网掩码","本地网关地址","","","",""))
                //23
                add(CommandBean("NET TCP/UDP服务器参数", "AT+SERVERIP/NET=", "", STYLE_SIX,"A通信协议","A目标地址","A目标端口","B通信协议","B目标地址","B目标端口",""))
                //24
                add(CommandBean("以太网 NTRIP服务器参数", "AT+NTRIPSVR/NET=", "", STYLE_FIVE,"目标地址","目标地址","目标地址","客户密码","挂载点","",""))
                //21
                add(CommandBean("重置WIFI", "AT+RST/WIFI=", "", STYLE_ONE,"","","","","","",""))
                //25
                add(CommandBean("WIFI热点的名称和密码", "AT+AP/WIFI=", "", STYLE_TWO,"WIFI模块AP模式热点名称","WIFI模块AP模式热点名称","","","","",""))
                //26
                add(CommandBean("WIFI连接热点的名称和密码", "AT+STA/WIFI=", "", STYLE_SIX,"热点名称","热点密码","开启状态","模块IP","子网掩码","模块网关",""))
                //27
                add(CommandBean("WIFI TCP/UDP服务器参数", "AT+SOCK/WIFI=", "", STYLE_THIRD,"是否立即重连","目标地址","目标端口","","","",""))
                //28
                add(CommandBean("WIFI MQTT服务器参数", "AT+MQTTSVR/WIFI=", "", STYLE_FIVE,"目标地址","目标端口","客户ID","用户名","密码","",""))
                //29
                add(CommandBean("WIFI MQTT订阅与发布的主题名", "AT+MQTT_SUB_PUB/WIFI=", "", STYLE_TWO,"WIFI模块MQTT服务器订阅的主题","WIFI模块MQTT服务器发布的主题","","","","",""))
                //30
                add(CommandBean("WIFI NTRIP服务器参数", "AT+NTRIPSVR/WIFI=", "", STYLE_FIVE,"目标地址","目标地址","用户名","密码","挂载点","",""))
                //31
                add(CommandBean("WIFI模块工作模式", "AT+BLEMODE/WIFI=", "", STYLE_ONE,"","","","","","",""))
                //32
                add(CommandBean("WIFI蓝牙名称", "AT+BLENAME/WIFI=", "", STYLE_ONE,"","","","","","",""))
                //33
                add(CommandBean("电台配置", "AT+CONFIG/DT=", "", STYLE_ONE,"","","","","","",""))
                //34
                add(CommandBean("电台空中传输速率", "AT+AIR_BAUD/DT=", "", STYLE_ONE,"","","","","","",""))
                //35
                add(CommandBean("电台ID配置", "AT+ID/DT=", "", STYLE_ONE,"","","","","","",""))
                //35
                add(CommandBean("GNSS模块串口2数据透传", "AT+DATA/GNSS2=", "", STYLE_ONE,"","","","","","",""))
                //35
                add(CommandBean("GNSS模块串口3数据透传", "AT+DATA/GNSS3=", "", STYLE_ONE,"","","","","","",""))
                //35
                add(CommandBean("心跳包", "AT+HEART=", "", STYLE_ONE,"","","","","","",""))
                //35
                add(CommandBean("启动升级指令", "AT+OTA_UPDATE", "", STYLE_ONE,"","","","","","",""))
                //35
                add(CommandBean("启动升级指令32", "AT+OTA_CRC32=", "", STYLE_ONE,"","","","","","",""))

            }
            _settingList.postValue(result)
        }
    }
}