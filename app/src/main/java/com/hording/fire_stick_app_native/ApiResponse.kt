package com.hording.fire_stick_app_native

sealed class ApiResponse<out T> {
    object None : ApiResponse<Nothing>()
    object Loading : ApiResponse<Nothing>()
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Error<T>(val message: String) : ApiResponse<T>()
}

sealed class SocketListenerStatus {
    object MaintenanceMode : SocketListenerStatus()
    object EmergencyMode : SocketListenerStatus()
    object OfflineMode : SocketListenerStatus()
    object ActiveMode : SocketListenerStatus()
}