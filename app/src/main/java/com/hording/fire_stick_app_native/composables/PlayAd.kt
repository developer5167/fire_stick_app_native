package com.hording.fire_stick_app_native.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hording.fire_stick_app_native.models.Data

@Composable
fun PlayAd(ad: Data, onAdPlayed: (Data) -> Unit = {}) {
    val context = LocalContext.current

    LaunchedEffect(ad.id) {
        onAdPlayed(ad)
    }

    when (ad.media_type.lowercase()) {
        "image", "img", "photo" -> {
            println("MEDIA_TYPE  :${ad.media_url}")
            val url = if (ad.media_url.startsWith("http")) {
                ad.media_url
            } else {
                "http://192.168.15.168:3000/${ad.media_url}"
            }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .crossfade(true)
                    .listener(
                        onError = { request, error ->
                            println("COIL_ERROR → ${error.throwable}")
                        },
                        onSuccess = { _, _ ->
                            println("COIL_SUCCESS → Loaded OK")
                        }
                    )
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        "video", "mp4" -> {
            VideoPlayer(url = ad.media_url)
        }

        else -> Text("Unknown media type")
    }
}
