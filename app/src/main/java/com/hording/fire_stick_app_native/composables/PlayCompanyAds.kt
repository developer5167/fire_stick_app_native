package com.hording.fire_stick_app_native.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.hording.fire_stick_app_native.models.Data
import kotlinx.coroutines.delay

@Composable
fun PlayCompanyAds(
    ads: List<Data>,
    onLoopCompleted: () -> Unit,
    onAdPlayed: (Data, Boolean) -> Unit // Changed to include isMainAd
) {
    var idx by remember { mutableIntStateOf(0) }

    LaunchedEffect(idx) {
        delay(10000)

        if (idx == ads.lastIndex) {
            onLoopCompleted()
            idx = 0
        } else {
            idx++
        }
    }

    PlayAd(ads[idx], onAdPlayed = { ad -> onAdPlayed(ad, false) })
}
