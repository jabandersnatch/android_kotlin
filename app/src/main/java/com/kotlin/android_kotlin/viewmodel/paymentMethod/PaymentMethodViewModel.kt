package com.kotlin.android_kotlin.viewmodel.paymentMethod

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.android_kotlin.data.paymentMethod.PaymentMethod
import com.kotlin.android_kotlin.domain.paymentMethod.PaymentMethodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaymentMethodViewModel(private val repository: PaymentMethodRepository) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val paymentMethodList = MutableLiveData<List<PaymentMethod>>()
    val paymentMethodNamesList = MutableLiveData<List<String>>()

    init {
        paymentMethodList.value = emptyList() // Initialize the LiveData with empty list
    }

    fun addPaymentMethod(paymentMethod: PaymentMethod) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repository.addPaymentMethod(paymentMethod)
            isLoading.postValue(false)
        }
    }

    fun getAllPaymentMethods(id_user: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            withContext(Dispatchers.Default) {
                paymentMethodList.postValue(repository.getAllPaymentMethods(id_user))
            }
            isLoading.postValue(false)
        }
    }

    fun getNamesPaymentMethods(id_user: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            withContext(Dispatchers.Default) {
                paymentMethodNamesList.postValue(repository.getNamesPaymentMethods(id_user))
            }
            isLoading.postValue(false)
        }
    }


}