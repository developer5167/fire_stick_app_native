package com.hording.fire_stick_app_native.models

data class ActivationRequest(
    var activation_code:String,
    var email:String,
    var password:String,
)
