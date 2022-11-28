package com.won983212.gaon.data.source.socket

import android.util.Log
import com.won983212.gaon.BuildConfig
import com.won983212.gaon.data.model.ConnectionInfo
import com.won983212.gaon.data.model.SendTransportInfo
import com.won983212.gaon.exception.RequestCallError
import io.socket.client.AckWithTimeout
import io.socket.client.IO
import org.json.JSONObject
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class SocketPairingDataSource @Inject constructor() {
    private val mSocket = IO.socket(BuildConfig.SOCKET_PATH)

    init {
        mSocket.connect()
    }

    private suspend fun <T> emit(
        event: String,
        vararg params: Any
    ) = suspendCoroutine {
        Log.d("SocketCall", "Call $event ${params.contentToString()}")

        mSocket.emit(event, params, object : AckWithTimeout(5000) {
            override fun onSuccess(vararg args: Any?) {
                if (args[0] is JSONObject) {
                    val response = args[0] as JSONObject
                    Log.d("SocketCall", "$event Response: $response")
                    if (response.has("error")) {
                        val errorObj = response.get("error") as JSONObject
                        val message = if (errorObj.has("message")) {
                            errorObj.getString("message")
                        } else {
                            "No message"
                        }
                        it.resumeWithException(RequestCallError(message))
                    } else {
                        it.resume(response as T)
                    }
                } else {
                    Log.d("SocketCall", "$event Response: ${args[0]}")
                    it.resume(args[0] as T)
                }
            }

            override fun onTimeout() {
                Log.d("SocketCall", "Timeout $event")
                val exception = TimeoutException()
                exception.printStackTrace()
                it.resumeWithException(exception)
            }
        })
    }

    suspend fun acceptInvite(code: Int): ConnectionInfo {
        return ConnectionInfo.fromJson(
            emit(
                "acceptInvite",
                code
            )
        )
    }

    suspend fun createMobileSendTransport(roomId: Int, userId: Int): SendTransportInfo {
        return SendTransportInfo.fromJson(
            emit(
                "createMobileSendTransport",
                roomId,
                userId
            )
        )
    }

    suspend fun connectTransport(
        roomId: Int,
        userId: Int,
        transportId: String,
        dtlsParameters: String
    ): Boolean {
        return emit(
            "connectTransport",
            roomId,
            userId,
            transportId,
            dtlsParameters,
            ""
        )
    }

    suspend fun sendTransport(
        roomId: Int,
        userId: Int,
        transportId: String,
        rtpParameters: String
    ): String = emit(
        "sendTransport",
        roomId,
        userId,
        transportId,
        true,
        "Mobile",
        "audio",
        rtpParameters,
        ""
    )
}