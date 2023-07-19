package com.kotlin.android_kotlin.view

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.kotlin.android_kotlin.R
import com.kotlin.android_kotlin.databinding.ActivityMainBinding
import kotlinx.coroutines.*

enum class ProviderType{
    BASIC
}

class MainActivity : ClassPadre(), SensorEventListener {

    lateinit var navController: NavController
    private var lightSensor: Sensor? = null
    private var sensorManager: SensorManager? = null
    var buttonMessage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)
        if (lightSensor == null) {
            Log.d("tag", "El dispositivo no tiene un sensor de luz disponible")
        } else {
            sensorManager?.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        //Deleting the infinite loop

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            //Se obtiene la info del login
            val username : String? = user.displayName
            val email: String? = user.email.toString()
            val provider: String? = ProviderType.BASIC.toString()
            val userID: String? = user.uid

            val infoLogin = Bundle()
            infoLogin.putString("username", username)
            infoLogin.putString("email", email)
            infoLogin.putString("provider", provider)
            infoLogin.putString("userID", userID)

            navController = findNavController(R.id.fragmentContainerView)
            val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            navController.navigate(R.id.homeFragment, infoLogin)

            val bottomSheetFragment = BottomSheetFragment()

            bottomNavigation.setOnItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.homeFragment -> {
                        navController.navigate(R.id.homeFragment, infoLogin)
                        true
                    }
                    R.id.savingTipsFragment -> {
                        navController.navigate(R.id.savingTipsFragment)
                        true
                    }
                    R.id.registerMovementFragment -> {

                        var actualFragment = ""
                        lifecycleScope.launch {

                            val deferred = CompletableDeferred<Unit>()

                            bottomSheetFragment.setDismissListener(object :
                                BottomSheetFragment.BottomSheetDismissListener {
                                override fun onBottomSheetDismissed() {
                                    deferred.complete(Unit)
                                }
                            })

                            bottomSheetFragment.show(supportFragmentManager, "BottomSheetDialog")

                            lifecycleScope.launch {
                                deferred.await()

                                actualFragment = bottomSheetFragment.getActualFragment()

                                if (actualFragment=="RegisterNewMovement"){
                                    navController.navigate(R.id.registerMovementFragment, infoLogin)
                                    bottomSheetFragment.setActualFragment("")
                                }
                                else if (actualFragment=="AddNewDebt"){
                                    navController.navigate(R.id.registerDebtFragment, infoLogin)
                                    bottomSheetFragment.setActualFragment("")
                                }
                                else if (actualFragment=="AddPaymentMethod"){
                                    navController.navigate(R.id.registerPaymentMethodFragment, infoLogin)
                                    bottomSheetFragment.setActualFragment("")
                                }
                                else{
                                    bottomSheetFragment.setActualFragment("")
                                }

                            }
                        }
                        true
                    }
                    R.id.historicalFragment -> {
                        navController.navigate(R.id.historicalFragment, infoLogin)
                        true
                    }
                    R.id.settingsFragment -> {
                        navController.navigate(R.id.settingsFragment, infoLogin)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Elimina el registro del SensorEventListener
        sensorManager?.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isDarkMode = sharedPreferences.getBoolean("switch_value_key", false)

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // hacer algo en respuesta a un cambio en la precisión del sensor
    }
    override fun onSensorChanged(event: SensorEvent) {

        val casButton = findViewById<ImageButton>(R.id.CASButton)
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val lightValue = event.values[0]
            if (lightValue < 1000 && !isDarkThemeOn()) {

                casButton.setColorFilter(Color.rgb(0, 117, 162))
                casButton.visibility = View.VISIBLE
                casButton.setOnClickListener {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Lightning change")
                    builder.setMessage("We detected a change of you environment lightning, would you like to switch to dark mode app theme?")
                    builder.setPositiveButton("Accept") { dialog, which ->

                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    builder.setNegativeButton("Cancel", null)
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }

            }

            else if(lightValue > 1000 && isDarkThemeOn()){
                casButton.setColorFilter(Color.rgb(234, 241, 249))
                casButton.visibility= View.VISIBLE
                casButton.setOnClickListener{
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Lightning change")
                    builder.setMessage("We detected a change of you environment lightning, would you like to switch to light mode app theme?")
                    builder.setPositiveButton("Accept") { dialog, which ->

                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    builder.setNegativeButton("Cancel",null)
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }

            }
            else{
                casButton.visibility = View.INVISIBLE
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    fun onSingOut() {
        finish()
    }

    fun setFragmentInContainer(fragment: Int, arguments: Bundle) {
        navController.navigate(fragment, arguments)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val menuItem = when (fragment) {
            R.id.homeFragment -> bottomNavigation.menu.findItem(R.id.homeFragment)
            R.id.showAllDebtsFragment ->bottomNavigation.menu.findItem(R.id.homeFragment)
            R.id.savingTipsFragment -> bottomNavigation.menu.findItem(R.id.savingTipsFragment)
            R.id.registerMovementFragment -> bottomNavigation.menu.findItem(R.id.registerMovementFragment)
            R.id.historicalFragment -> bottomNavigation.menu.findItem(R.id.historicalFragment)
            R.id.settingsFragment -> bottomNavigation.menu.findItem(R.id.settingsFragment)
            R.id.showAllPaymentMethodsFragment -> bottomNavigation.menu.findItem(R.id.settingsFragment)
            else -> null
        }
        menuItem?.isChecked = true
    }

    public fun isDarkThemeOn(): Boolean {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}
