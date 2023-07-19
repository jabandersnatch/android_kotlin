package com.kotlin.android_kotlin.viewmodel.transaction

import androidx.lifecycle.ViewModelProvider
import com.kotlin.android_kotlin.domain.transaction.TransactionRepository

class TransactionViewModelFactory (private val repository: TransactionRepository) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(TransactionRepository::class.java).newInstance(repository)
    }
}