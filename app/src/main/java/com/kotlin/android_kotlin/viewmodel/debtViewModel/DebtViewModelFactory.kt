package com.kotlin.android_kotlin.viewmodel.debtViewModel

import androidx.lifecycle.ViewModelProvider
import com.kotlin.android_kotlin.domain.debt.DebtRepository

class DebtViewModelFactory (private val repository: DebtRepository) : ViewModelProvider.Factory{
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(DebtRepository::class.java).newInstance(repository)
    }
}