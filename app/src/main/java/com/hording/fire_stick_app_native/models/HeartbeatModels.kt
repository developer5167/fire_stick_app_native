package com.hording.fire_stick_app_native.models

import com.google.gson.annotations.SerializedName

data class HeartbeatRequest(
    @SerializedName("device_id")
    val deviceId: String
)

data class HeartbeatResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("device_id")
    val deviceId: String,
    @SerializedName("last_seen")
    val lastSeen: String
)
