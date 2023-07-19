package com.kotlin.android_kotlin.domain.paymentMethod

import android.util.Log
import com.kotlin.android_kotlin.data.paymentMethod.PaymentMethod
import com.kotlin.android_kotlin.data.paymentMethod.PaymentMethodDao


class PaymentMethodRepository {
    // Retrieve all paymentMethods
    suspend fun getAllPaymentMethods(id_user: String): List<PaymentMethod> {
        val paymentMethods = PaymentMethodDao().getAllPaymentMethods(id_user)
        Log.i("PaymentMethodRepository", "getAllPaymentMethods: $paymentMethods")
        return paymentMethods
    }

    // Retrieve n paymentMethods
    suspend fun getNamesPaymentMethods( id_user: String): List<String> {
        Log.i("metr3", "${PaymentMethodDao().getNamesPaymentMethods( id_user)}")
        return PaymentMethodDao().getNamesPaymentMethods( id_user)
    }

    // Add a new paymentMethods
    suspend fun addPaymentMethod(paymentMethod: PaymentMethod) {
        PaymentMethodDao().addPaymentMethod(paymentMethod)
    }

}