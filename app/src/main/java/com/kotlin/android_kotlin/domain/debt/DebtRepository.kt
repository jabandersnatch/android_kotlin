package com.kotlin.android_kotlin.domain.debt

import com.kotlin.android_kotlin.data.debts.Debt
import com.kotlin.android_kotlin.data.debts.DebtDao

class DebtRepository {

    // Retrieve all paymentMethods
    suspend fun getAllDebts(id_user: String): List<Debt> {
        return DebtDao().getAllDebts(id_user)
    }

    // Retrieve n paymentMethods
    suspend fun getNDebts(n: Int, id_user: String): List<Debt> {
        return DebtDao().getNDebts(n, id_user)
    }

    // Add a new paymentMethods
    suspend fun addDebt(debt: Debt) {
        DebtDao().addDebt(debt)
    }
}