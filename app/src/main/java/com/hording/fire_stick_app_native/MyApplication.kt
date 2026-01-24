package com.hording.fire_stick_app_native

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {

    @Inject
    lateinit var heartbeatManager: HeartbeatManager

    override fun onCreate() {
        super.onCreate()
        heartbeatManager.startHeartbeat()
    }
}
