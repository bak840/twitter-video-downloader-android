package com.bakulabs.twittervideodownloader.downloader

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bakulabs.twittervideodownloader.domain.Variant

class DownloaderViewModel: ViewModel() {
    var variants: List<Variant> by mutableStateOf(listOf())
        private set

    fun getVariants(tweetUrl: String) {
        // TODO: call service to update variants
        variants = listOf(
            Variant("", "1920x1080", "36 kB"),
            Variant("", "1280x720","18 kB" )
        )
    }

    fun downloadVariant(variantUrl: String) {
        // TODO: download variant
    }
}