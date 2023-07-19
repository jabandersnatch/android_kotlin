package com.kotlin.android_kotlin.view

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.kotlin.android_kotlin.R
import com.kotlin.android_kotlin.controller.AccessDB_Controller
import com.google.firebase.Timestamp
import com.kotlin.android_kotlin.data.transaction.Transaction
import com.kotlin.android_kotlin.domain.paymentMethod.PaymentMethodRepository
import com.kotlin.android_kotlin.domain.transaction.TransactionRepository
import com.kotlin.android_kotlin.viewmodel.paymentMethod.PaymentMethodViewModel
import com.kotlin.android_kotlin.viewmodel.paymentMethod.PaymentMethodViewModelFactory
import com.kotlin.android_kotlin.viewmodel.transaction.TransactionViewModel
import com.kotlin.android_kotlin.viewmodel.transaction.TransactionViewModelFactory


private operator fun Boolean.invoke(value: () -> Unit) {

}

class RegisterMovementFragment : Fragment() {

    private val accessDB_Controller = AccessDB_Controller()
    private lateinit var txtInputName:TextInputLayout
    private lateinit var txtInputValue:TextInputLayout
    private lateinit var valueMovement:TextInputEditText
    private lateinit var nameMovement:TextInputEditText
    var paymentMethod: String? =null
    private lateinit var viewModelTransactions: TransactionViewModel
    private lateinit var viewModelPaymentMethods: PaymentMethodViewModel
    private var color=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repositoryTransactions = TransactionRepository()
        viewModelTransactions = ViewModelProvider(
            this,
            TransactionViewModelFactory(repositoryTransactions)
        )[TransactionViewModel::class.java]
        arguments?.let {

        }

        val repositoryPaymentMethods = PaymentMethodRepository()
        viewModelPaymentMethods = ViewModelProvider(
            this,
            PaymentMethodViewModelFactory(repositoryPaymentMethods)
        )[PaymentMethodViewModel::class.java]
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_register_movement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Cambiar categoria
        val btnFood= requireView().findViewById<ImageButton>(R.id.imageFood)
        val btnTravel= requireView().findViewById<ImageButton>(R.id.imageTravel)
        val btnClothing= requireView().findViewById<ImageButton>(R.id.imageClothing)
        val btnSalary= requireView().findViewById<ImageButton>(R.id.imageSalary)

        val colorPresionado = ContextCompat.getColor(requireContext(), R.color.colorPressed)
        val colorNormal = ContextCompat.getColor(requireContext(), R.color.colorNormal)

        var category = "Food"

        btnFood.backgroundTintList = ColorStateList.valueOf(colorPresionado)
        btnTravel.backgroundTintList = ColorStateList.valueOf(color)
        btnClothing.backgroundTintList = ColorStateList.valueOf(color)
        btnSalary.backgroundTintList = ColorStateList.valueOf(color)
        //Cambia el string de categoria
        btnFood.setOnClickListener {
            category = "Food"
            btnFood.backgroundTintList = ColorStateList.valueOf(colorPresionado)
            btnTravel.backgroundTintList = ColorStateList.valueOf(color)
            btnClothing.backgroundTintList = ColorStateList.valueOf(color)
            btnSalary.backgroundTintList = ColorStateList.valueOf(color)

        }
        btnTravel.setOnClickListener {
            category = "Travel"
            btnFood.backgroundTintList = ColorStateList.valueOf(color)
            btnTravel.backgroundTintList = ColorStateList.valueOf(colorPresionado)
            btnClothing.backgroundTintList = ColorStateList.valueOf(color)
            btnSalary.backgroundTintList = ColorStateList.valueOf(color)

        }
        btnClothing.setOnClickListener {
            category = "Clothing"
            btnFood.backgroundTintList = ColorStateList.valueOf(color)
            btnTravel.backgroundTintList = ColorStateList.valueOf(color)
            btnClothing.backgroundTintList = ColorStateList.valueOf(colorPresionado)
            btnSalary.backgroundTintList = ColorStateList.valueOf(color)
        }
        btnSalary.setOnClickListener {
            category = "Salary"
            btnFood.backgroundTintList = ColorStateList.valueOf(color)
            btnTravel.backgroundTintList = ColorStateList.valueOf(color)
            btnClothing.backgroundTintList = ColorStateList.valueOf(color)
            btnSalary.backgroundTintList = ColorStateList.valueOf(colorPresionado)
        }


        val userId = requireArguments().getString("userID")
        txtInputValue= requireView().findViewById(R.id.txtInputValue)
        txtInputName = requireView().findViewById(R.id.txtInputName)
        nameMovement=  requireView().findViewById<TextInputEditText>(R.id.editTextNameMovement)
        valueMovement= requireView().findViewById<TextInputEditText>(R.id.editTextValue)

        val spinnerAccount: Spinner = view.findViewById(R.id.spinner)

