package com.won983212.gaon.presentation.view.voice

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.won983212.gaon.data.model.ConnectionInfo
import com.won983212.gaon.data.model.SendTransportInfo
import com.won983212.gaon.data.repository.PairingRepository
import com.won983212.gaon.presentation.base.BaseViewModel
import com.won983212.gaon.presentation.util.asLiveData
import com.won983212.gaon.presentation.util.getParcelableExtraCompat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.mediasoup.droid.Device
import org.mediasoup.droid.Producer
import org.mediasoup.droid.SendTransport
import org.mediasoup.droid.Transport
import org.webrtc.AudioTrack
import javax.inject.Inject


@HiltViewModel
class VoiceViewModel @Inject constructor(
    private val pairingRepository: PairingRepository
) : BaseViewModel() {

    private val _channelName = MutableLiveData("Unknown")
    val channelName = _channelName.asLiveData()

    private val device = Device()
    private var connectionContext: ConnectionInfo? = null
    private var sendTransportInfo: SendTransportInfo? = null
    private var audioTrack: AudioTrack? = null
    private var producer: Producer? = null


    fun initializeByIntent(intent: Intent) {
        connectionContext =
            intent.getParcelableExtraCompat(VoiceActivity.EXTRA_CONNECTION_INFO)
        if (connectionContext != null) {
            _channelName.value = connectionContext!!.channelName
            createConnection(connectionContext!!)
        }
    }

    private fun createConnection(connection: ConnectionInfo) = viewModelScope.launch {
        device.load(connection.rtpCapabilities)
        sendTransportInfo = startProgressTask {
            pairingRepository.createMobileSendTransport(
                connection.roomId,
                connection.userId
            )
        }

        sendTransportInfo?.let {
            val mSendTransport = device.createSendTransport(
                sendTransportListener,
                it.transportId,
                it.iceParameters,
                it.iceCandidates,
                it.dtlsParameters
            )

            producer = mSendTransport.produce(
                { producer: Producer? -> Log.e("VoiceViewModel", "onTransportClose(), producer") },
                audioTrack, null, null, "{\"type\": \"Mobile\"}"
            )
        }
    }

    fun setAudioTrack(audioTrack: AudioTrack?) {
        this.audioTrack = audioTrack
    }

    private val sendTransportListener: SendTransport.Listener = object : SendTransport.Listener {
        private val listenerTAG: String = "SendTrans"

        override fun onProduce(
            transport: Transport, kind: String?, rtpParameters: String?, appData: String?
        ): String {
            val conn = connectionContext
            val transportInfo = sendTransportInfo

            if (conn != null && transportInfo != null && rtpParameters != null) {
                val producerId = runBlocking {
                    pairingRepository.sendTransport(
                        conn.roomId,
                        conn.userId,
                        transportInfo.transportId,
                        rtpParameters
                    )
                }

                Log.d(listenerTAG, "producerId: $producerId")
                return producerId
            }

            return ""
        }

        override fun onConnect(transport: Transport, dtlsParameters: String?) {
            val conn = connectionContext
            val transportInfo = sendTransportInfo

            if (conn != null && transportInfo != null && dtlsParameters != null) {
                runBlocking {
                    pairingRepository.connectTransport(
                        conn.roomId,
                        conn.userId,
                        transportInfo.transportId,
                        dtlsParameters
                    )
                }

                Log.d(listenerTAG, "onConnect")
            }
        }

        override fun onConnectionStateChange(transport: Transport?, connectionState: String) {
            Log.d(listenerTAG, "onConnectionStateChange: $connectionState")
        }
    }
}