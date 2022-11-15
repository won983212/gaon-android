package com.won983212.gaon.presentation.view.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.won983212.gaon.databinding.DialogLoadingBinding

class LoadingDialog(context: Context) : GaonDialog(context) {
    override fun open(): AlertDialog {
        val layout = DialogLoadingBinding.inflate(
            LayoutInflater.from(context),
            null, false
        )
        return openDialog(layout.root)
    }
}