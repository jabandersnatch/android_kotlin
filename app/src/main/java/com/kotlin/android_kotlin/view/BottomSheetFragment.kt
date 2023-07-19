package com.kotlin.android_kotlin.view

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kotlin.android_kotlin.R

class BottomSheetFragment: BottomSheetDialogFragment() {

    interface BottomSheetDismissListener {
        fun onBottomSheetDismissed()
    }
    private var dismissListener: BottomSheetDismissListener? = null
    private var actualFragment:String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnRegisterNewMovement = view.findViewById<Button>(R.id.btnRegisterNewMovement)
        val btnAddNewDebt = view.findViewById<Button>(R.id.btnAddNewDebt)
        val btnAddPaymentMethod = view.findViewById<Button>(R.id.btnAddPaymentMethod)

        btnRegisterNewMovement.setOnClickListener{
            actualFragment = "RegisterNewMovement"
            dismiss()
        }

        btnAddNewDebt.setOnClickListener{
            actualFragment = "AddNewDebt"
            dismiss()
        }

        btnAddPaymentMethod.setOnClickListener{
            actualFragment = "AddPaymentMethod"
            dismiss()
        }
    }

    fun setDismissListener(listener: BottomSheetDismissListener) {
        dismissListener = listener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.onBottomSheetDismissed()
    }

    fun getActualFragment(): String {
        return actualFragment
    }
    fun setActualFragment(fragment: String) {
        actualFragment = fragment
    }
}