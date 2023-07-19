package com.kotlin.android_kotlin.data.paymentMethod

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class PaymentMethodDao {

    private val db = FirebaseFirestore.getInstance()
    private val paymentMethodref = db.collection("paymentmethod")

    suspend fun addPaymentMethod(paymentMethod: PaymentMethod) {
        paymentMethodref.document().set(paymentMethod).await()
    }


    // retrieve all paymentMethod documents by the active user
    suspend fun getAllPaymentMethods(id_user: String): List<PaymentMethod> {
        Log.i("PaymentMethodDao", id_user)
        val snapshot = paymentMethodref.whereEqualTo("id_user", id_user).get().await()
        Log.i("PaymentMethodDao", snapshot.toString())
        val paymentMethods = snapshot.toObjects(PaymentMethod::class.java)
        Log.i("PaymentMethodDao", paymentMethods.toString())
        return paymentMethods
    }

    // retrieve n paymentMethod documents by the active user
    suspend fun getNamesPaymentMethods(id_user: String): List<String> {
        val snapshot = paymentMethodref.whereEqualTo("id_user", id_user).get().await()
        val paymentMethods = snapshot.toObjects(PaymentMethod::class.java)
        Log.i("metr2", "${paymentMethods.map { it.name }}")
        return paymentMethods.map { it.name }
    }





}