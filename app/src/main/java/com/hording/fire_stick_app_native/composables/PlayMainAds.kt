package com.hording.fire_stick_app_native.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.hording.fire_stick_app_native.models.AdsModel
import com.hording.fire_stick_app_native.models.Data
import kotlinx.coroutines.delay

@Composable
fun PlayMainAds(
    mainAds: List<Data>,
    companyAds: List<Data>,
    onLoopCompleted: () -> Unit
) {
    var currentIndex by remember { mutableStateOf(0) }
    var showExtraCompanyAd by remember { mutableStateOf(false) }

    LaunchedEffect(currentIndex) {
        delay(10000)

        if (currentIndex == mainAds.lastIndex) {
            showExtraCompanyAd = true
        } else {
            currentIndex++
        }
    }

    if (showExtraCompanyAd && companyAds.isNotEmpty()) {
        val randomAd = companyAds.random()
        PlayAd(randomAd)
        LaunchedEffect(Unit) {
            delay(10000)
            onLoopCompleted()
            currentIndex = 0
            showExtraCompanyAd = false
        }
    } else {
        PlayAd(mainAds[currentIndex])
    }
}
