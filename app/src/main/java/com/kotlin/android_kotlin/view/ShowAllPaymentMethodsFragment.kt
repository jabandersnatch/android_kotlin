package com.kotlin.android_kotlin.view

import android.os.Bundle
import android.util.SparseArray
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.kotlin.android_kotlin.R
import com.kotlin.android_kotlin.data.paymentMethod.PaymentMethod
import com.kotlin.android_kotlin.domain.paymentMethod.PaymentMethodRepository
import com.kotlin.android_kotlin.view.adapters.PaymentMethodsAdapter
import com.kotlin.android_kotlin.viewmodel.paymentMethod.PaymentMethodViewModel
import com.kotlin.android_kotlin.viewmodel.paymentMethod.PaymentMethodViewModelFactory


class ShowAllPaymentMethodsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: PaymentMethodViewModel
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = PaymentMethodRepository()
        viewModel = ViewModelProvider(
            this,
            PaymentMethodViewModelFactory(repository)
        )[PaymentMethodViewModel::class.java]
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_show_all_payment_methods, container, false)

        val userID = requireArguments().getString("userID")
        recyclerView = view.findViewById(R.id.paymentMethods_list)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val paymentMethods = SparseArray<PaymentMethod>()
        if (userID != null) {
            viewModel.getAllPaymentMethods(userID)

            viewModel.paymentMethodList.observe(viewLifecycleOwner) { paymentMethodList ->
                paymentMethods.clear()
                for (index in 0 until paymentMethodList.size) {
                    paymentMethods.put(index, paymentMethodList[index])
                }
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }

        recyclerView.adapter = PaymentMethodsAdapter(paymentMethods)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}