package com.hording.fire_stick_app_native.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.hording.fire_stick_app_native.ApiResponse
import com.hording.fire_stick_app_native.models.AdsModel

@Composable
fun EmergencyAdsHandler(emergencyAds: State<ApiResponse<AdsModel>>) {
    when (val state = emergencyAds.value) {

        is ApiResponse.Success -> {
            if (state.data.data.isNotEmpty()) {
                PlayCompanyAds(
                    ads = state.data.data,
                    onLoopCompleted = {}
                )
            } else {
                StatusTextScreen("No emergency ads found")
            }
        }

        is ApiResponse.Loading -> {
            LoadingScreen()
        }

        else -> {
            StatusTextScreen("No emergency ads available")
        }
    }
}
