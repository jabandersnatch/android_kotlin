package com.kotlin.android_kotlin.domain.transaction

import android.util.Log
import com.kotlin.android_kotlin.data.transaction.Transaction
import com.kotlin.android_kotlin.data.transaction.TransactionDao

class TransactionRepository {
    // Retrieve all transactions
    suspend fun getAllTransactions(id_user: String): List<Transaction> {
        val transactions = TransactionDao().getAllTransactions(id_user)
        Log.i("TransactionRepository", "getAllTransactions: $transactions")
        return transactions
    }

    // Retrieve n transactions
    suspend fun getNTransactions(n: Int, id_user: String): List<Transaction> {
        return TransactionDao().getNTransactions(n, id_user)
    }

    // Retrieve a specific transaction by its ID
    suspend fun getTransactionById(transactionId: String): Transaction {
        return TransactionDao().getTransactionById(transactionId)
    }

    // Add a new transaction
    suspend fun addTransaction(transaction: Transaction) {
        TransactionDao().addTransaction(transaction)
    }

    // Update a transaction
    suspend fun updateTransaction(transactionId: String, transaction: Transaction) {
        TransactionDao().updateTransaction(transactionId, transaction)
    }

    // Delete a transaction
    suspend fun deleteTransaction(transactionId: String) {
        TransactionDao().deleteTransaction(transactionId)
    }

    // get the total amount of all transactions withing a date range
    suspend fun getTotalAmount(startdate: String, enddate: String, id_user: String): Double {
        return TransactionDao().getTotalAmount(startdate, enddate, id_user)
    }
}