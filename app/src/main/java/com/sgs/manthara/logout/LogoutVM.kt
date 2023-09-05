package com.sgs.manthara.logout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LogoutVM(private val repositary: Logoutrepositary) : ViewModel(){

    fun clearValues() = viewModelScope.launch {
        repositary.clearValues()
    }

}