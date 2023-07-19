package com.kotlin.android_kotlin.viewmodel.paymentMethod

import androidx.lifecycle.ViewModelProvider
import com.kotlin.android_kotlin.domain.paymentMethod.PaymentMethodRepository

class PaymentMethodViewModelFactory (private val repository: PaymentMethodRepository) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(PaymentMethodRepository::class.java).newInstance(repository)
    }
}