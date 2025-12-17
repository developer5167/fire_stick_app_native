package com.hording.fire_stick_app_native.repository

import com.hording.fire_stick_app_native.models.ActivationRequest
import com.hording.fire_stick_app_native.models.ActivationResponse
import com.hording.fire_stick_app_native.netWork.ApiService
import javax.inject.Inject

class DeviceActivationRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun activateDevice(activationRequest: ActivationRequest): ActivationResponse {
        return apiService.activateDevice(activationRequest)
    }
}