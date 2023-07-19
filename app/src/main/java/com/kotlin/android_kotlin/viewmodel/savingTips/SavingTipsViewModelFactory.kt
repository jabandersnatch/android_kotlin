package com.kotlin.android_kotlin.viewmodel.savingTips

import androidx.lifecycle.ViewModelProvider
import com.kotlin.android_kotlin.domain.savingTips.SavingTipsRepository

class SavingTipsViewModelFactory (private val repository: SavingTipsRepository) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(SavingTipsRepository::class.java).newInstance(repository)
    }
}