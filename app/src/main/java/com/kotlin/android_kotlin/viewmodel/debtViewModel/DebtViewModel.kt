package com.kotlin.android_kotlin.viewmodel.debtViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.android_kotlin.data.debts.Debt
import com.kotlin.android_kotlin.domain.debt.DebtRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DebtViewModel(private val repository: DebtRepository) : ViewModel()  {

    val isLoading = MutableLiveData<Boolean>()
    val debtList = MutableLiveData<List<Debt>>()

    init {
        debtList.value = emptyList() // Initialize the LiveData with empty list
    }

    fun addDebt(debt: Debt) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repository.addDebt(debt)
            isLoading.postValue(false)
        }
    }

    fun getAllDebts(id_user: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            withContext(Dispatchers.Default) {
                debtList.postValue(repository.getAllDebts(id_user))
            }
            isLoading.postValue(false)
        }
    }
}