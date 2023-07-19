package com.kotlin.android_kotlin.viewmodel.transaction

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.android_kotlin.data.transaction.Transaction
import com.kotlin.android_kotlin.domain.transaction.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val transactionList = MutableLiveData<List<Transaction>>()

    init {
        transactionList.value = emptyList() // Initialize the LiveData with empty list
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repository.addTransaction(transaction)
            isLoading.postValue(false)
        }
    }

    fun getAllTransactions(id_user: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            withContext(Dispatchers.Default) {
                transactionList.postValue(repository.getAllTransactions(id_user))
            }
            isLoading.postValue(false)
        }
    }

    fun updateTransaction(transactionId: String, transaction: Transaction) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repository.updateTransaction(transactionId, transaction)
            isLoading.postValue(false)
        }
    }

    fun deleteTransaction(transactionId: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repository.deleteTransaction(transactionId)
            isLoading.postValue(false)
        }
    }

    fun getTotalAmount(startdate: String, enddate: String, id_user: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repository.getTotalAmount(startdate, enddate, id_user)
            isLoading.postValue(false)
        }
    }

    fun getNTransactions(n: Int, id_user: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            transactionList.postValue(repository.getNTransactions(n, id_user))
            isLoading.postValue(false)
        }
    }
}