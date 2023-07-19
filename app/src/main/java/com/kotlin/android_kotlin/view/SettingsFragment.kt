package com.kotlin.android_kotlin.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.Window
import com.kotlin.android_kotlin.R
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.text.isDigitsOnly
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.auth.FirebaseAuth
import com.kotlin.android_kotlin.controller.AccessDB_Controller
import java.text.SimpleDateFormat
import java.util.*

class SettingsFragment : Fragment(R.layout.fragment_settings){

    //Variables para la fecha
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    private val accessDB_Controller = AccessDB_Controller()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as MainActivity

        val username = requireArguments().getString("username")
        Log.i("que?", username.toString())
        val email = requireArguments().getString("email")

        val emailTextView = requireView().findViewById<TextView>(R.id.textViewEmail)
        emailTextView.text=email.toString()

        val nameTextView = view.findViewById<TextView>(R.id.textViewName)
        nameTextView.text = username.toString()

        //val btnChangePassword= requireView().findViewById<Button>(R.id.btnChangePassword)
        val btnDebtNoti= requireView().findViewById<Button>(R.id.btnDebtNoti)
        val btnSavingNoti= requireView().findViewById<Button>(R.id.btnSavingNoti)
        val btnSignOff= requireView().findViewById<Button>(R.id.btnSignOff)

        //btnChangePassword.setOnClickListener {
            //TODO
            //Log.i("ChangePassword", "Se oprimio el boton brrr")
        //}

        btnDebtNoti.setOnClickListener {
            showDebtDialog()
        }

        btnSavingNoti.setOnClickListener {
            showSavingDialog()
        }

        btnSignOff.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val signInIntent = Intent(context, SignInActivity::class.java)
            activity.onSingOut()
            startActivity(signInIntent)
        }

        //*********switch theme

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val savedSwitchValue = sharedPreferences.getBoolean("switch_value_key", true)

        val switch = view.findViewById<Switch>(R.id.switchMode)
        switch.isChecked = savedSwitchValue

        switch.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("switch_value_key", isChecked)
            editor.apply()

            val intent = Intent("com.example.ACTION_THEME_CHANGED")
            intent.putExtra("switch_value", isChecked)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val btn= requireView().findViewById<Button>(R.id.showAllPaymentMethods)
        btn.setOnClickListener {
            val activity = requireActivity() as MainActivity
            activity.setFragmentInContainer(R.id.showAllPaymentMethodsFragment, requireArguments())
        }


    }

    private fun showDebtDialog(){
        val dialog=Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.debt_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val nameText = dialog.findViewById<EditText>(R.id.Name)
        val valueText = dialog.findViewById<EditText>(R.id.Value)
        val dateText  = dialog.findViewById<TextView>(R.id.dateText)

        var date = ""

        val btnDate = dialog.findViewById<Button>(R.id.btnDate)
        btnDate.setOnClickListener{
            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, myear, mmonth, mday ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, myear)
                calendar.set(Calendar.MONTH, mmonth)
                calendar.set(Calendar.DAY_OF_MONTH, mday)

                val tpd = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)

                    val dateFormat = SimpleDateFormat("dd/MM/yy HH:mm", Locale.ENGLISH)
                    val formattedDate = dateFormat.format(calendar.time)

                    date = formattedDate
                    dateText.text = "Selected date: $formattedDate"
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)

                tpd.show()

            }, year, month, day)

            dpd.show()
        }

        val btnSubmit = dialog.findViewById<Button>(R.id.btnSubmit)
        btnSubmit.setOnClickListener{

            val name = nameText.text.toString()
            var value = valueText.text.toString()
            val userId = requireArguments().getString("userID")

            if ((name.isEmpty()|| name.isBlank()|| name.isNullOrEmpty()||name.isDigitsOnly())&&(value.isEmpty()||value.isNullOrEmpty())&&date=="") {

                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Error")
                builder.setMessage("Please fill in all the necessary parameters")
                builder.setPositiveButton("Accept", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()

            } else {
                accessDB_Controller.callRegisterDebtNotification(requireContext(),userId.toString(),name,value.toDouble(),date)
                dialog.dismiss()
            }
        }
        dialog.show()



    }

    private fun showSavingDialog(){
        val dialog=Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.saving_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnFinish = dialog.findViewById<Button>(R.id.btnFinish)
        btnFinish.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }



}