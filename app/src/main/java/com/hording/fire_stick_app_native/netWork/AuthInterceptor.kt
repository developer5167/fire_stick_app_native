package com.hording.fire_stick_app_native.netWork

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.hording.fire_stick_app_native.repository.DeviceDetailsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val deviceDetailsRepository: DeviceDetailsRepository) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = deviceDetailsRepository.token
        val actualToken = runBlocking { token.first() }
        val newRequest = chain.request().newBuilder()
        if (actualToken.isNotEmpty()) {
            newRequest.addHeader("Authorization", "Bearer $actualToken")
        }
        return chain.proceed(newRequest.build())
    }
}