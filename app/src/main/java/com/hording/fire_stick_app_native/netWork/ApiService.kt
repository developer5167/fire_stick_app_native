package com.hording.fire_stick_app_native.netWork

import com.hording.fire_stick_app_native.models.ActivationRequest
import com.hording.fire_stick_app_native.models.ActivationResponse
import com.hording.fire_stick_app_native.models.AdsModel
import com.hording.fire_stick_app_native.models.DeviceStatusResponse
import com.hording.fire_stick_app_native.models.MainAdsModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
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

    @GET("/tvApp/emergency-ads/devices/{deviceId}")
    suspend fun fetchEmergencyAds(@Path("deviceId") deviceId: String): AdsModel

    @GET("/tvApp/device/status")
    suspend fun fetchStatus(@Query("device_id") deviceId: String): DeviceStatusResponse
}