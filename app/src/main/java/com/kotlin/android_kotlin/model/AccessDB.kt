package com.kotlin.android_kotlin.model

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import com.kotlin.android_kotlin.data.transaction.Transaction
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.util.*
import kotlin.collections.ArrayList

class AccessDB {
    private val db = FirebaseFirestore.getInstance()
    var totalSpent = 0.0
    var totalTrans=0
    var totalWeek = 0.0
    var totalSaved = 0.0
    var totalSavedAllTimes=0.0
    var incomes=0.0
    var outcomes=0.0
    public fun insertData(context: Context, category:String, userId:String, date: Timestamp, name:String, value: Double?, paymentMethod:String) {

        // New document with a generated ID
        val document = db.collection("transactions").document()

        // HashMap object with the fields to add to the document
        val data = hashMapOf(
            "category" to category,
            "date" to date,
            "id_user" to userId,
            "name" to name,
            "paymentmethod" to paymentMethod,
            "value" to value
        )

        // Insert data into the document
        document.set(data)
            .addOnSuccessListener {
               
            }
            .addOnFailureListener {
                // Handle insert failure
            }
    }

    public fun insertDebtNotification(context: Context, userId: String, name: String, value: Double, date: String) {

        // New document with a generated ID
        val document = db.collection("debt_notification").document()

        // HashMap object with the fields to add to the document
        val data = hashMapOf(
            "date" to date,
            "id_user" to userId,
            "name" to name,
            "value" to value
        )

        // Insert data into the document
        document.set(data)
            .addOnSuccessListener {
               
            }
            .addOnFailureListener {
                // Handle insert failure
            }
    }

