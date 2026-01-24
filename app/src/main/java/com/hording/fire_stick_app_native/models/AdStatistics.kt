package com.hording.fire_stick_app_native.models

import com.google.gson.annotations.SerializedName

data class AdStatisticsRequest(
    @SerializedName("ad_id")
    val adId: String,
    @SerializedName("device_id")
    val deviceId: String,
    @SerializedName("location")
    val location: String? = null,
    @SerializedName("duration_played")
    val durationPlayed: Int? = 0
)

data class AdStatisticsResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("play_time")
    val playTime: String
)
