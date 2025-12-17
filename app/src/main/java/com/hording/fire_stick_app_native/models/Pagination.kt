package com.hording.fire_stick_app_native.models

data class Pagination(
    val limit: Int,
    val page: Int,
    val total: Int,
    val totalPages: Int
)