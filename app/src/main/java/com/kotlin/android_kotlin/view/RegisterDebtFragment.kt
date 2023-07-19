package com.kotlin.android_kotlin.view

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.kotlin.android_kotlin.R
import com.kotlin.android_kotlin.data.debts.Debt
import com.kotlin.android_kotlin.domain.debt.DebtRepository
import com.kotlin.android_kotlin.viewmodel.debtViewModel.DebtViewModel
import com.kotlin.android_kotlin.viewmodel.debtViewModel.DebtViewModelFactory


class RegisterDebtFragment : Fragment() {

    private lateinit var viewModel: DebtViewModel
    private lateinit var txtInputName: TextInputLayout
    private lateinit var debtName: TextInputEditText
    private lateinit var txtInputValue: TextInputLayout
    private lateinit var debtValue: TextInputEditText
    private lateinit var txtInputCreditor: TextInputLayout
    private lateinit var debtCreditor: TextInputEditText

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_debt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtInputName = requireView().findViewById(R.id.txtInputName)
        debtName=  requireView().findViewById(R.id.editTextName)

        txtInputValue= requireView().findViewById(R.id.txtInputValue)
        debtValue= requireView().findViewById(R.id.editTextValue)

        txtInputCreditor= requireView().findViewById(R.id.txtInputCreditor)
        debtCreditor= requireView().findViewById(R.id.editTextCreditor)

        val btnAddNewDebt = requireView().findViewById<Button>(R.id.btnAddNewDebt)
        btnAddNewDebt.setOnClickListener {

            if (checkConnectivity()) {

                if (validate()) {
                    val userId = requireArguments().getString("userID")
                    val name: String = debtName.text.toString()
                    val value: String = debtValue.text.toString()
                    val creditor: String = debtCreditor.text.toString()


                    val debt = Debt(creditor, userId.toString(), name, false, value.toInt())
                    viewModel.addDebt(debt)

                    viewModel.isLoading.observe(viewLifecycleOwner, Observer { idLoading ->
                        if (idLoading == false) {

                            btnAddNewDebt.isEnabled = false

                            Handler().postDelayed({
                                btnAddNewDebt.isEnabled = true
                            }, 2000)

                            debtName.setText("")
                            txtInputName.clearFocus()
                            debtValue.setText("")
                            txtInputValue.clearFocus()
                            debtCreditor.setText("")
                            txtInputCreditor.clearFocus()

                            showAlert("Register successful","You debt was registered successfully")
                        }
                    })

                    btnAddNewDebt.isEnabled = false

                    Handler().postDelayed({
                        btnAddNewDebt.isEnabled = true
                    }, 2000)

                    debtName.setText("")
                    txtInputName.clearFocus()
                    debtValue.setText("")
                    txtInputValue.clearFocus()
                    debtCreditor.setText("")
                    txtInputCreditor.clearFocus()
                }
            } else {showAlert("Wifi connection error","No internet access, please check your connection and try again")}
        }
    }

    private fun validate(): Boolean{
        var bool = true
        val name: String = debtName.text.toString()
        val value: String = debtValue.text.toString()
        val creditor: String = debtCreditor.text.toString()

        //*******************NAME*******************
        if (name.isEmpty()|| name.isBlank()|| name.isNullOrEmpty()||name.isDigitsOnly()) {
            txtInputName.error = "Enter a valid name"
            bool = false
        }
        else if(name.length>15){
            txtInputName.error = "Enter a shorter name for the transaction"
            bool = false
        }
        else{
            txtInputName.isErrorEnabled=false
        }

        //*******************CREDITOR*******************
        if (creditor.isEmpty()|| creditor.isBlank()|| creditor.isNullOrEmpty()||creditor.isDigitsOnly()) {
            txtInputCreditor.error = "Enter a valid name"
            bool = false
        }
        else if(creditor.length>15){
            txtInputCreditor.error = "Enter a shorter name for the transaction"
            bool = false
        }
        else{
            txtInputCreditor.isErrorEnabled=false
        }

        //*******************VALUE*******************
        if (value.isEmpty()||value.isNullOrEmpty()) {
            txtInputValue.error = "Enter a value"
            bool = false
        }
        else if(value.length>9){
            txtInputValue.error = "Enter a value with no more that 10 digits for the transaction"
            bool = false
        }
        else if(value.toLong()<=0|| value.startsWith("0") ){
            txtInputValue.error = "Enter a positive value"
            bool = false
        }
        else{
            txtInputValue.isErrorEnabled=false
        }
        return bool
    }


    private fun checkConnectivity():Boolean{
        var isConnected = false
        val sc = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val redInfo = sc.allNetworkInfo

        for (ir in redInfo) {
            if (ir.typeName.equals("WIFI", ignoreCase = true) || ir.typeName.equals("MOBILE", ignoreCase = true)) {
                if (ir.isConnected) {
                    isConnected = true
                    break
                }
            }
        }
        return isConnected
    }

    private fun showAlert(title :String , message: String){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Accept", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}