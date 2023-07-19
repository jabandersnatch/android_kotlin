package com.kotlin.android_kotlin.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.android_kotlin.R
import com.kotlin.android_kotlin.data.debts.Debt

class DebtsAdapter(private val transactions: List<Debt>) : RecyclerView.Adapter<DebtsAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_debt, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.bind(transaction)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameTextView: TextView = itemView.findViewById(R.id.debt_name)
        private val creditorTextView: TextView = itemView.findViewById(R.id.creditor)
        private val valueTextView: TextView = itemView.findViewById(R.id.debt_value)

        fun bind(transaction: Debt) {
            nameTextView.text = transaction.name
            valueTextView.text = "$ " + transaction.value.toString()
            creditorTextView.text = "Is owed to: "+transaction.creditor
        }
    }
}
