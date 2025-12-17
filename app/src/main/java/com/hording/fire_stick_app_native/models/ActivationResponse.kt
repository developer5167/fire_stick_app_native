package com.hording.fire_stick_app_native.models

data class ActivationResponse(
    val success: Boolean,
    val message: String,
    val device: Device,
    val token: String,
    val baseUrl: String
)

data class Device(
    val id: String,
    val status: String,
    val isAssigned: Boolean,
    val assignedTo: String,
)
