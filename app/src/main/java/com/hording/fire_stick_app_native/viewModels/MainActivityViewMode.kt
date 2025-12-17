package com.hording.fire_stick_app_native.viewModels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hording.fire_stick_app_native.ApiResponse
import com.hording.fire_stick_app_native.models.ActivationRequest
import com.hording.fire_stick_app_native.models.ActivationResponse
import com.hording.fire_stick_app_native.repository.DeviceActivationRepository
import com.hording.fire_stick_app_native.repository.DeviceDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewMode @Inject constructor(
    private val deviceActivationRepository: DeviceActivationRepository,
    private val deviceDetailsRepository: DeviceDetailsRepository,
) :
    ViewModel() {
    var activationRequest: ActivationRequest = ActivationRequest("", "", "")
    private val _activationResponseMutableStateFlow =
        MutableStateFlow<ApiResponse<ActivationResponse>>(
            ApiResponse.None)
    val activationResponse = _activationResponseMutableStateFlow

    val token = deviceDetailsRepository.token.asLiveData()
    val id = deviceDetailsRepository.deviceId.asLiveData()

    fun activateDevice() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(ContentValues.TAG, "activateDevice: $activationRequest")
            _activationResponseMutableStateFlow.value = ApiResponse.Loading
            try {
                val result = deviceActivationRepository.activateDevice(activationRequest)
                _activationResponseMutableStateFlow.value = ApiResponse.Success(result)
                deviceDetailsRepository.saveAssignedTo(ApiResponse.Success(result).data.device.assignedTo)
                deviceDetailsRepository.saveBaseUrl(ApiResponse.Success(result).data.baseUrl)
                deviceDetailsRepository.saveDeviceID(ApiResponse.Success(result).data.device.id)
                deviceDetailsRepository.saveIsAssigned(ApiResponse.Success(result).data.device.isAssigned)
                deviceDetailsRepository.saveToken(ApiResponse.Success(result).data.token)
            } catch (e: Exception) {
                Log.d(ContentValues.TAG, "activateDevice: $e")
                _activationResponseMutableStateFlow.value = ApiResponse.Error(e.message.toString())
            }

        }
    }
}