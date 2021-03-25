package com.bakulabs.twittervideodownloader.downloader

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bakulabs.twittervideodownloader.domain.Variant
import com.bakulabs.twittervideodownloader.network.TwitterApi
import com.bakulabs.twittervideodownloader.network.asDomainModel
import kotlinx.coroutines.launch
import timber.log.Timber

class DownloaderViewModel : ViewModel() {
    var variants: List<Variant> by mutableStateOf(listOf())
        private set

    fun getVariants(tweetUrl: String) {
        // TODO: call service to update variants
        viewModelScope.launch {
            try {
                val status = TwitterApi.service.getStatus("Network request successful")
                Timber.i(status.id.toString())
                variants =
                    status.extendedEntities.media[0].videoInfo?.variants?.filter { it.contentType == "video/mp4" }
                        ?.map { it.asDomainModel() }
                        ?: listOf()
            } catch (e: Exception) {
                e.message?.let {
                    Timber.i(it)
                }
            }
        }

    }

    fun downloadVariant(variantUrl: String) {
        // TODO: download variant
    }
}