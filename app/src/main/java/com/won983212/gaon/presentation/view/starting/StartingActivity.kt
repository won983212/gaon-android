package com.won983212.gaon.presentation.view.starting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.won983212.gaon.R
import com.won983212.gaon.presentation.view.code.CodeInputActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StartingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting)
        openCodeInputDialog()
    }

    private fun openCodeInputDialog() = lifecycleScope.launch {
        delay(2000)
        Intent(applicationContext, CodeInputActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }
}