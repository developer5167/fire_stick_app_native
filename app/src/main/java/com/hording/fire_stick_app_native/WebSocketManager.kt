package com.hording.fire_stick_app_native

import androidx.lifecycle.viewmodel.compose.viewModel
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

class WebSocketManager @Inject constructor(private  val fetchAdsRepository: FetchAdsRepository,private val deviceDetailsRepository: DeviceDetailsRepository) {

    private lateinit var socket: Socket
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _statusFlow = MutableStateFlow("active")
    val statusFlow = _statusFlow.asStateFlow()


    init {
        fetchInitialStatus()
    }
    fun fetchInitialStatus(){
        scope.launch {
            val device = deviceDetailsRepository.deviceId.first()
            val status = fetchAdsRepository.fetchStatus(device)
            _statusFlow.value = status.status
        }
    }

    fun connect(url: String, deviceId: String,token: String) {
        val opts = IO.Options()
        opts.query = "token=$token"
        socket = IO.socket(url,opts)

        socket.on(Socket.EVENT_CONNECT) {
            println("🟢 Socket.IO connected")
            socket.emit("join_device", deviceId)
        }

        socket.on("device_status") { args ->
            try {
                val data = args[0] as JSONObject
                val status = data.getString("status")

                println("📥 DEVICE STATUS → $status")

                scope.launch {
                    _statusFlow.emit(status)
                }
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
