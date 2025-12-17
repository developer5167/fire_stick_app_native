package com.hording.fire_stick_app_native.composables

import androidx.compose.runtime.Composable
import com.hording.fire_stick_app_native.ApiResponse
import com.hording.fire_stick_app_native.models.AdsModel
import com.hording.fire_stick_app_native.models.MainAdsModel

@Composable
fun ActiveAdsHandler(
    mainAdsState: ApiResponse<MainAdsModel>,
    companyAdsState: ApiResponse<AdsModel>,
    onFetchMainAds: () -> Unit
) {
    when {
        mainAdsState is ApiResponse.Success &&
                mainAdsState.data.ads.isNotEmpty() -> {

            PlayMainAds(
                mainAds = mainAdsState.data.ads,
                companyAds = if (companyAdsState is ApiResponse.Success)
                    companyAdsState.data.data else emptyList(),
                onLoopCompleted = onFetchMainAds
            )
        }

        companyAdsState is ApiResponse.Success &&
                companyAdsState.data.data.isNotEmpty() -> {

            PlayCompanyAds(
                ads = companyAdsState.data.data,
                onLoopCompleted = onFetchMainAds
            )
        }

        else -> NoAdsFoundScreen()
    }
}
