package com.won983212.gaon.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class ConnectionInfo(
    val channelName: String,
    val rtpCapabilities: String,
    val roomId: Int,
    val userId: Int
) : Parcelable {
    companion object {
        fun fromJson(jsonText: JSONObject): ConnectionInfo {
            return ConnectionInfo(
                jsonText.getString("channelName"),
                jsonText.getString("rtpCapabilities"),
                jsonText.getInt("roomId"),
                jsonText.getInt("userId")
            )
        }
    }
}