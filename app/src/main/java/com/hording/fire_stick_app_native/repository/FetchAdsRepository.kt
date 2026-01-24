package com.hording.fire_stick_app_native.repository

import com.hording.fire_stick_app_native.models.AdStatisticsRequest
import com.hording.fire_stick_app_native.models.AdStatisticsResponse
import com.hording.fire_stick_app_native.models.AdsModel
import com.hording.fire_stick_app_native.models.DeviceStatusResponse
import com.hording.fire_stick_app_native.models.HeartbeatRequest
import com.hording.fire_stick_app_native.models.HeartbeatResponse
import com.hording.fire_stick_app_native.models.MainAdsModel
import com.hording.fire_stick_app_native.netWork.ApiService
import javax.inject.Inject

class FetchAdsRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun fetchMainAds(deviceId: String): MainAdsModel {
        return apiService.fetchMainAds(deviceId = deviceId)
    }

    suspend fun fetchCompanyAds(): AdsModel {
        return apiService.fetCompanyAds()
    }

    suspend fun fetchEmergencyAds(deviceId: String): AdsModel {
        return apiService.fetchEmergencyAds(deviceId)
    }

    suspend fun fetchStatus(deviceId: String): DeviceStatusResponse {
        return apiService.fetchStatus(deviceId)
    }

    suspend fun postAdStatistics(request: AdStatisticsRequest): AdStatisticsResponse {
        return apiService.postAdStatistics(request)
    }

    suspend fun postHeartbeat(request: HeartbeatRequest): HeartbeatResponse {
        return apiService.postHeartbeat(request)
    }
}