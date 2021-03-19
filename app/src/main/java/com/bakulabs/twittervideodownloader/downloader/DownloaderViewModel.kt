package com.bakulabs.twittervideodownloader.downloader

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bakulabs.twittervideodownloader.domain.Variant

class DownloaderViewModel: ViewModel() {
    var variants: List<Variant> by mutableStateOf(listOf())
        private set

    fun fetchVariants(tweetUrl: String) {
        // TODO: call service to update variants
    }

    fun downloadVariant(variantUrl: String) {
        // TODO: download variant
    }
}