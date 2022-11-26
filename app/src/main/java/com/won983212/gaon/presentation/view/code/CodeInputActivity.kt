package com.won983212.gaon.presentation.view.code

import android.os.Bundle
import android.widget.RadioButton
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.won983212.gaon.databinding.ActivityCodeInputBinding

class CodeInputActivity : AppCompatActivity() {
    private val viewModel by viewModels<CodeInputViewModel>()

    private lateinit var binding: ActivityCodeInputBinding
    private lateinit var pwdIndicatorButtons: List<RadioButton>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCodeInputBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)

        pwdIndicatorButtons =
            binding.layoutPasswordIndicator.children.map { it as RadioButton }.toList()

        registerViewModelEvents()

        onBackPressedDispatcher.addCallback(this) {
            finish()
        }
    }

    private fun registerViewModelEvents() {
        viewModel.attachDefaultHandlers(this)

        viewModel.eventSuccess.observe(this) {
            finish()
        }

        viewModel.eventPwdIndicatorStateChanged.observe(this) { it ->
            val state = it.second
            if (it.first == CodeInputViewModel.INDICATOR_INDEX_ALL) {
                pwdIndicatorButtons.forEach { it.isChecked = state }
            } else {
                pwdIndicatorButtons[it.first].isChecked = state
            }
        }
    }
}