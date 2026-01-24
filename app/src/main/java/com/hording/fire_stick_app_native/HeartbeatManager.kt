package com.hording.fire_stick_app_native

import com.hording.fire_stick_app_native.models.HeartbeatRequest
import com.hording.fire_stick_app_native.repository.DeviceDetailsRepository
import com.hording.fire_stick_app_native.repository.FetchAdsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

class HeartbeatManager @Inject constructor(
    private val fetchAdsRepository: FetchAdsRepository,
    private val deviceDetailsRepository: DeviceDetailsRepository
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var isStarted = false

    fun startHeartbeat() {
        if (isStarted) return
        isStarted = true

        scope.launch {
            while (isActive) {
                try {
                    val deviceId = deviceDetailsRepository.deviceId.first()
                    if (deviceId.isNotEmpty()) {
                        val response = fetchAdsRepository.postHeartbeat(HeartbeatRequest(deviceId))
                        if (response.success) {
                            println("💓 Heartbeat sent successfully: ${response.lastSeen}")
                        } else {
                            println("⚠️ Heartbeat failed: ${response.message}")
                        }
                        delay(10000) // Send every 10 seconds
                    } else {
                        println("⏳ Heartbeat waiting for device activation...")
                        delay(5000) // Retry after 5 seconds if no device ID
                    }
                } catch (e: Exception) {
                    println("❌ Heartbeat error: ${e.message}. Retrying in 5s...")
                    delay(5000) // Retry after 5 seconds on error
                }
            }
        }
    }
}