    public fun insertDebt(context: Context, creditor:String, userId: String, name: String, paid: Boolean , value: Int) {

        // New document with a generated ID
        val document = db.collection("debt").document()

        // HashMap object with the fields to add to the document
        val data = hashMapOf(
            "creditor" to creditor,
            "id_user" to userId,
            "name" to name,
            "paid" to paid,
            "value" to value
        )

        // Insert data into the document
        document.set(data)
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                // Handle insert failure
            }
    }

    public fun getIncomes(userID: String?, callback: (Double) -> Unit) {

        // Get the transactions made by the user
        getTransactionsMadeByUser(userID) { transactions: ArrayList<Transaction> ->
            // Loop through the transactions
            for (transaction in transactions) {

                if(transaction.category =="Salary"){
                    // Substract the value of the transaction to the total saved
                    incomes += transaction.value
                }

            }
            callback(incomes)
        }
    }

    public fun getOutcomes(userID: String?, callback: (Double) -> Unit) {

        // Get the transactions made by the user
        getTransactionsMadeByUser(userID) { transactions: ArrayList<Transaction> ->
            // Loop through the transactions
            for (transaction in transactions) {

                if(transaction.category !="Salary"){
                    // Substract the value of the transaction to the total saved
                    outcomes += transaction.value
                }

            }
            callback(outcomes)
        }
    }

    public fun getTotalSaved(userID: String?, callback: (Double) -> Unit) {

        // Get the transactions made by the user
        getTransactionsMadeByUser(userID) { transactions: ArrayList<Transaction> ->
            // Loop through the transactions
            for (transaction in transactions) {

                if(transaction.category !="Salary"){
                    // Substract the value of the transaction to the total saved
                    totalSavedAllTimes += transaction.value
                }
                else{
                    totalSavedAllTimes += transaction.value
                }

            }
            callback(totalSavedAllTimes)
        }
    }


    // Create a function that gets the total spent in the last month
    // This should take the userID as a parameter and return a double
    // with the total spent in the last month
    public fun getTotalSpentLastMonth(userID: String?, callback: (Double) -> Unit) {

        // Get the current date
        val currentMonth = LocalDate.now().monthValue
        // Get the date of the last month
        val lastMonthDate = LocalDate.now().minusMonths(1).monthValue

        // Get the transactions made by the user
        getTransactionsMadeByUser(userID) { transactions: ArrayList<Transaction> ->
            val format = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.US)
            // Loop through the transactions
            for (transaction in transactions) {
                val transDate = format.parse(transaction.date.toDate().toString())
                val calendar = Calendar.getInstance()
                calendar.time = transDate
                val transMonth = calendar.get(Calendar.MONTH) + 1

                // Check if the transaction is in the last month
                /*Log.i("dateB", "fecha: ${transMonth}")
                Log.i("mes1", "fecha: ${lastMonthDate}")
                Log.i("mes2", "fecha: ${currentMonth}")*/

                if (transMonth >= lastMonthDate && transMonth < currentMonth) {
                    // Add the value of the transaction to the total spent
                    if (transaction.value < 0){
                    totalSpent += transaction.value
                    }
                }
            }

            // Return the total spent
            callback(totalSpent)
        }
    }
    public fun getTotalTransLastMonth(userID: String?, callback: (Double) -> Unit) {

        // Get the current date
        val currentMonth = LocalDate.now().monthValue
        // Get the date of the last month
        val lastMonthDate = LocalDate.now().minusMonths(1).monthValue

        // Get the transactions made by the user
        getTransactionsMadeByUser(userID) { transactions: ArrayList<Transaction> ->
            val format = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.US)
            // Loop through the transactions


            for (transaction in transactions) {
                val transDate = format.parse(transaction.date.toDate().toString())
                val calendar = Calendar.getInstance()
                calendar.time = transDate
                val transMonth = calendar.get(Calendar.MONTH) + 1

                // Check if the transaction is in the last month
                /*Log.i("dateB", "fecha: ${transMonth}")
                Log.i("mes1", "fecha: ${lastMonthDate}")
                Log.i("mes2", "fecha: ${currentMonth}")*/
                if (transMonth >= lastMonthDate && transMonth < currentMonth) {
                    // Add the value of the transaction to the total spent
                    totalTrans+= 1
                }
                //Log.i("valor", "getTotalSpentLastMonth: ${transaction.value}")
            }

            //Log.i("tot", "getTotalSpentLastMonth: $totalSpent")
            // Return the total spent
            callback(totalTrans.toDouble())
        }
    }

    public fun getTotalSpentLastWeek(userID: String?, callback: (Double) -> Unit) {

        // Get the current date
        // Get the date of the last month
        val lastSunday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        val lastMonday = lastSunday.minusDays(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val lastWeek = lastMonday..lastSunday

        //Log.i("dateL", "fecha: ${lastWeek}")
        // Get the transactions made by the user
        getTransactionsMadeByUser(userID) { transactions: ArrayList<Transaction> ->
            val format = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.US)
            // Loop through the transactions
            for (transaction in transactions) {
                // Check if the transaction is in the last week


                val transDate = format.parse(transaction.date.toDate().toString())
                val instant = transDate.toInstant()
                val zonedDate = instant.atZone(ZoneId.systemDefault())
                val transLocalDate = zonedDate.toLocalDate()

                if (transLocalDate in lastWeek ) {
                    if (transaction.value < 0){
                        totalWeek += transaction.value
                    }
                }
            }

            // Return the total spent
            callback(totalWeek)
        }
    }

    public fun getTotalSavedLastMonth(userID: String?, callback: (Double) -> Unit) {

        // Get the current date
        val currentMonth = LocalDate.now().monthValue
        // Get the date of the last month
        val lastMonthDate = LocalDate.now().minusMonths(1).monthValue

        // Get the transactions made by the user
        getTransactionsMadeByUser(userID) { transactions: ArrayList<Transaction> ->
            val format = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.US)
            // Loop through the transactions
            for (transaction in transactions) {
                val transDate = format.parse(transaction.date.toDate().toString())
                val calendar = Calendar.getInstance()
                calendar.time = transDate
                val transMonth = calendar.get(Calendar.MONTH) + 1

                // Check if the transaction is in the last month
                /*Log.i("dateB", "fecha: ${transMonth}")
                Log.i("mes1", "fecha: ${lastMonthDate}")
                Log.i("mes2", "fecha: ${currentMonth}")*/

                if (transMonth >= lastMonthDate && transMonth < currentMonth ) {
                    if(transaction.category !="Salary"){
                        // Substract the value of the transaction to the total saved
                        totalSaved += transaction.value
                    }
                    else{
                        totalSaved += transaction.value
                    }

                }
            }

            // Return the total saved
            callback(totalSaved)
        }
    }


    // Create a function that gets the transactions made by a user
    // This should take the userID as a parameter and return an ArrayList
    // with the transactions made by the user
    fun getTransactionsMadeByUser(userID: String?, callback: (ArrayList<Transaction>) -> Unit) {

        // Get the transactions from the database
        db.collection("transactions")
            .get()
            .addOnSuccessListener { result ->

                // Create an ArrayList to store the transactions
                val transactions = ArrayList<Transaction>()
                // Loop through the transactions
                for (document in result) {
                    // Check if the transaction was made by the user
                    if (document.data["id_user"].toString() == userID) {
                        // Use the Timestamp class to convert the date from the database to a Date object
                        val timestamp = document.data["date"] as Timestamp

                        // Convert the date to a string

                        transactions.add(
                            Transaction(
                                id_user = document.data["id_user"].toString(),
                                name = document.data["name"].toString(),
                                value = document.data["value"].toString().toDouble(),
                                category = document.data["category"].toString(),
                                paymentmethod = document.data["paymentmethod"].toString(),
                                date = timestamp,
                            )
                        )
                    }
                }
                // Return the transactions
                callback(transactions)
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
    }

}

