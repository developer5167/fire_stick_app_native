package com.hording.fire_stick_app_native.composables

import androidx.compose.runtime.Composable
import com.hording.fire_stick_app_native.ApiResponse
import com.hording.fire_stick_app_native.models.AdsModel
import com.hording.fire_stick_app_native.models.Data
import com.hording.fire_stick_app_native.models.MainAdsModel

@Composable
fun ActiveAdsHandler(
    mainAdsState: ApiResponse<MainAdsModel>,
    companyAdsState: ApiResponse<AdsModel>,
    onFetchMainAds: () -> Unit,
    onAdPlayed: (Data, Boolean) -> Unit // Changed to include isMainAd boolean
) {
    when {
        mainAdsState is ApiResponse.Success &&
                mainAdsState.data.ads.isNotEmpty() -> {

            PlayMainAds(
                mainAds = mainAdsState.data.ads,
                companyAds = if (companyAdsState is ApiResponse.Success)
                    companyAdsState.data.data else emptyList(),
                onLoopCompleted = onFetchMainAds,
                onAdPlayed = onAdPlayed
            )
        }

        companyAdsState is ApiResponse.Success &&
                companyAdsState.data.data.isNotEmpty() -> {

            PlayCompanyAds(
                ads = companyAdsState.data.data,
                onLoopCompleted = onFetchMainAds,
                onAdPlayed = { ad,value -> onAdPlayed(ad, false) } // Always false for company ads
            )
        }

        else -> NoAdsFoundScreen()
    }
}
