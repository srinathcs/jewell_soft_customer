package com.sgs.manthara.utils


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    SettingPreference.PREF_NAME
)

class SettingPreference(private val context: Context) {

    companion object {
        const val PREF_NAME = "theme_pref"
        const val APP_THEME = "app_theme"
        const val LOCALE="locale"
    }
    suspend fun setAppTheme(value: String) {
        val dataStoreKey = stringPreferencesKey(APP_THEME)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    fun getApptheme(): kotlinx.coroutines.flow.Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(APP_THEME)
        val theme = it[dataStoreKey] ?: "Follow System"
        theme
    }


    suspend fun  setAppLang(value: String){
        val dataStoreKey = stringPreferencesKey(LOCALE)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }

    }

    fun getAppLang():kotlinx.coroutines.flow.Flow<String> = context.dataStore.data.map {
        val dataStoreKey= stringPreferencesKey(LOCALE)
        val locale =it[dataStoreKey] ?: "follow System"
        locale
    }


}