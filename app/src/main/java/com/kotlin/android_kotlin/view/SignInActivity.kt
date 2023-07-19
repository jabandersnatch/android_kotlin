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
import com.kotlin.android_kotlin.R
import com.kotlin.android_kotlin.view.utils.LoadingDialog
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        setup()
    }

    private fun setup(){
        //Loading
        val loading = LoadingDialog(this)

        //Info log in
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val emailText = findViewById<TextInputEditText>(R.id.emailTextInput)
        val passwordText = findViewById<TextInputEditText>(R.id.passwordTextInput)

        //info Sign in
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        btnSignUp.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        btnSignIn.setOnClickListener{
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

                val email= emailText.text.toString()
                val password= passwordText.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()){
                    loading.startLoading()
                    btnSignIn.isEnabled=false
                    //Start the coroutine to wait for the login result
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            val result = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
                            showHome()
                            loading.isDismiss()
                            delay(2000)
                            btnSignIn.isEnabled = true
                            finish()
                        } catch (e: Exception) {
                            btnSignIn.isEnabled = true
                            loading.isDismiss()
                            showAlert("Error","The username or password is incorrect")
                        }
                    }

                }else{showAlert("Error","Please fill in all the necessary parameters")}

            } else {showAlert("Wifi connection error","No internet access, please check your connection and try again")}
        }
    }
    private fun showAlert(title :String , message: String){
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