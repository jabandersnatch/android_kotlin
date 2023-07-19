package com.kotlin.android_kotlin.data.savingTips

data class SavingTips(
    val tip: String,
    val likes: Int
) {
    constructor() : this("", 0)
}