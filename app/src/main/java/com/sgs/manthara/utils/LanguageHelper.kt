package com.sgs.manthara.utils

import android.content.res.Resources
import android.os.Build


object LanguageHelper {
    fun getSystemLanguageCode(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources.getSystem().configuration.locales[0].language
        } else {
            Resources.getSystem().configuration.locale.language
        }

    }
}