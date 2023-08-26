package com.sgs.manthara.jewelRetrofit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class JewelFactory (private val jewelRepository: JewelRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return JewelVM(jewelRepository) as T
    }
}
