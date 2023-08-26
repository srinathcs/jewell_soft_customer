package com.sgs.manthara.jewelRetrofit

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(MainPreference.PREF_NAME)

class MainPreference( val context: Context) {

    companion object {

        const val PREF_NAME = "main_pref"
        const val CID = "cid"
        const val COMPANY_NAME = "company_name"
        const val LEDGER_ID = "ledger_id"
        const val MOBILE_NO = "mobile_no"
        const val PHONE = "phone"
        const val ROLE_ID = "role_id"
        const val ROLE_TYPE = "role_type"
        const val SSID = "ssid"
        const val USER_ID = "user_id"
        const val F_NAME = "f_name"
        const val NAME = "name"
        const val IS_LOGGED_IN = "is_logged_in"
        const val COMPANY_ID = "company_id"
        const val CUSTOMER_ID = "customer_id"
        const val ICON = "icon"
        const val COMPANY_SERIAL_ID = "company_serial_id"
        const val PHONE_NO = "phone_no"
        const val USER_PHONE = "user_Phone"
        const val EMAIL = "email"
        const val PAY_ID = "payid"

        const val  LATITUDE ="latitude"
        const val  LONGITUDE = "longitude"
        const val DATE ="date"

        const val TEXTVIEW = "textview"

        const val ROUTE_ID = "route_id"

        const val BID = "bid"

    }




    suspend fun saveLogin(value: Boolean) {

        val dataStoreKey = booleanPreferencesKey(IS_LOGGED_IN)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    fun isLoggedIn(): Flow<Boolean> = context.dataStore.data.map {

        val dataStoreKey = booleanPreferencesKey(IS_LOGGED_IN)
        val isLoggedIn = it[dataStoreKey] ?: false
        isLoggedIn

    }

    fun getBID(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(BID)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }
    fun getRouteId(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(ROUTE_ID)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }


    suspend fun saveBID(value: String) {
        val dataStoreKey = stringPreferencesKey(BID)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }



    suspend fun saveCId(value: String) {
        val dataStoreKey = stringPreferencesKey(CID)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }

    }



    suspend fun saveSerialNo(value: String) {

        val dataStoreKey = stringPreferencesKey(COMPANY_SERIAL_ID)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }

    }


    fun getSerialNo(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(COMPANY_SERIAL_ID)
        val cid = it[dataStoreKey] ?: "0"
        cid
    }


    fun getCid(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(CID)
        val cid = it[dataStoreKey] ?: "0"
        cid
    }

    fun getCidandUserid(): Flow<CidandUserid> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(CID)
        val dataStoreKey1 = stringPreferencesKey(USER_ID)

        val cid = it[dataStoreKey] ?: "0"
        val user_id = it[dataStoreKey1]?: "0"
        val cidandUserid = CidandUserid(cid,user_id)
        cidandUserid
    }

    suspend fun saveCompanyName(value: String) {
        val dataStoreKey = stringPreferencesKey(COMPANY_NAME)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }

    }


    suspend fun saveCompanyPhoneNo(value: String) {
        val dataStoreKey = stringPreferencesKey(PHONE_NO)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    fun getCompanyPhoneNo(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(PHONE_NO)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }

    suspend fun saveUserPhoneNo(value: String) {
        val dataStoreKey = stringPreferencesKey(USER_PHONE)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    fun getUserPhoneNo(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(USER_PHONE)
        val userNo = it[dataStoreKey] ?: "0"
        userNo
    }


    fun getCompanyName(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(COMPANY_NAME)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }

    suspend fun saveLedgerId(value: String) {

        val dataStoreKey = stringPreferencesKey(LEDGER_ID)
        context.dataStore.edit {

            it[dataStoreKey] = value
        }
    }


    fun getLedger(): Flow<String> = context.dataStore.data.map {

        val dataStoreKey = stringPreferencesKey(LEDGER_ID)

        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }

    suspend fun saveMobileNo(value: String) {

        val dataStoreKey = stringPreferencesKey(MOBILE_NO)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    fun getMobileNo(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(MOBILE_NO)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }

    suspend fun savePhoneMobileNo(value: String) {

        val dataStoreKey = stringPreferencesKey(PHONE)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    fun getPhoneMobileNo(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(PHONE)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }

    suspend fun saveRoleId(value: String) {
        val dataStoreKey = stringPreferencesKey(ROLE_ID)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    fun getEmail(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(EMAIL)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }

    suspend fun saveEmail(value: String){
        val dataStoreKey = stringPreferencesKey(EMAIL)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    fun getRoleId(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(ROLE_ID)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }

    suspend fun saveRoleType(value: String) {
        val dataStoreKey = stringPreferencesKey(ROLE_TYPE)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    fun getRoleType(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(ROLE_TYPE)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }

    suspend fun saveSSID(value: String) {
        val dataStoreKey = stringPreferencesKey(SSID)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    fun getSSID(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(SSID)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }

    suspend fun saveUserId(value: String) {
        val dataStoreKey = stringPreferencesKey(USER_ID)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    fun getUserId(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(USER_ID)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }

    suspend fun saveUserName(value: String) {
        val dataStoreKey = stringPreferencesKey(F_NAME)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }


    fun getUserName(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(F_NAME)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }

    suspend fun saveName(value: String) {
        val dataStoreKey = stringPreferencesKey(F_NAME)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }


    fun getName(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(F_NAME)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }

    suspend fun savePayid(value: String) {
        val dataStoreKey = stringPreferencesKey(PAY_ID)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }


    fun getPayid(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(PAY_ID)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }




    suspend fun saveCompanyID(value: String) {
        val dataStoreKey = stringPreferencesKey(COMPANY_ID)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    fun getCompanyId(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(COMPANY_ID)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }





    suspend fun saveLatitude(value: String) {
        val dataStoreKey = stringPreferencesKey(LATITUDE)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    fun getLatitude(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(LATITUDE)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }



    suspend fun saveLongitude(value: String) {
        val dataStoreKey = stringPreferencesKey(LONGITUDE)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    fun getLongitude(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(LONGITUDE)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }




    suspend fun saveCustomerID(value: String) {
        val dataStoreKey = stringPreferencesKey(CUSTOMER_ID)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    fun getCustomerId(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(CUSTOMER_ID)
        val companyName = it[dataStoreKey] ?: "0"
        companyName
    }

    suspend fun saveIconStatus(value: String) {

        val dataStoreKey = stringPreferencesKey(ICON)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    fun getIconStatus(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(ICON)
        val icon = it[dataStoreKey] ?: "0"
        icon

    }


    suspend fun saveTextView(value: String){
        val dataStoreKey = stringPreferencesKey(TEXTVIEW)
        context.dataStore.edit {
            it[dataStoreKey] =value
        }
    }

    fun getTextView():Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(TEXTVIEW)
        val textView = it[dataStoreKey] ?:"0"
        textView
    }



    suspend fun saveDate(value: String){
        val dataStoreKey= stringPreferencesKey(DATE)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }
    fun getDate(): Flow<String> = context.dataStore.data.map {
        val dataStoreKey = stringPreferencesKey(DATE)
        val date = it[dataStoreKey] ?: "0"
        date

    }

    suspend fun clearValues() {
        context.dataStore.edit {
            it.clear()
        }



    }
}

data class CidandUserid(var cid:String? = null,var user_id:String?=null)