package com.kotlin.android_kotlin.controller

import android.content.Context
import com.kotlin.android_kotlin.model.AccessDB
import com.google.firebase.Timestamp

class AccessDB_Controller {
    private val accessDB = AccessDB()

    fun callRegisterFragment(
        context: Context,
        category: String,
        userId: String,
        date: Timestamp,
        name: String,
        value: Double?,
        paymentMethod: String
    ) {
        accessDB.insertData(context,category,userId,date,name,value,paymentMethod)
    }

    fun callHomeFragmentIncomes(userId: String, callback: (Double) -> Unit) {
        accessDB.getIncomes(userId) { incomes ->
            callback(incomes)
        }
    }

    fun callHomeFragmentOutcomes(userId: String, callback: (Double) -> Unit) {
        accessDB.getOutcomes(userId) { outcomes ->
            callback(outcomes)
        }
    }

    fun callHomeFragmentTotalSaved(userId: String, callback: (Double) -> Unit) {
        accessDB.getTotalSaved(userId) { totalSavedAllTimes ->
            callback(totalSavedAllTimes)
        }
    }

    fun callHomeFragmentTotalSpentLastMonth(userId: String, callback: (Double) -> Unit) {
        accessDB.getTotalSpentLastMonth(userId) { totalSpent ->
            callback(totalSpent)
        }
    }

    fun callHomeFragmentTotalTransLastMonth(userId: String, callback: (Int) -> Unit) {
        accessDB.getTotalTransLastMonth(userId) { totalTrans ->
            callback(totalTrans.toInt())
        }
    }

    fun callHomeFragmentTotalSpentLastWeek(userId: String, callback: (Double) -> Unit) {
        accessDB.getTotalSpentLastWeek(userId) { totalWeek ->
            callback(totalWeek)
        }
    }

    fun callHomeFragmentTotalSavedLastMonth(userId: String, callback: (Double) -> Unit) {
        accessDB.getTotalSavedLastMonth(userId) { totalSaved ->
            callback(totalSaved)
        }
    }

    fun callRegisterDebtNotification(
        context: Context,
        userId: String,
        name: String,
        value: Double,
        date: String
    ) {
        accessDB.insertDebtNotification(context,userId,name,value,date)
    }

    fun callRegisterDebt(
        context: Context,
        creditor:String,
        userId: String,
        name: String,
        paid:Boolean,
        value: Int
    ) {
        accessDB.insertDebt(context, creditor, userId, name, paid, value)
    }
}