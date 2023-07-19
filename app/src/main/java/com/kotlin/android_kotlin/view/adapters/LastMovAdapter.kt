package com.kotlin.android_kotlin.view.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.android_kotlin.R
import java.text.SimpleDateFormat
import com.kotlin.android_kotlin.data.transaction.Transaction
import java.util.*


class LastMovAdapter(private val transactions: List<Transaction>) : RecyclerView.Adapter<LastMovAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_last_mov, parent, false)
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
        private val nameTextView: TextView = itemView.findViewById(R.id.transaction_name)
        private val dateTextView: TextView = itemView.findViewById(R.id.transaction_date)
        private val valueTextView: TextView = itemView.findViewById(R.id.transaction_value)
        private val paymentMethodTextView: TextView = itemView.findViewById(R.id.transaction_payment_method)
        private val categoryTextView: TextView = itemView.findViewById(R.id.transaction_category)
        private val arrowImageView: ImageView = itemView.findViewById(R.id.transaction_arrow)

        fun bind(transaction: Transaction) {
            nameTextView.text = transaction.name

            val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
            val dateFormat = SimpleDateFormat("dd/MM/yy HH:mm", Locale.ENGLISH)
            try {
                val date = inputFormat.parse(transaction.date.toDate().toString())
                val outputDate = dateFormat.format(date)
                dateTextView.text = outputDate
                Log.i("fecha", "booleano: ${transaction.date}")
                Log.i("fecha2", "booleano: ${outputDate}")
            } catch (e: Exception) {
                println("Ocurrió un error: ${e.message}")
            }

            // Save the value as a string with 2 decimal places and a dollar sign
            valueTextView.text = "$%.2f".format(transaction.value)
            paymentMethodTextView.text = transaction.paymentmethod
            categoryTextView.text=transaction.category

            // Set the arrow image based on the value the color based on the value
            if (transaction.category == "Salary") {
                arrowImageView.setImageResource(R.drawable.baseline_arrow_upward_24)
                arrowImageView.setColorFilter(Color.rgb(165, 190, 0))
                valueTextView.setTextColor(itemView.context.getColor(R.color.green))
            } else {
                arrowImageView.setImageResource(R.drawable.baseline_arrow_downward_24)
                arrowImageView.setColorFilter(Color.rgb(244, 67, 54))
                valueTextView.setTextColor(itemView.context.getColor(R.color.red))
            }

        }
    }
}