        if (userId != null) {
            viewModelPaymentMethods.paymentMethodNamesList.observe(viewLifecycleOwner) { paymentMethodNamesList ->
                val adapter = if (paymentMethodNamesList.isNotEmpty()) {
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, paymentMethodNamesList)
                } else {
                    val emptyListMessage = "No payment methods registered"
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOf(emptyListMessage))
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerAccount.adapter = adapter
            }
            viewModelPaymentMethods.getNamesPaymentMethods(userId)

        }


        val btnRegisterMovement = requireView().findViewById<Button>(R.id.addMovement)
        btnRegisterMovement.setOnClickListener {
            spinnerAccount.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {

                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // Code to perform some action when nothing is selected
                    }
                }

                paymentMethod = spinnerAccount.selectedItem.toString()

                if (validate()) {
                    val name = nameMovement.text.toString()
                    var value = valueMovement.text.toString().toDouble()
                    // get the current date
                    val date = Timestamp.now()


                    if (category != "Salary"){
                        value= -value
                    }
                    val transaction =Transaction(userId.toString(),name,value,category,
                        paymentMethod!!,date)


                    viewModelTransactions.addTransaction(transaction)


                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Register successful")
                    builder.setMessage("You registered you movement successfully")
                    builder.setPositiveButton("Accept", null)
                    builder.setCancelable(false)
                    val dialog: AlertDialog = builder.create()
                    dialog.show()

                    viewModelTransactions.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
                        if (isLoading == false) {

                            btnRegisterMovement.isEnabled = false

                            Handler().postDelayed({
                                btnRegisterMovement.isEnabled = true
                            }, 2000)

                            nameMovement.setText("")
                            txtInputName.clearFocus()
                            valueMovement.setText("")
                            txtInputValue.clearFocus()

                        }
                    })
                    btnRegisterMovement.isEnabled = false

                    Handler().postDelayed({
                        btnRegisterMovement.isEnabled = true
                    }, 2000)

                    nameMovement.setText("")
                    txtInputName.clearFocus()
                    valueMovement.setText("")
                    txtInputValue.clearFocus()

                }



                else{
                    //Log.i("valo3r", "booleano: entroelse")
                    //Toast.makeText(context,"Please complete all the fields", Toast.LENGTH_SHORT).show()

                }

        }


    }
    private fun isDarkThemeOn(): Boolean {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
    private fun setImage() {
        val btnFood= requireView().findViewById<ImageButton>(R.id.imageFood)
        val btnTravel= requireView().findViewById<ImageButton>(R.id.imageTravel)
        val btnClothing= requireView().findViewById<ImageButton>(R.id.imageClothing)
        val btnSalary= requireView().findViewById<ImageButton>(R.id.imageSalary)
        if (isDarkThemeOn()) {
            color=Color.rgb(74, 89, 101)
            btnFood?.setImageResource(R.drawable.food_dark)

            btnTravel?.setImageResource(R.drawable.travel_dark)
            btnTravel?.backgroundTintList = ColorStateList.valueOf(color)

            btnClothing?.setImageResource(R.drawable.clothing_dark)
            btnClothing?.backgroundTintList = ColorStateList.valueOf(color)

            btnSalary?.setImageResource(R.drawable.salary_dark)
            btnSalary?.backgroundTintList = ColorStateList.valueOf(color)

        } else {
            color=Color.rgb(235, 242, 250)
            btnFood?.setImageResource(R.drawable.food)


            btnTravel?.setImageResource(R.drawable.travel)
            btnTravel?.backgroundTintList = ColorStateList.valueOf(color)

            btnClothing?.setImageResource(R.drawable.clothing)
            btnClothing?.backgroundTintList = ColorStateList.valueOf(color)

            btnSalary?.setImageResource(R.drawable.salary)
            btnSalary?.backgroundTintList = ColorStateList.valueOf(color)
        }
    }
    override fun onResume() {
        super.onResume()
        setImage()
    }

     fun validate(): Boolean{
        var bool = true
        var name: String
        var value: String
        var method: String
        name = nameMovement.text.toString()
        value = valueMovement.text.toString()
        if(paymentMethod==null){
            paymentMethod = "No payment methods registered"
        }
        method= paymentMethod as String

        Log.i("valor2", "booleano: $bool")
        if (name.isEmpty()|| name.isBlank()|| name.isNullOrEmpty()||name.isDigitsOnly()) {
            txtInputName.setError("Enter a valid name")
            bool = false
        }
        else if(name.length>15){
            txtInputName.setError("Enter a shorter name for the transaction")
            bool = false
        }
        else{
            txtInputName.isErrorEnabled=false
        }

        if (value.isEmpty()||value.isNullOrEmpty()) {
            txtInputValue.setError("Enter a value")
            bool = false
        }
        else if(value.length>9){
            txtInputValue.setError("Enter a value with no more that 10 digits for the transaction")
            Toast.makeText(context,"Enter a value with no more that 9 digits for the transaction", Toast.LENGTH_LONG).show()
            bool = false
        }
        else if(value.toLong()<=0|| value.startsWith("0") ){
            txtInputValue.setError("Enter a value grater than zero")
            bool = false
        }
        else{
            txtInputValue.isErrorEnabled=false
        }
        if (method =="No payment methods registered") {
            Toast.makeText(context,"Register a payment method", Toast.LENGTH_LONG).show()
            bool = false
        }
        else if (method.isEmpty()||method.isNullOrEmpty()){
            Toast.makeText(context,"Enter a payment method", Toast.LENGTH_LONG).show()
            bool = false
        }
        Log.i("valor", "booleano: $bool")
        return bool
    }
}