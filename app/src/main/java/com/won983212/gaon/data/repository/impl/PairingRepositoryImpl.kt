package com.won983212.gaon.data.repository.impl

import com.won983212.gaon.data.model.ConnectionInfo
import com.won983212.gaon.data.model.SendTransportInfo
import com.won983212.gaon.data.repository.PairingRepository
import com.won983212.gaon.data.source.socket.SocketPairingDataSource

internal class PairingRepositoryImpl(
    private val socketPairingDataSource: SocketPairingDataSource
) : PairingRepository {
    override suspend fun acceptInvite(code: Int): ConnectionInfo {
        return socketPairingDataSource.acceptInvite(code)
    }

    override suspend fun createMobileSendTransport(roomId: Int, userId: Int): SendTransportInfo {
        return socketPairingDataSource.createMobileSendTransport(roomId, userId)
    }

    override suspend fun connectTransport(
        roomId: Int,
        userId: Int,
        transportId: String,
        dtlsParameters: String
    ) {
        socketPairingDataSource.connectTransport(roomId, userId, transportId, dtlsParameters)
    }

    override suspend fun sendTransport(
        roomId: Int,
        userId: Int,
        transportId: String,
        rtpParameters: String
    ): String {
        return socketPairingDataSource.sendTransport(roomId, userId, transportId, rtpParameters)
    }
}