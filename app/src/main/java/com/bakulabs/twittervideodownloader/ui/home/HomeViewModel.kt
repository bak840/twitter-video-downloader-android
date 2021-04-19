package com.bakulabs.twittervideodownloader.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bakulabs.twittervideodownloader.R
import com.bakulabs.twittervideodownloader.api.Result
import com.bakulabs.twittervideodownloader.api.TweetRepository
import com.bakulabs.twittervideodownloader.api.getVariants
import com.bakulabs.twittervideodownloader.domain.Variant
import com.bakulabs.twittervideodownloader.util.getTweetIdFromUrl
import com.bakulabs.twittervideodownloader.util.isTweetUrlValid
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel : ViewModel() {
    private val tweetRepository = TweetRepository()

    var isLoading: Boolean by mutableStateOf(false)
        private set

    var showSnackBar: Boolean by mutableStateOf(false)
        private set

    var snackBarMessageId: Int by mutableStateOf(0)
        private set

    var showSheet: Boolean by mutableStateOf(false)
        private set

    var hideSheet: Boolean by mutableStateOf(false)
        private set

    var variants: List<Variant> by mutableStateOf(listOf())
        private set

    private fun showSnackBarMessage(messageId: Int) {
        snackBarMessageId = messageId
        showSnackBar = true
    }

    fun dismissSnackBar() {
        showSnackBar = false
    }

    fun dismissSheet() {
        showSheet = false
    }

    fun getVariants(url: String) {
        Timber.d("Get variants button pressed")
        if (isTweetUrlValid(url)) {
            val id = getTweetIdFromUrl(url)
            if (id != null) {
                viewModelScope.launch {
                    isLoading = true
                    when (val tweet = tweetRepository.getTweet(id)) {
                        is Result.Error -> {
                            isLoading = false
                            tweet.exception.message?.let {
                                Timber.e(it)
                            }
                        }
                        is Result.Success -> {
                            Timber.d("Successfully fetched Tweet ${tweet.data.id}")
                            variants = tweet.data.getVariants()
                            delay(100)
                            isLoading = false
                            if (variants.isNotEmpty()) {
                                Timber.d("Show sheet")
                                showSheet = true
                            } else {
                                Timber.i("No video in tweet")
                                showSnackBarMessage(R.string.no_video_in_tweet)
                            }
                        }
                    }
                }
            } else {
                Timber.i("Failed to get tweet id")
                showSnackBarMessage(R.string.tweet_url_invalid)
            }
        } else {
            Timber.i("Tweet URL invalid")
            showSnackBarMessage(R.string.tweet_url_invalid)
        }
    }

    fun downloadVariant(variant: Variant) {
        // TODO: download variant
    }
}