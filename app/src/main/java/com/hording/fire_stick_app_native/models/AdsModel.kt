package com.hording.fire_stick_app_native.models

data class AdsModel(
    val `data`: List<Data>,
    val message: String,
    val pagination: Pagination,
    val success: Boolean
)