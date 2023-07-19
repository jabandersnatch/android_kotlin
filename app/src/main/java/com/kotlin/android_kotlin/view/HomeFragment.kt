package com.kotlin.android_kotlin.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kotlin.android_kotlin.R
import com.kotlin.android_kotlin.view.adapters.LastMovAdapter
import com.kotlin.android_kotlin.controller.AccessDB_Controller
import com.kotlin.android_kotlin.data.transaction.Transaction


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private lateinit var recyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private val accessDB_Controller = AccessDB_Controller()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val email = requireArguments().getString("email")

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val emailTextView = view.findViewById<TextView>(R.id.textWelcome)
        val username = requireArguments().getString("username")
        emailTextView.text = "Welcome $username!"

        recyclerView = view.findViewById(R.id.transaction_list)
        recyclerView.layoutManager = LinearLayoutManager(context)


        val userID = requireArguments().getString("userID")
        //Log.i("user", "onCreateView: $userID")
        recyclerView.adapter = LastMovAdapter(getTransactionsMadeByUser(userID))
        val budgetTextView = view.findViewById<TextView>(R.id.textView4)
        accessDB_Controller.callHomeFragmentTotalSaved(userID.toString()) { totalSavedAllTimes ->
            // Set the text of the spentLastMonth text view
            budgetTextView.text = "$ $totalSavedAllTimes"
        }

        // Geth the spentLastMonth text view from the linear layout
        val spentLastMonthTextView = view.findViewById<TextView>(R.id.spentLastMonth)
        val transLastMonthTextView = view.findViewById<TextView>(R.id.transactionsLastMonth)
        val spentLastWeekTextView = view.findViewById<TextView>(R.id.spentLastWeek)
        val savedLastMonthTextView = view.findViewById<TextView>(R.id.savedLastMonth)
        val incomesTextView = view.findViewById<TextView>(R.id.incomes)
        val outcomesTextView = view.findViewById<TextView>(R.id.outcomes)
        // Get the total spent in the last month
        accessDB_Controller.callHomeFragmentIncomes(userID.toString()) { incomes ->
            // Set the text of the spentLastMonth text view
            incomesTextView.text = "$$incomes"
        }

        accessDB_Controller.callHomeFragmentOutcomes(userID.toString()) { outcomes ->
            // Set the text of the spentLastMonth text view
            outcomesTextView.text = "$$outcomes"
        }


        accessDB_Controller.callHomeFragmentTotalSpentLastMonth(userID.toString()) { totalSpent ->
            // Set the text of the spentLastMonth text view
            spentLastMonthTextView.text = "Spent last month: $$totalSpent"
        }

        accessDB_Controller.callHomeFragmentTotalTransLastMonth(userID.toString()) { totalTrans ->
            // Set the text of the spentLastMonth text view
            transLastMonthTextView.text = "Transactions in the last month: $totalTrans"
        }
        accessDB_Controller.callHomeFragmentTotalSpentLastWeek(userID.toString()) { totalWeek ->
            // Set the text of the spentLastMonth text view
            spentLastWeekTextView.text = "Spent last week: $$totalWeek"
        }

        accessDB_Controller.callHomeFragmentTotalSavedLastMonth(userID.toString()) { totalSaved ->
            // Set the text of the spentLastMonth text view
            savedLastMonthTextView.text = "Last month profit: $$totalSaved"
        }


        val activity = requireActivity() as MainActivity
        val btnNewMovement = view.findViewById<Button>(R.id.button)
        btnNewMovement.setOnClickListener{
            activity.setFragmentInContainer(R.id.registerMovementFragment, requireArguments())
        }

        val btnSavingTips = view.findViewById<Button>(R.id.button4)
        btnSavingTips.setOnClickListener{
            activity.setFragmentInContainer(R.id.savingTipsFragment, requireArguments())
        }

        val btnSeeAllDebts = view.findViewById<Button>(R.id.btnAllDebts)
        btnSeeAllDebts.setOnClickListener{
            activity.setFragmentInContainer(R.id.showAllDebtsFragment, requireArguments())
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun getTransactionsMadeByUser(userID: String?): MutableList<Transaction> {
        val transactions = mutableListOf<Transaction>()
        var count = 0 // contador para el número de transacciones agregadas
        db.collection("transactions")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data["id_user"].toString() == userID) {
                        // Use the Timestamp class to convert the date from the database to a Date object
                        val timestamp = document.data["date"] as Timestamp

                        // Convert the date to a string

                        if (count < 5) { // solo agregar las primeras 5 transacciones
                            transactions.add(
                                Transaction(
                                    id_user = document.data["id_user"].toString(),
                                    name = document.data["name"].toString(),
                                    value = document.data["value"].toString().toDouble(),
                                    category = document.data["category"].toString(),
                                    paymentmethod = document.data["paymentmethod"].toString(),
                                    date = timestamp
                                )
                            )
                            count++
                        } else {
                            break // salir del ciclo si ya se agregaron 5 transacciones
                        }
                    }
                }
                recyclerView.adapter = LastMovAdapter(transactions)

            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
        return transactions
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

}