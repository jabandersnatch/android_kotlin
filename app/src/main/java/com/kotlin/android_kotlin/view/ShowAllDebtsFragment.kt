package com.kotlin.android_kotlin.view

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.android_kotlin.R
import com.kotlin.android_kotlin.data.debts.Debt
import com.kotlin.android_kotlin.domain.debt.DebtRepository
import com.kotlin.android_kotlin.view.adapters.DebtsAdapter
import com.kotlin.android_kotlin.viewmodel.debtViewModel.DebtViewModel
import com.kotlin.android_kotlin.viewmodel.debtViewModel.DebtViewModelFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.*

class ShowAllDebtsFragment : Fragment(), LifecycleObserver  {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: DebtViewModel

    private lateinit var textConnectivity: TextView
    private lateinit var connectivityCheckJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = DebtRepository()
        viewModel = ViewModelProvider(
            this,
            DebtViewModelFactory(repository)
        )[DebtViewModel::class.java]
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_show_all_debts, container, false)
        textConnectivity = view.findViewById(R.id.connectionWarning)
        val userID = requireArguments().getString("userID")

        recyclerView = view.findViewById(R.id.debt_list)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val debts=ArrayList<Debt>()
        if (userID != null) {
            viewModel.getAllDebts(userID)

            viewModel.debtList.observe(viewLifecycleOwner) { debtList ->
                debts.clear()
                debts.addAll(debtList)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }

        recyclerView.adapter = DebtsAdapter(debts)
        return view
    }

    override fun onStart() {
        super.onStart()
        lifecycle.addObserver(this)
    }

    override fun onStop() {
        lifecycle.removeObserver(this)
        super.onStop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumeEvent() {
        startConnectivityCheck()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPauseEvent() {
        stopConnectivityCheck()
    }

    private fun startConnectivityCheck() {
        connectivityCheckJob = CoroutineScope(Dispatchers.IO).launch {
            while (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                var isConnected = false

                val sc = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = sc.activeNetworkInfo

                if (networkInfo != null && networkInfo.isConnected) {
                    isConnected = true
                }


                withContext(Dispatchers.Main) {
                    if (isConnected) {
                        textConnectivity.visibility = View.GONE;
                    } else {
                        textConnectivity.visibility = View.VISIBLE;
                    }
                }

                delay(1000)
            }
        }
    }

    private fun stopConnectivityCheck() {
        connectivityCheckJob.cancel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}