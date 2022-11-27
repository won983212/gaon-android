package com.won983212.gaon.data.repository

import com.won983212.gaon.data.model.ConnectionInfo
import com.won983212.gaon.data.model.SendTransportInfo

interface PairingRepository {
    suspend fun acceptInvite(code: Int): ConnectionInfo

    suspend fun createMobileSendTransport(roomId: Int, userId: Int): SendTransportInfo

    suspend fun connectTransport(roomId: Int, userId: Int, transportId: String, dtlsParameters: String)

    suspend fun sendTransport(roomId: Int, userId: Int, transportId: String, rtpParameters: String): String
}