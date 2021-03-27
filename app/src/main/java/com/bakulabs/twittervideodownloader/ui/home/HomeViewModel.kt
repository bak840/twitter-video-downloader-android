package com.bakulabs.twittervideodownloader.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bakulabs.twittervideodownloader.api.asDomainModel
import com.bakulabs.twittervideodownloader.api.TwitterApi
import com.bakulabs.twittervideodownloader.domain.Variant
import com.bakulabs.twittervideodownloader.util.getTweetIdFromUrl
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel : ViewModel() {
    var variants: List<Variant> by mutableStateOf(listOf())
        private set

    fun getVariants(url: String) {
        // TODO: call service to update variants
        val id = getTweetIdFromUrl(url)
        if (id != null) {
            viewModelScope.launch {
                try {
                    Timber.i("Network request successful")
                    val status = TwitterApi.service.getTweet(id)
                    Timber.i(status.id.toString())
                    variants =
                        status.extendedEntities?.media?.get(0)?.videoInfo?.variants?.filter { it.contentType == "video/mp4" }
                            ?.map { it.asDomainModel() }
                            ?: listOf()
                } catch (e: Exception) {
                    e.message?.let {
                        Timber.i(it)
                    }
                }
            }
        }
        else {
            Timber.d("Failed to get tweet id")
        }
    }

    fun downloadVariant(variantUrl: String) {
        // TODO: download variant
    }
}