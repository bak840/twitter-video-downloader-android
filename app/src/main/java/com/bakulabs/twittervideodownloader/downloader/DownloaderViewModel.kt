package com.bakulabs.twittervideodownloader.downloader

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bakulabs.twittervideodownloader.domain.Variant

class DownloaderViewModel: ViewModel() {
    var url: String by mutableStateOf("")
        private set

    var variants: List<Variant> by mutableStateOf(listOf())
        private set

    fun fetchVariants() {
        // TODO: call service to update variants
    }

    fun downloadVariant(index: Int) {
        // TODO: download variant
    }
}