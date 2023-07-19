package com.kotlin.android_kotlin.view.adapters

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.android_kotlin.R
import com.kotlin.android_kotlin.data.paymentMethod.PaymentMethod

class PaymentMethodsAdapter(private val paymentMethods: SparseArray<PaymentMethod>) : RecyclerView.Adapter<PaymentMethodsAdapter.PaymentMethodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_payment_method, parent, false)
        return PaymentMethodViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentMethodViewHolder, position: Int) {
        val paymentMethod = paymentMethods[position]
        holder.bind(paymentMethod)
    }

    override fun getItemCount(): Int {
        return paymentMethods.size()
    }

    inner class PaymentMethodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameTextView: TextView = itemView.findViewById(R.id.paymentMethod_name)
        private val iconView: ImageView  = itemView.findViewById(R.id.paymentMethod_icon)
        private val valueTextView: TextView = itemView.findViewById(R.id.paymentMethod_value)

        fun bind(paymentMethod: PaymentMethod) {
            nameTextView.text = paymentMethod.name
            valueTextView.text = "$ " + paymentMethod.value.toString()

            if (paymentMethod.icon == "Card") {
                iconView.setImageResource(R.drawable.credit_card)
            }
            else if (paymentMethod.icon == "Bank") {
                iconView.setImageResource(R.drawable.bank_ran)
            }
            else if (paymentMethod.icon == "Cash") {
                iconView.setImageResource(R.drawable.cash)
            }
            else if (paymentMethod.icon == "Phone") {
                iconView.setImageResource(R.drawable.phone)
            }

        }
    }
}
