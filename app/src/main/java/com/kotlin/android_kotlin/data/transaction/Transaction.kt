package com.kotlin.android_kotlin.data.transaction

import com.google.firebase.Timestamp

data class Transaction(
    val id_user: String,
    val name: String,
    val value: Double,
    val category: String,
    val paymentmethod: String,
    val date: Timestamp,
) {
    constructor() : this("", "", 0.0, "", "", Timestamp.now())
}