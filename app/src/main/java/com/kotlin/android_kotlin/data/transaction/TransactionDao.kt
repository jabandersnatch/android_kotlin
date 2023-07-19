package com.kotlin.android_kotlin.data.transaction

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class TransactionDao {

    private val db = FirebaseFirestore.getInstance()
    private val transactionref = db.collection("transactions")

    suspend fun addTransaction(transaction: Transaction) {
        transactionref.document().set(transaction).await()
    }

    suspend fun getTransactionById(transactionId: String): Transaction {
        val snapshot = transactionref.document(transactionId).get().await()
        return snapshot.toObject(Transaction::class.java)!!
    }

    // retrieve all transaction documents ordered by date made by the active user
    suspend fun getAllTransactions(id_user: String): List<Transaction> {
        Log.i("TransactionDao", id_user)
        val snapshot = transactionref.whereEqualTo("id_user", id_user).orderBy("date", Query.Direction.DESCENDING).limit(20).get().await()
        Log.i("TransactionDao", snapshot.toString())
        val transactions = snapshot.toObjects(Transaction::class.java)
        Log.i("TransactionDao", transactions.toString())
        return transactions
    }

    // retrieve n transaction documents ordered by date made by the active user
    suspend fun getNTransactions(n: Int, id_user: String): List<Transaction> {
        val snapshot = transactionref.whereEqualTo("id_user", id_user).orderBy("date", Query.Direction.DESCENDING).limit(n.toLong()).get().await()
        return snapshot.toObjects(Transaction::class.java)
    }

    // get the total amount of all transactions withing a date range
    suspend fun getTotalAmount(startdate: String, enddate: String, id_user: String): Double {
        val snapshot = transactionref.whereEqualTo("id_user", id_user).whereGreaterThanOrEqualTo("date", startdate).whereLessThanOrEqualTo("date", enddate).get().await()
        val transactions = snapshot.toObjects(Transaction::class.java)
        var total = 0.0
        for (transaction in transactions) {
            total += transaction.value
        }
        return total
    }

    // update the transaction
    suspend fun updateTransaction(transactionId: String, transaction: Transaction) {
        val docref = transactionref.document(transactionId)
        docref.set(transaction).await()
    }

    // delete a transaction document

    suspend fun deleteTransaction(transactionId: String) {
        transactionref.document(transactionId).delete().await()
    }
}