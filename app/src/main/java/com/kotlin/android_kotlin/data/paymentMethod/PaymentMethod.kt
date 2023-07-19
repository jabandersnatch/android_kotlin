package com.kotlin.android_kotlin.data.paymentMethod

data class PaymentMethod(
    val icon: String,
    val id_user: String,
    val name: String,
    val value: Double,
) {
    constructor() : this("", "", "", 0.0)
}