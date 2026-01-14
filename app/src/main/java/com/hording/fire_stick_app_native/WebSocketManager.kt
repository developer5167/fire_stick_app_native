package com.hording.fire_stick_app_native

import com.hording.fire_stick_app_native.repository.DeviceDetailsRepository
import com.hording.fire_stick_app_native.repository.FetchAdsRepository
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

class WebSocketManager @Inject constructor(
    private val fetchAdsRepository: FetchAdsRepository,
    private val deviceDetailsRepository: DeviceDetailsRepository
) {

    private lateinit var socket: Socket
    private val scope = CoroutineScope(Dispatchers.IO)

    // Initialized to "loading" to allow UI to differentiate between initial state and fetched state
    private val _statusFlow = MutableStateFlow("loading")
    val statusFlow = _statusFlow.asStateFlow()

    init {
        fetchInitialStatus()
    }

    fun fetchInitialStatus() {
        scope.launch {
            try {
                val deviceId = deviceDetailsRepository.deviceId.first()
                if (deviceId.isNotEmpty()) {
                    val status = fetchAdsRepository.fetchStatus(deviceId)
                    println("📥 INITIAL STATUS FETCHED → ${status.status}")
                    _statusFlow.value = status.status
                } else {
                    println("⚠️ DEVICE ID IS EMPTY, SKIPPING INITIAL STATUS FETCH")
                }
            } catch (e: Exception) {
                println("❌ INITIAL STATUS FETCH ERROR → ${e.message}")
            }
        }
    }

    fun connect(url: String, deviceId: String, token: String) {
        val opts = IO.Options()
        opts.query = "token=$token"
        socket = IO.socket(url, opts)

        socket.on(Socket.EVENT_CONNECT) {
            println("🟢 Socket.IO connected")
            socket.emit("join_device", deviceId)
        }

        socket.on("device_status") { args ->
            try {
                val data = args[0] as JSONObject
                val status = data.getString("status")

                println("📥 DEVICE STATUS FROM SOCKET → $status")

                _statusFlow.value = status
            } catch (e: Exception) {
                println("❌ SOCKET PARSE ERROR → ${e.message}")
            }
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            println("🔴 Socket.IO disconnected")
        }

        socket.on(Socket.EVENT_CONNECT_ERROR) {
            println("❌ Socket.IO connection error")
        }

        socket.connect()
    }

    fun disconnect() {
        if (::socket.isInitialized) {
            socket.disconnect()
            socket.off()
        }
    }
}
