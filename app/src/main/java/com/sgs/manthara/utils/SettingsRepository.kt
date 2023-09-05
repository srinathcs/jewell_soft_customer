package com.sgs.manthara.utils

import com.sgs.manthara.jewelRetrofit.MainPreference

class SettingsRepository (private val settingsPreference: SettingsPreference, private val mainPreference: MainPreference) {


    suspend fun setAppTheme(value: String) = settingsPreference.setAppTheme(value)

    fun getAppTheme() = settingsPreference.getAppTheme()

    suspend fun saveIconStatus(value: String) = mainPreference.saveIconStatus(value)

}