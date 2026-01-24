package com.hording.fire_stick_app_native.netWork

import com.hording.fire_stick_app_native.models.ActivationRequest
import com.hording.fire_stick_app_native.models.ActivationResponse
import com.hording.fire_stick_app_native.models.AdStatisticsRequest
import com.hording.fire_stick_app_native.models.AdStatisticsResponse
import com.hording.fire_stick_app_native.models.AdsModel
import com.hording.fire_stick_app_native.models.DeviceStatusResponse
import com.hording.fire_stick_app_native.models.HeartbeatRequest
import com.hording.fire_stick_app_native.models.HeartbeatResponse
import com.hording.fire_stick_app_native.models.MainAdsModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("/tvApp/devices/activate")
    suspend fun activateDevice(@Body activationRequest: ActivationRequest): ActivationResponse

    @GET("/tvApp/ads")
    suspend fun fetchMainAds(@Query("device_id") deviceId: String): MainAdsModel

    @GET("/tvApp/company-ads")
    suspend fun fetCompanyAds(): AdsModel

    @GET("admin/emergency-ads/devices/{deviceId}")
    suspend fun fetchEmergencyAds(@Path("deviceId") deviceId: String): AdsModel

    @GET("/tvApp/device/status")
    suspend fun fetchStatus(@Query("device_id") deviceId: String): DeviceStatusResponse

    @POST("/ad-statistics")
    suspend fun postAdStatistics(@Body adStatisticsRequest: AdStatisticsRequest): AdStatisticsResponse

    @POST("/device/heartbeat")
    suspend fun postHeartbeat(@Body heartbeatRequest: HeartbeatRequest): HeartbeatResponse
}