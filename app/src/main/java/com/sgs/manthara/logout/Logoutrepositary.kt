package com.sgs.manthara.logout

import com.sgs.manthara.jewelRetrofit.MainPreference

class Logoutrepositary(private val mainPreference: MainPreference) {

    suspend fun clearValues() = mainPreference.clearValues()

}