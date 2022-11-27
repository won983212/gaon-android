package com.won983212.gaon.presentation.view.voice

import android.content.Context
import android.util.Log
import org.webrtc.*
import org.webrtc.audio.AudioDeviceModule
import org.webrtc.audio.JavaAudioDeviceModule

class PeerConnectionUtils {
    private val mThreadChecker: ThreadUtils.ThreadChecker = ThreadUtils.ThreadChecker()
    private var mPeerConnectionFactory: PeerConnectionFactory? = null
    private var mAudioSource: AudioSource? = null

    // PeerConnection factory creation.
    private fun createPeerConnectionFactory(context: Context) {
        Log.d(TAG, "createPeerConnectionFactory()")
        mThreadChecker.checkIsOnValidThread()

        val builder = PeerConnectionFactory.builder()
        builder.setOptions(null)

        val adm = createJavaAudioDevice(context)
        mPeerConnectionFactory = builder
            .setAudioDeviceModule(adm)
            .createPeerConnectionFactory()
    }

    private fun createJavaAudioDevice(appContext: Context): AudioDeviceModule {
        Log.d(TAG, "createJavaAudioDevice()")
        mThreadChecker.checkIsOnValidThread()
        // Enable/disable OpenSL ES playback.
        // Set audio record error callbacks.
        val audioRecordErrorCallback: JavaAudioDeviceModule.AudioRecordErrorCallback =
            object : JavaAudioDeviceModule.AudioRecordErrorCallback {
                override fun onWebRtcAudioRecordInitError(errorMessage: String) {
                    Log.e(TAG, "onWebRtcAudioRecordInitError: $errorMessage")
                }

                override fun onWebRtcAudioRecordStartError(
                    errorCode: JavaAudioDeviceModule.AudioRecordStartErrorCode, errorMessage: String
                ) {
                    Log.e(TAG, "onWebRtcAudioRecordStartError: $errorCode. $errorMessage")
                }

                override fun onWebRtcAudioRecordError(errorMessage: String) {
                    Log.e(TAG, "onWebRtcAudioRecordError: $errorMessage")
                }
            }
        val audioTrackErrorCallback: JavaAudioDeviceModule.AudioTrackErrorCallback =
            object : JavaAudioDeviceModule.AudioTrackErrorCallback {
                override fun onWebRtcAudioTrackInitError(errorMessage: String) {
                    Log.e(TAG, "onWebRtcAudioTrackInitError: $errorMessage")
                }

                override fun onWebRtcAudioTrackStartError(
                    errorCode: JavaAudioDeviceModule.AudioTrackStartErrorCode, errorMessage: String
                ) {
                    Log.e(TAG, "onWebRtcAudioTrackStartError: $errorCode. $errorMessage")
                }

                override fun onWebRtcAudioTrackError(errorMessage: String) {
                    Log.e(TAG, "onWebRtcAudioTrackError: $errorMessage")
                }
            }
        return JavaAudioDeviceModule.builder(appContext)
            .setAudioRecordErrorCallback(audioRecordErrorCallback)
            .setAudioTrackErrorCallback(audioTrackErrorCallback)
            .createAudioDeviceModule()
    }

    // Audio source creation.
    private fun createAudioSource(context: Context) {
        Log.d(TAG, "createAudioSource()")
        mThreadChecker.checkIsOnValidThread()
        if (mPeerConnectionFactory == null) {
            createPeerConnectionFactory(context)
        }
        mAudioSource = mPeerConnectionFactory?.createAudioSource(MediaConstraints())
    }

    // Audio track creation.
    fun createAudioTrack(context: Context, id: String?): AudioTrack? {
        Log.d(TAG, "createAudioTrack()")
        mThreadChecker.checkIsOnValidThread()
        if (mAudioSource == null) {
            createAudioSource(context)
        }
        return mPeerConnectionFactory?.createAudioTrack(id, mAudioSource)
    }

    fun dispose() {
        Log.w(TAG, "dispose()")
        mThreadChecker.checkIsOnValidThread()
        if (mAudioSource != null) {
            mAudioSource!!.dispose()
            mAudioSource = null
        }
        if (mPeerConnectionFactory != null) {
            mPeerConnectionFactory!!.dispose()
            mPeerConnectionFactory = null
        }
    }

    companion object {
        private const val TAG = "PeerConnectionUtils"
    }
}