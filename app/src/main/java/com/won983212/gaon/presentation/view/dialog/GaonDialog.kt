package com.won983212.gaon.presentation.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.app.AlertDialog

abstract class GaonDialog(protected val context: Context) {

    private var onCancelledListener: OnDialogCancelled? = null

    abstract fun open(): AlertDialog

    protected fun openDialog(view: View): AlertDialog {
        val builder = AlertDialog.Builder(context)
            .setView(view)
            .setCancelable(false)

        if (onCancelledListener != null) {
            builder.setOnCancelListener { onCancelledListener?.onCancelled() }
        }

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        return dialog
    }

    fun setOnCancelledListener(listener: OnDialogCancelled): GaonDialog {
        onCancelledListener = listener
        return this
    }

    fun interface OnDialogSubmit<T> {
        fun onSubmit(data: T)
    }

    fun interface OnDialogCancelled {
        fun onCancelled()
    }
}