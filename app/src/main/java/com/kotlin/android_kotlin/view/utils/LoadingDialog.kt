package com.kotlin.android_kotlin.view.utils

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.kotlin.android_kotlin.R

class LoadingDialog (val activity:Activity){
    private lateinit var isdialog : AlertDialog

    fun startLoading(){
        val inflater = activity.layoutInflater
        val dialogView = inflater.inflate(R.layout.loading_item, null)
        val builder = androidx.appcompat.app.AlertDialog.Builder(activity)

        builder.setView(dialogView)
        builder.setCancelable(false)
        isdialog = builder.create()
        isdialog.show()
    }

    fun isDismiss(){
        isdialog.dismiss()
    }
}