package com.sgs.manthara.new_otp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SmsBroadcastReceiver: BroadcastReceiver() {

    var smsBroadcastReceiverListener: SMSBroadcastReceiverListener?= null

    override fun onReceive(p0: Context?, p1: Intent?) {

        if (SmsRetriever.SMS_RETRIEVED_ACTION == p1?.action){

            val extras = p1.extras
            val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

            when(smsRetrieverStatus.statusCode){

                CommonStatusCodes.SUCCESS -> {

                    val messageIntent = extras?.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                    smsBroadcastReceiverListener?.onSuccess(messageIntent)
                }

                CommonStatusCodes.TIMEOUT -> {

                    smsBroadcastReceiverListener?.onFailure()
                }
            }
        }
    }

    interface SMSBroadcastReceiverListener {

        fun onSuccess(intent:Intent?)
        fun onFailure()
    }
}