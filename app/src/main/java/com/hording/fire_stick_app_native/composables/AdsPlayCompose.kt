package com.hording.fire_stick_app_native.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hording.fire_stick_app_native.SocketListenerStatus
import com.hording.fire_stick_app_native.viewModels.AdsPlayViewModel

@Composable
fun AdsPlayCompose(adsPlayViewModel: AdsPlayViewModel = hiltViewModel()) {
    val mainAdsState = adsPlayViewModel.mainAds.collectAsState()
    val companyAdsState = adsPlayViewModel.companyAds.collectAsState()
    val emergencyAds = adsPlayViewModel.emergencyAds.collectAsState()
    val status = adsPlayViewModel.socketStatus.collectAsState()

    println("STATE>>>:  ${status.value}")




    LaunchedEffect(Unit) {
        adsPlayViewModel.fetchMainAds()
        adsPlayViewModel.fetchCompanyAds()
        adsPlayViewModel.fetchEmergencyAds()
    }
        when (status.value) {
            SocketListenerStatus.EmergencyMode -> {
                println("STATE>>>:  ${status.value}  ${emergencyAds.value}")
                EmergencyAdsHandler(emergencyAds)
            }
            SocketListenerStatus.MaintenanceMode -> {
                StatusTextScreen("Device under maintenance")
            }
            SocketListenerStatus.OfflineMode -> {
                StatusTextScreen("Device is offline")
            }
            SocketListenerStatus.ActiveMode -> {
                ActiveAdsHandler(
                    mainAdsState = mainAdsState.value,
                    companyAdsState = companyAdsState.value,
                    onFetchMainAds = { adsPlayViewModel.fetchMainAds() }
                )
            }
    }



//    when {
//        mainAdsState.value is ApiResponse.Success && (mainAdsState.value as ApiResponse.Success<MainAdsModel>).data.ads.isNotEmpty() -> {
//            PlayMainAds(
//                mainAds = (mainAdsState.value as ApiResponse.Success<MainAdsModel>).data.ads,
//                companyAds = (companyAdsState.value as ApiResponse.Success<AdsModel>).data.data,
//                onLoopCompleted = {
//                    adsPlayViewModel.fetchMainAds() // fetch main ads after loop
//                }
//            )
//        }
//
//        companyAdsState.value is ApiResponse.Success &&
//                (companyAdsState.value as ApiResponse.Success<AdsModel>).data.data.isNotEmpty() -> {
//            PlayCompanyAds(
//                ads = (companyAdsState.value as ApiResponse.Success<AdsModel>).data.data,
//                onLoopCompleted = {
//                    adsPlayViewModel.fetchMainAds() // try fetching main ads again
//                }
//            )
//        }
//
//        else -> NoAdsFoundScreen()
//
//    }
}