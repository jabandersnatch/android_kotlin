package com.kotlin.android_kotlin.data.debts

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DebtDao {

    private val db = FirebaseFirestore.getInstance()
    private val debtRef = db.collection("debt")

    suspend fun addDebt(debt: Debt) {
        debtRef.document().set(debt).await()
    }


    suspend fun getAllDebts(id_user: String): List<Debt> {
        val snapshot = debtRef.whereEqualTo("id_user", id_user).get().await()
        return snapshot.toObjects(Debt::class.java)
    }

    // retrieve n paymentMethod documents by the active user
    suspend fun getNDebts(n: Int, id_user: String): List<Debt> {
        val snapshot = debtRef.whereEqualTo("id_user", id_user).get().await()
        return snapshot.toObjects(Debt::class.java)
    }
}