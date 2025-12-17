package com.hording.fire_stick_app_native.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hording.fire_stick_app_native.ApiResponse
import com.hording.fire_stick_app_native.Constants
import com.hording.fire_stick_app_native.SocketListenerStatus
import com.hording.fire_stick_app_native.WebSocketManager
import com.hording.fire_stick_app_native.models.AdsModel
import com.hording.fire_stick_app_native.models.MainAdsModel
import com.hording.fire_stick_app_native.repository.DeviceDetailsRepository
import com.hording.fire_stick_app_native.repository.FetchAdsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdsPlayViewModel @Inject constructor(
    private val fetchAdsRepository: FetchAdsRepository,
    private val deviceDetailsRepository: DeviceDetailsRepository,
    private val webSocketManager: WebSocketManager) : ViewModel() {
    private  val _currentStatus = MutableStateFlow<SocketListenerStatus>(SocketListenerStatus.ActiveMode)
    val socketStatus = _currentStatus
    init {
        connectSocket();
        observeStatusUpdates()
    }
    init {
        connectSocket()
        observeStatusUpdates()
    }

    private fun connectSocket() {
        viewModelScope.launch {
            val deviceId = deviceDetailsRepository.deviceId.first()
            val token = deviceDetailsRepository.token.first()
            webSocketManager.connect(
                Constants.BASE_URL_,
                deviceId,
                token
            )
        }
    }

    private fun observeStatusUpdates() {
        viewModelScope.launch {
            webSocketManager.statusFlow.collect { status ->
                println("DEVICE STATUS → $status")
                when(status){
                    "emergency-mode"->{
                        _currentStatus.value = SocketListenerStatus.EmergencyMode
                    }
                    "maintenance"->{
                        _currentStatus.value = SocketListenerStatus.MaintenanceMode
                    }
                    "offline"->{
                        _currentStatus.value = SocketListenerStatus.OfflineMode
                    }
                    "active"->{
                        _currentStatus.value = SocketListenerStatus.ActiveMode
                    }
                }
            }
        }
    }

    private val _mainAds = MutableStateFlow<ApiResponse<MainAdsModel>>(ApiResponse.None)
    val mainAds = _mainAds

    private val _companyAds = MutableStateFlow<ApiResponse<AdsModel>>(ApiResponse.None)
    val companyAds = _companyAds

    private val _emergencyAds = MutableStateFlow<ApiResponse<AdsModel>>(ApiResponse.None)
    val emergencyAds = _emergencyAds

    fun fetchMainAds() {
        viewModelScope.launch(Dispatchers.IO) {
            _mainAds.value = ApiResponse.Loading
            try {
                val deviceId =  deviceDetailsRepository.deviceId.first()
                _mainAds.value = ApiResponse.Success(fetchAdsRepository.fetchMainAds(deviceId))
            } catch (e: Exception) {
                _mainAds.value = ApiResponse.Error(message = e.message.toString())
            }
        }
    }
    fun fetchCompanyAds() {
        viewModelScope.launch(Dispatchers.IO) {
            _companyAds.value = ApiResponse.Loading
            try {
                _companyAds.value = ApiResponse.Success(fetchAdsRepository.fetchCompanyAds())
            } catch (e: Exception) {
                _companyAds.value = ApiResponse.Error(message = e.message.toString())
            }
        }
    }
   fun fetchEmergencyAds() {
        viewModelScope.launch(Dispatchers.IO) {
            _emergencyAds.value = ApiResponse.Loading
            try {
                val deviceId =  deviceDetailsRepository.deviceId.first()
                _emergencyAds.value = ApiResponse.Success(fetchAdsRepository.fetchEmergencyAds(deviceId))
            } catch (e: Exception) {
                _emergencyAds.value = ApiResponse.Error(message = e.message.toString())
            }
        }
    }
}