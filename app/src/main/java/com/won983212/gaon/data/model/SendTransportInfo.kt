package com.won983212.gaon.data.model

import org.json.JSONObject

data class SendTransportInfo(
    val transportId: String,
    val iceParameters: String,
    val iceCandidates: String,
    val dtlsParameters: String
) {
    companion object {
        fun fromJson(jsonText: JSONObject): SendTransportInfo {
            return SendTransportInfo(
                jsonText.getString("transportId"),
                jsonText.getString("iceParameters"),
                jsonText.getString("iceCandidates"),
                jsonText.getString("dtlsParameters")
            )
        }
    }
}