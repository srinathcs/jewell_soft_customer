package com.sgs.manthara

import android.content.Context
import android.content.SharedPreferences

class ImagePreferrence {

    fun setString(key: String?, value: String?) {
        preferences!!.edit().putString(key, value).apply()
    }

    fun getString(key: String?): String? {
        return preferences!!.getString(key, "")
    }

    companion object {
        private var INSTANCE: ImagePreferrence? = null
        private var preferences: SharedPreferences? = null
        @Synchronized
        fun getInstance(context: Context): ImagePreferrence? {
            if (INSTANCE == null) {
                INSTANCE = ImagePreferrence()
                preferences = context.getSharedPreferences("userinfo3", Context.MODE_PRIVATE)
            }
            return INSTANCE
        }
    }
}