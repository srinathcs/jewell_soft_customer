package com.sgs.manthara.new_otp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Matcher
import java.util.regex.Pattern

class NewReceiver() : BroadcastReceiver() {

    var otpReceiveListener: OTPReceiveListener? = null

    fun InjectListner(otpReceiveListener: OTPReceiveListener){
        this.otpReceiveListener = otpReceiveListener
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent!!.action) {
            val extras = intent!!.extras
            if (extras != null) {
                val status: Status? = extras[SmsRetriever.EXTRA_STATUS] as Status?
                if (status != null) when (status.getStatusCode()) {
                    CommonStatusCodes.SUCCESS -> {

                        // Get SMS message contents
                        val message = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?
                        if (message != null) {
                            val pattern: Pattern = Pattern.compile("(\\d{4})")
                            //   \d is for a digit
                            //   {} is the number of digits here 4.
                            val matcher: Matcher = pattern.matcher(message)
                            var value = ""
                            if (matcher.find()) {
                                value = matcher.group(0) // 4 digit number
                                if (this.otpReceiveListener != null) this.otpReceiveListener!!.onOTPReceived(
                                    value
                                )
                            } else {

                                if (this.otpReceiveListener != null) this.otpReceiveListener!!.onOTPReceived(
                                    "null"
                                )
                            }
                        }
                    }
                    CommonStatusCodes.TIMEOUT -> if (this.otpReceiveListener != null) this.otpReceiveListener!!.onOTPTimeOut()
                }
            }
        }
    }

    interface OTPReceiveListener {
        fun onOTPReceived(otp: String?)
        fun onOTPTimeOut()
    }
}