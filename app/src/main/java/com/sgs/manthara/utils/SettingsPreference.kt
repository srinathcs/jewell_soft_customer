package com.sgs.manthara.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SettingsPreference( private val context: Context) {

    companion object {

        const val PREF_NAME = "settings_pref"
        const val BASE_URL = "base_url"
        const val APP_THEME = "app_theme"
        const val LOCALE = "locale"
    }
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PREF_NAME)


    suspend fun saveBaseURL(value: String) {

        val dataStoreKey = stringPreferencesKey(BASE_URL)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }

    }



    suspend fun setAppTheme(value: String) {

        val dataStoreKey = stringPreferencesKey(APP_THEME)
        context.dataStore.edit {
            it[dataStoreKey] =value
        }
    }


    fun getAppTheme(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(APP_THEME)
        val theme = it[dataStoreKey] ?: "Follow System"
        theme
    }

    suspend fun setAppLocale(value: String) {
        val dataStoreKey = stringPreferencesKey(LOCALE)

        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    fun getAppLocale(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(LOCALE)
        val locale = it[dataStoreKey] ?: "follow system"
        locale
    }

    suspend fun clearValues() {

        context.dataStore.edit {

            it.clear()
        }

    }

}