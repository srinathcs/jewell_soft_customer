package com.sgs.manthara.new_otp

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.Arrays

class AppSignatureHashHelper(context: Context) : ContextWrapper(context) {

    val TAG = "AppSignatureHashHelper"

    private val HASH_TYPE = "SHA-256"
    val NUM_HASHED_BYTES = 9
    val NUM_BASE64_CHAR = 11


    @ExperimentalUnsignedTypes
    @RequiresApi(Build.VERSION_CODES.P)
    fun getAppSignature(): ArrayList<String> {

        val list: ArrayList<String> = arrayListOf()
        try {
            val packageName = packageName
            val packageManager = packageManager
            val signatures = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            ).signatures


            for (signature in signatures) {
                val hash: String =
                    getHash(packageName, signature.toCharsString())
                if (hash != null) {
                    list.add(String.format("%s", hash))
                }
            }
        } catch (exception: Exception) {
            Log.i(TAG, "getAppSignature: signature not found")

        }
        return list

    }

    @ExperimentalUnsignedTypes

    fun getHash(packageName: String, signature: String): String {

        val appInfo = packageName + "" + signature
        try {
            val messageDigest = MessageDigest.getInstance(HASH_TYPE)
            messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))
            var hashSignature = messageDigest.digest()
            hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES)

            var base64Hash =
                Base64.encodeToString(hashSignature, Base64.NO_PADDING or Base64.NO_WRAP)
            base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR)
            return base64Hash

        } catch (exception: Exception) {

            Log.i(TAG, "hash: no algo exception")
        }

        return ""


    }


}

