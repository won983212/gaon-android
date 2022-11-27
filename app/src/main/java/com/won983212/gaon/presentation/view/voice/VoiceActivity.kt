package com.won983212.gaon.presentation.view.voice

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.won983212.gaon.databinding.ActivityVoiceBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VoiceActivity : AppCompatActivity() {
    private val viewModel by viewModels<VoiceViewModel>()

    private lateinit var binding: ActivityVoiceBinding
    private val peerConnectionUtils = PeerConnectionUtils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVoiceBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)

        viewModel.setAudioTrack(peerConnectionUtils.createAudioTrack(this, "mic"))
        viewModel.initializeByIntent(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        peerConnectionUtils.dispose()
    }

    companion object {
        const val EXTRA_CONNECTION_INFO = "connectionInfo"
    }
}