package com.won983212.gaon

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.mediasoup.droid.MediasoupClient

@HiltAndroidApp
class GaonMobileMic : Application() {
    override fun onCreate() {
        super.onCreate()

        MediasoupClient.initialize(this)
    }
}