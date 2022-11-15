package com.won983212.gaon.presentation.util

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

internal fun TextView.setTextColorRes(@ColorRes color: Int) =
    setTextColor(context.getColorCompat(color))

internal fun <T> MutableLiveData<T>.asLiveData(): LiveData<T> = this

internal fun Context.toastShort(text: CharSequence) =
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

internal fun Context.toastShort(@StringRes textRes: Int) =
    Toast.makeText(this, textRes, Toast.LENGTH_SHORT).show()

internal fun Context.toastLong(text: CharSequence) =
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()

internal fun Context.toastLong(@StringRes textRes: Int) =
    Toast.makeText(this, textRes, Toast.LENGTH_LONG).show()