package com.sgs.manthara.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingsViewModel (private val repository: SettingsRepository) :
    ViewModel() {

    fun setAppTheme(value: String) = viewModelScope.launch {
        repository.setAppTheme(value)
    }

    fun getAppTheme() = repository.getAppTheme()


    fun saveIconStatus(value: String) = viewModelScope.launch {
        repository.saveIconStatus(value)
    }

}