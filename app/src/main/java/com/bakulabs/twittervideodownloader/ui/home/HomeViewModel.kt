package com.bakulabs.twittervideodownloader.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bakulabs.twittervideodownloader.R
import com.bakulabs.twittervideodownloader.api.TwitterApi
import com.bakulabs.twittervideodownloader.api.asDomainModel
import com.bakulabs.twittervideodownloader.domain.Variant
import com.bakulabs.twittervideodownloader.util.getTweetIdFromUrl
import com.bakulabs.twittervideodownloader.util.isTweetUrlValid
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel : ViewModel() {
    var isLoading: Boolean by mutableStateOf(false)

    var variants: List<Variant> by mutableStateOf(listOf())
        private set

    sealed class Event {
        object ShowSheet : Event()
        data class ShowSnackBar(val resId: Int): Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun getVariants(url: String) {
        if (isTweetUrlValid(url)) {
            val id = getTweetIdFromUrl(url)
            if (id != null) {
                viewModelScope.launch {
                    isLoading = true
                    try {
                        val status = TwitterApi.service.getTweet(id)
                        Timber.d("Successfully retrieved Tweet ${status.id}")
                        variants =
                            status.extendedEntities?.media?.get(0)?.videoInfo?.variants?.filter { it.contentType == "video/mp4" }
                                ?.map { it.asDomainModel() }
                                ?: listOf()
                        isLoading = false
                        if (variants.isNotEmpty()) {
                            Timber.d("Send show sheet event")
                            eventChannel.send(Event.ShowSheet)
                        }
                    } catch (e: Exception) {
                        e.message?.let {
                            Timber.d(it)
                        }
                        isLoading = false
                    }
                }
            } else {
                Timber.d("Failed to get tweet id")
                viewModelScope.launch {
                    eventChannel.send(Event.ShowSnackBar(R.string.tweet_url_invalid))
                }
            }
        }
        else {
            Timber.d("Tweet URL invalid")
            viewModelScope.launch {
                eventChannel.send(Event.ShowSnackBar(R.string.tweet_url_invalid))
            }
        }
    }

    fun downloadVariant(variantUrl: String) {
        // TODO: download variant
    }
}