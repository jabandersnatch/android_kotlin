package com.kotlin.android_kotlin.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.kotlin.android_kotlin.R
import com.kotlin.android_kotlin.view.utils.LoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        setup()
    }

    private fun setup (){
        //Loading
        val loading = LoadingDialog(this)

        //Info log in
        val btnSignIn = findViewById<Button>(R.id.btnSignUp)

        val usernameText = findViewById<TextInputEditText>(R.id.usernameTextInput)
        val emailText = findViewById<TextInputEditText>(R.id.emailTextInput)
        val passwordText = findViewById<TextInputEditText>(R.id.passwordTextInput)
        val passwordConfirmationText = findViewById<TextInputEditText>(R.id.passwordConfirmationTextInput)

        btnSignIn.setOnClickListener {


            var isConnected = false
            val sc = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val redInfo = sc.allNetworkInfo

            for (ir in redInfo) {
                if (ir.typeName.equals("WIFI", ignoreCase = true) || ir.typeName.equals("MOBILE", ignoreCase = true)) {
                    if (ir.isConnected) {
                        isConnected = true
                        break
                    }
                }
            }

            if (isConnected) {

                val username = usernameText.text.toString()
                val email = emailText.text.toString()
                val password = passwordText.text.toString()
                val passwordConfirmation = passwordConfirmationText.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty() && passwordConfirmation.isNotEmpty()) {

                    if (password == passwordConfirmation)
                    {
                        loading.startLoading()
                        btnSignIn.isEnabled=false
                        //Start the coroutine to wait for the login result
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                val result = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
                                val user = result.user
                                user?.let {
                                    val profileUpdates = UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build()
                                    user.updateProfile(profileUpdates)
                                }?.await()
                                showHome()
                                loading.isDismiss()
                                delay(2000)
                                btnSignIn.isEnabled = true
                                finish()
                            } catch (e: Exception) {
                                btnSignIn.isEnabled = true
                                showAlert("Error", "An error occurred authenticating the user")
                                loading.isDismiss()
                            }
                        }
                    }else{showAlert("Error", "Passwords do not match")}

                }else{showAlert("Error", "Please fill in all the necessary parameters")}

            } else {showAlert("Wifi connection error","No internet access, please check your connection and try again")}
        }
    }

    private fun showAlert(title:String , message: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Accept", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(){
        val homeIntent= Intent(this, MainActivity::class.java)
        startActivity(homeIntent)
    }
}