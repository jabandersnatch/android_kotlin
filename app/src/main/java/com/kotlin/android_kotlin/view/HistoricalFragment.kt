package com.kotlin.android_kotlin.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kotlin.android_kotlin.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.android_kotlin.data.transaction.Transaction
import com.kotlin.android_kotlin.view.adapters.LastMovAdapter
import com.kotlin.android_kotlin.domain.transaction.TransactionRepository
import com.kotlin.android_kotlin.viewmodel.transaction.TransactionViewModelFactory
import com.kotlin.android_kotlin.viewmodel.transaction.TransactionViewModel
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoricalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoricalFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: TransactionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = TransactionRepository()
        viewModel = ViewModelProvider(
            this,
            TransactionViewModelFactory(repository)
        )[TransactionViewModel::class.java]
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_historical, container, false)
        recyclerView = view.findViewById(R.id.transaction_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val userID = requireArguments().getString("userID")
        Log.i("user", "onCreateView: $userID")
        val transactions = ArrayList<Transaction>()
        if (userID != null) {
            viewModel.getAllTransactions(userID)

            viewModel.transactionList.observe(viewLifecycleOwner) { transactionList ->
                transactions.clear()
                transactions.addAll(transactionList)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }


        recyclerView.adapter = LastMovAdapter(transactions)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoricalFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoricalFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}