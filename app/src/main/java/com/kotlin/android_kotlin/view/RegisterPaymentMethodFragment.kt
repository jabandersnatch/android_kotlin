package com.kotlin.android_kotlin.view

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import com.kotlin.android_kotlin.R
import com.kotlin.android_kotlin.data.paymentMethod.PaymentMethod
import com.kotlin.android_kotlin.data.transaction.Transaction
import com.kotlin.android_kotlin.domain.paymentMethod.PaymentMethodRepository
import com.kotlin.android_kotlin.viewmodel.paymentMethod.PaymentMethodViewModel
import com.kotlin.android_kotlin.viewmodel.paymentMethod.PaymentMethodViewModelFactory


class RegisterPaymentMethodFragment : Fragment() {

    private lateinit var txtInputName:TextInputLayout
    private lateinit var txtInputValue:TextInputLayout
    private lateinit var valueMovement:TextInputEditText
    private lateinit var nameMovement:TextInputEditText

    private lateinit var viewModel: PaymentMethodViewModel
    private var color=0

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

        return inflater.inflate(R.layout.fragment_register_payment_method, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Cambiar icon
        val btnCard= requireView().findViewById<ImageButton>(R.id.imageCard)
        val btnBank= requireView().findViewById<ImageButton>(R.id.imageBank)
        val btnCash= requireView().findViewById<ImageButton>(R.id.imageCoin)
        val btnPhone= requireView().findViewById<ImageButton>(R.id.imageCel)

        val colorPresionado = ContextCompat.getColor(requireContext(), R.color.colorPressed)

        var icon = "Cash"

        btnCard.backgroundTintList = ColorStateList.valueOf(color)
        btnBank.backgroundTintList = ColorStateList.valueOf(color)
        btnCash.backgroundTintList = ColorStateList.valueOf(colorPresionado)
        btnPhone.backgroundTintList = ColorStateList.valueOf(color)
        //Cambia el string de icon
        btnCard.setOnClickListener {
            icon = "Card"
            btnCard.backgroundTintList = ColorStateList.valueOf(colorPresionado)
            btnBank.backgroundTintList = ColorStateList.valueOf(color)
            btnCash.backgroundTintList = ColorStateList.valueOf(color)
            btnPhone.backgroundTintList = ColorStateList.valueOf(color)

        }
        btnBank.setOnClickListener {
            icon = "Bank"
            btnCard.backgroundTintList = ColorStateList.valueOf(color)
            btnBank.backgroundTintList = ColorStateList.valueOf(colorPresionado)
            btnCash.backgroundTintList = ColorStateList.valueOf(color)
            btnPhone.backgroundTintList = ColorStateList.valueOf(color)

        }
        btnCash.setOnClickListener {
            icon = "Cash"
            btnCard.backgroundTintList = ColorStateList.valueOf(color)
            btnBank.backgroundTintList = ColorStateList.valueOf(color)
            btnCash.backgroundTintList = ColorStateList.valueOf(colorPresionado)
            btnPhone.backgroundTintList = ColorStateList.valueOf(color)
        }
        btnPhone.setOnClickListener {
            icon = "Phone"
            btnCard.backgroundTintList = ColorStateList.valueOf(color)
            btnBank.backgroundTintList = ColorStateList.valueOf(color)
            btnCash.backgroundTintList = ColorStateList.valueOf(color)
            btnPhone.backgroundTintList = ColorStateList.valueOf(colorPresionado)
        }


        val userId = requireArguments().getString("userID")
        txtInputValue= requireView().findViewById<TextInputLayout>(R.id.txtInputValuePaymentMethod)
        txtInputName = requireView().findViewById<TextInputLayout>(R.id.txtInputNamePaymentMethod)
        nameMovement=  requireView().findViewById<TextInputEditText>(R.id.editTextNamePaymetMethod)
        valueMovement= requireView().findViewById<TextInputEditText>(R.id.editTextValuePaymentMethod)

        val btnRegisterPaymentMethod = requireView().findViewById<Button>(R.id.addPaymentMethod)
        btnRegisterPaymentMethod.setOnClickListener {
            if (validate()) {
                val name = nameMovement.text.toString()
                var value = valueMovement.text.toString().toDouble()
                // get the current date

                val paymentMethod =
                    PaymentMethod(icon,userId.toString(),name,value)


                viewModel.addPaymentMethod(paymentMethod)


                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Register successful")
                builder.setMessage("You registered you payment method successfully")
                builder.setPositiveButton("Accept", null)
                builder.setCancelable(false)
                val dialog: AlertDialog = builder.create()
                dialog.show()

                viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
                    if (isLoading == false) {

                        btnRegisterPaymentMethod.isEnabled = false

                        Handler().postDelayed({
                            btnRegisterPaymentMethod.isEnabled = true
                        }, 2000)

                        nameMovement.setText("")
                        txtInputName.clearFocus()
                        valueMovement.setText("")
                        txtInputValue.clearFocus()

                    }
                })
                btnRegisterPaymentMethod.isEnabled = false

                Handler().postDelayed({
                    btnRegisterPaymentMethod.isEnabled = true
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
        val btnCard= requireView().findViewById<ImageButton>(R.id.imageCard)
        val btnBank= requireView().findViewById<ImageButton>(R.id.imageBank)
        val btnCash= requireView().findViewById<ImageButton>(R.id.imageCoin)
        val btnPhone= requireView().findViewById<ImageButton>(R.id.imageCel)
        if (isDarkThemeOn()) {
            color= Color.rgb(74, 89, 101)
            btnCash?.setImageResource(R.drawable.coin_dark)

            btnBank?.setImageResource(R.drawable.bank_dark)
            btnBank?.backgroundTintList = ColorStateList.valueOf(color)

            btnCard?.setImageResource(R.drawable.card_dark)
            btnCard?.backgroundTintList = ColorStateList.valueOf(color)

            btnPhone?.setImageResource(R.drawable.cel_dark)
            btnPhone?.backgroundTintList = ColorStateList.valueOf(color)

        } else {
            color= Color.rgb(235, 242, 250)
            btnCash?.setImageResource(R.drawable.coin)

            btnBank?.setImageResource(R.drawable.bank)
            btnBank?.backgroundTintList = ColorStateList.valueOf(color)

            btnCard?.setImageResource(R.drawable.card)
            btnCard?.backgroundTintList = ColorStateList.valueOf(color)

            btnPhone?.setImageResource(R.drawable.cel)
            btnPhone?.backgroundTintList = ColorStateList.valueOf(color)
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
        name = nameMovement.text.toString()
        value = valueMovement.text.toString()
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
            //Toast.makeText(context,"Enter a value for the transaction", Toast.LENGTH_LONG).show()
            bool = false
        }
        else if(value.length>9){
            txtInputValue.setError("Enter a value with no more that 10 digits for the transaction")
            bool = false
        }
        else if(value.toLong()<=0|| value.startsWith("0") ){
            txtInputValue.setError("Enter a value grater than zero")
            bool = false
        }
        else{
            txtInputValue.isErrorEnabled=false
        }
        Log.i("valor", "booleano: $bool")
        return bool
    }
}