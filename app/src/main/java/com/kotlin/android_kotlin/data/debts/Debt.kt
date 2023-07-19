package com.kotlin.android_kotlin.data.debts

data class Debt(
    val creditor: String,
    val id_user: String,
    val name: String,
    val paid: Boolean,
    val value: Int
) {
    constructor() : this("", "", "", false, 0)
}