package com.sgs.manthara.new_otp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class OTPReceiver : BroadcastReceiver() {

    private var otpListener: OTPReceiveListener? = null


    fun setotp1(editText: EditText?) {
        otp1 = editText
    }

    fun setotp2(editText: EditText?) {
        otp2 = editText
    }

    fun setotp3(editText: EditText?) {
        otp3 = editText
    }

    fun setotp4(editText: EditText?) {
        otp4 = editText
    }

    fun setotp5(editText: EditText?) {
        otp5 = editText
    }

    fun setotp6(editText: EditText?) {
        otp6 = editText
    }

    fun fitbutton(button: Button?) {
        send = button
    }

    fun setOTPListener(otpListener: OTPReceiveListener) {
        this.otpListener = otpListener
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras!![SmsRetriever.EXTRA_STATUS] as Status?

            when (status!!.statusCode) {
                CommonStatusCodes.SUCCESS -> {

                    //This is the full message
                    val message = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?

                    /*<#> Your ExampleApp code is: 123ABC78
                    FA+9qCX9VSu*/

                    //Extract the OTP code and send to the listener
                    try {
                        val otp = message!!.split("is ").toTypedArray()[1]
                        val fullnum = otp.substring(0, 6)
                        val notp1 = fullnum.substring(0, 1)
                        val notp2 = fullnum.substring(1, 2)
                        val notp3 = fullnum.substring(2, 3)
                        val notp4 = fullnum.substring(3, 4)
                        val notp5 = fullnum.substring(4, 5)
                        val notp6 = fullnum.substring(5, 6)
                        otp1?.setText(notp1)
                        otp2?.setText(notp2)
                        otp3?.setText(notp3)
                        otp4?.setText(notp4)
                        otp5?.setText(notp5)
                        otp6?.setText(notp6)


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    if (otpListener != null) {
                        otpListener!!.onOTPReceived(message)
                    }



                }
                CommonStatusCodes.TIMEOUT -> // Waiting for SMS timed out (5 minutes)
                    if (otpListener != null) {
                        otpListener!!.onOTPTimeOut()
                    }
                CommonStatusCodes.API_NOT_CONNECTED -> if (otpListener != null) {

                    otpListener!!.onOTPReceivedError("API NOT CONNECTED")
                }
                CommonStatusCodes.NETWORK_ERROR -> if (otpListener != null) {

                    otpListener!!.onOTPReceivedError("NETWORK ERROR")
                }
                CommonStatusCodes.ERROR -> if (otpListener != null) {

                    otpListener!!.onOTPReceivedError("SOME THING WENT WRONG")
                }
            }
        }
    }

    interface OTPReceiveListener {

        fun onOTPReceived(otp: String?)
        fun onOTPTimeOut() {}
        fun onOTPReceivedError(error: String?)
    }

    companion object {

        private var otp1: EditText? = null
        private var otp2: EditText? = null
        private var otp3: EditText? = null
        private var otp4: EditText? = null
        private var otp5: EditText? = null
        private var otp6: EditText? = null
        private var send: Button? = null


    }
}