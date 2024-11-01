package com.module.connect.util

import android.widget.EditText
import com.module.connect.databinding.FragmentHomeBinding

object InCludeUtils {


    /*fun seVersion(binding: FragmentHomeBinding, str: String) {
        binding.llInclude.etVersion.text = str
    }

    fun seICCID(binding: FragmentHomeBinding, str: String) {
        binding.llInclude.etIccid.text = str
    }

    fun seMemsFre(binding: FragmentHomeBinding, str: String) {
        binding.llInclude.etMemsfre.setText(str)
    }

    fun seCclk(binding: FragmentHomeBinding, str: String) {
        binding.llInclude.etCclk.text = str
    }

    fun seFeslo(binding: FragmentHomeBinding, str: String) {
        binding.llInclude.etFeslo.setText(str)
    }

    fun seSerial(binding: FragmentHomeBinding, str: String) {
        binding.llInclude.etSerial.setText(str)
    }

    fun setDhcp(binding: FragmentHomeBinding, str: String) {
        binding.llInclude.etDhcp.setText(str)
    }

    fun setResetWifi(binding: FragmentHomeBinding, str: String) {
        binding.llInclude.etResetWifi.text = str
    }

    fun setWifiMode(binding: FragmentHomeBinding, str: String) {
        binding.llInclude.etWifimode1.setText(str)
    }

    fun setWifiName(binding: FragmentHomeBinding, str: String) {
        binding.llInclude.etWifiname1.setText(str)
    }

    fun setDtMode(binding: FragmentHomeBinding, str: String) {
        binding.llInclude.etRadiomode1.setText(str)
    }

    fun setSpeed(binding: FragmentHomeBinding, str: String) {
        binding.llInclude.etRadiospeed1.setText(str)
    }

    fun setDtId(binding: FragmentHomeBinding, str: String) {
        binding.llInclude.etRadioid1.setText(str)
    }

    fun seMems(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etMems1.setText(res[0])
        binding.llInclude.etMems2.setText(res[1])
        binding.llInclude.etMems3.setText(res[2])
        binding.llInclude.etMems4.setText(res[3])
        binding.llInclude.etMems5.setText(res[4])
        binding.llInclude.etMems6.setText(res[5])
        binding.llInclude.etMems7.setText(res[6])
    }

    fun seUart(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etMcu1.setText(res[0])
        binding.llInclude.etMcu2.setText(res[1])
        binding.llInclude.etMcu3.setText(res[2])
        binding.llInclude.etMcu4.setText(res[3])
        binding.llInclude.etMcu5.setText(res[4])
        binding.llInclude.etMcu6.setText(res[5])
    }

    fun seStatus(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etStatus1.setText(res[0])
        binding.llInclude.etStatus2.setText(res[1])
        binding.llInclude.etStatus3.setText(res[2])
        binding.llInclude.etStatus4.setText(res[3])
        binding.llInclude.etStatus5.setText(res[4])
        binding.llInclude.etStatus6.setText(res[5])
    }

    fun seState(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etState1.setText(res[0])
        binding.llInclude.etState2.setText(res[1])
        binding.llInclude.etState3.setText(res[2])
        binding.llInclude.etState4.setText(res[3])
        binding.llInclude.etState5.setText(res[4])
    }

    fun sePower(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etPower1.setText(res[0])
        binding.llInclude.etPower2.setText(res[1])
        binding.llInclude.etPower3.setText(res[2])
        binding.llInclude.etPower4.setText(res[3])
        binding.llInclude.etPower5.setText(res[4])
    }

    fun seMode(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etMode1.setText(res[0])
        binding.llInclude.etMode2.setText(res[1])
        binding.llInclude.etMode3.setText(res[2])
        binding.llInclude.etMode4.setText(res[3])
    }

    fun seCsq(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etCsq1.setText(res[0])
        binding.llInclude.etCsq2.setText(res[1])
        binding.llInclude.etCsq3.setText(res[2])
    }

    fun seSock4(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etSock41.setText(res[0])
        binding.llInclude.etSock42.setText(res[1])
        binding.llInclude.etSock43.setText(res[2])
        binding.llInclude.etSock44.setText(res[3])
        binding.llInclude.etSock45.setText(res[4])
        binding.llInclude.etSock46.setText(res[5])
    }

    fun seMqtt(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etMqtt1.setText(res[0])
        binding.llInclude.etMqtt2.setText(res[1])
        binding.llInclude.etMqtt3.setText(res[2])
        binding.llInclude.etMqtt4.setText(res[3])
    }

    fun seMqttTheme(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etTheme1.setText(res[0])
        binding.llInclude.etTheme2.setText(res[1])
    }

    fun setPubTheme(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etSendtheme1.setText(res[0])
        binding.llInclude.etSendtheme2.setText(res[1])
        binding.llInclude.etSendtheme3.setText(res[2])
        binding.llInclude.etSendtheme4.setText(res[3])
        binding.llInclude.etSendtheme5.setText(res[4])
    }

    fun setNtrip(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etNtrip1.setText(res[0])
        binding.llInclude.etNtrip2.setText(res[1])
        binding.llInclude.etNtrip3.setText(res[2])
        binding.llInclude.etNtrip4.setText(res[3])
        binding.llInclude.etNtrip5.setText(res[4])
    }

    fun setFtp(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etFtp1.setText(res[0])
        binding.llInclude.etFtp2.setText(res[1])
        binding.llInclude.etFtp3.setText(res[2])
        binding.llInclude.etFtp4.setText(res[3])
        binding.llInclude.etFtp5.setText(res[4])
    }

    fun setIp(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etIp1.setText(res[0])
        binding.llInclude.etIp2.setText(res[1])
        binding.llInclude.etIp3.setText(res[2])
    }

    fun seNet(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etNet1.setText(res[0])
        binding.llInclude.etNet2.setText(res[1])
        binding.llInclude.etNet3.setText(res[2])
        binding.llInclude.etNet4.setText(res[3])
        binding.llInclude.etNet5.setText(res[4])
        binding.llInclude.etNet6.setText(res[5])
    }

    fun seServerntrip(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etServerntrip1.setText(res[0])
        binding.llInclude.etServerntrip2.setText(res[1])
        binding.llInclude.etServerntrip3.setText(res[2])
        binding.llInclude.etServerntrip4.setText(res[3])
        binding.llInclude.etServerntrip5.setText(res[4])
    }

    fun setApWifi(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etWifihot1.setText(res[0])
        binding.llInclude.etWifihot2.setText(res[1])
    }

    fun setStaWifi(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etConnectwifi1.setText(res[0])
        binding.llInclude.etConnectwifi2.setText(res[1])
        binding.llInclude.etConnectwifi3.setText(res[2])
        binding.llInclude.etConnectwifi4.setText(res[3])
        binding.llInclude.etConnectwifi5.setText(res[4])
        binding.llInclude.etConnectwifi6.setText(res[5])
    }

    fun setUdp(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etUdp1.setText(res[0])
        binding.llInclude.etUdp2.setText(res[1])
        binding.llInclude.etUdp3.setText(res[2])
    }

    fun setMqttWifi(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etWifimqtt1.setText(res[0])
        binding.llInclude.etWifimqtt2.setText(res[1])
        binding.llInclude.etWifimqtt3.setText(res[2])
        binding.llInclude.etWifimqtt4.setText(res[3])
        binding.llInclude.etWifimqtt5.setText(res[4])
    }

    fun setMqttPub(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etMqttwifitheme1.setText(res[0])
        binding.llInclude.etMqttwifitheme2.setText(res[1])
    }

    fun setWifiNtrip(binding: FragmentHomeBinding, str: String) {
        val res = str.substringAfter("=").split(",")
        binding.llInclude.etWifintrip1.setText(res[0])
        binding.llInclude.etWifintrip2.setText(res[1])
        binding.llInclude.etWifintrip3.setText(res[2])
        binding.llInclude.etWifintrip4.setText(res[3])
        binding.llInclude.etWifintrip5.setText(res[4])
    }


    fun clickUart(binding: FragmentHomeBinding) {
        if (isUratNotNull(binding)) {

        }
    }

     fun isUratNotNull(binding: FragmentHomeBinding): Boolean {
        if (areAllEditTextsNotNullOrEmpty(
                binding.llInclude.etMcu1, binding.llInclude.etMcu2, binding.llInclude.etMcu3,
                binding.llInclude.etMcu4, binding.llInclude.etMcu5, binding.llInclude.etMcu6
            )
        ) {
            return true
        }
        return false
    }

   private fun areAllEditTextsNotNullOrEmpty(vararg editTexts: EditText): Boolean {
        return editTexts.all { it.text?.isNotEmpty() == true }
    }*/


    fun areAllEditTextsNotNullOrEmpty(vararg editTexts: EditText): Boolean {
        return editTexts.all { it.text?.isNotEmpty() == true }
    }
}