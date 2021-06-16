package com.bakulabs.twittervideodownloader.ui.home

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bakulabs.twittervideodownloader.R
import com.bakulabs.twittervideodownloader.data.DataResult
import com.bakulabs.twittervideodownloader.data.DefaultTweetRepository
import com.bakulabs.twittervideodownloader.domain.Variant
import com.bakulabs.twittervideodownloader.network.DownloadResult
import com.bakulabs.twittervideodownloader.network.DefaultVideoDownloadService
import com.bakulabs.twittervideodownloader.network.getVariants
import com.bakulabs.twittervideodownloader.util.getTweetIdFromUrl
import com.bakulabs.twittervideodownloader.util.isTweetUrlValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var tweetRepository: DefaultTweetRepository

    @Inject
    lateinit var downloadService: DefaultVideoDownloadService


    var url: String by mutableStateOf("initUrl")
        private set

    fun updateUrl(newValue: String) {
        url = newValue
    }

    var variants: List<Variant> by mutableStateOf(listOf())
        private set

    var isLoading: Boolean by mutableStateOf(false)
        private set

    var isSheetShowing: Boolean by mutableStateOf(false)
        private set

    var isSheetHiding: Boolean by mutableStateOf(false)
        private set

    fun dismissSheet() {
        isSheetShowing = false
    }

    var isVariantSnackBarShowing: Boolean by mutableStateOf(false)
        private set

    var variantUri: Uri by mutableStateOf(Uri.Builder().build())
        private set

    private fun showVariantSnackBar(uri: Uri) {
        variantUri = uri
        isVariantSnackBarShowing = true
    }

    fun dismissVariantSnackBar() {
        isVariantSnackBarShowing = false
    }

    var isErrorSnackBarShowing: Boolean by mutableStateOf(false)
        private set

    var errorResId: Int by mutableStateOf(0)
        private set

    private fun showErrorSnackBar(messageId: Int) {
        errorResId = messageId
        isErrorSnackBarShowing = true
    }

    fun dismissErrorSnackBar() {
        isErrorSnackBarShowing = false
    }

    fun getVariants() {
        Timber.d("Get variants button pressed")
        if (isTweetUrlValid(url)) {
            val id = getTweetIdFromUrl(url)
            if (id != null) {
                viewModelScope.launch {
                    isLoading = true
                    when (val tweet = tweetRepository.getTweet(id)) {
                        is DataResult.Error -> {
                            isLoading = false
                            tweet.exception.message?.let {
                                Timber.e(it)
                            }
                        }
                        is DataResult.Success -> {
                            Timber.d("Successfully fetched Tweet ${tweet.data.id}")
                            variants = tweet.data.getVariants()
                            delay(100)
                            isLoading = false
                            if (variants.isNotEmpty()) {
                                Timber.d("Show sheet")
                                isSheetShowing = true
                                isSheetHiding = false
                            } else {
                                Timber.i("No video in tweet")
                                showErrorSnackBar(R.string.no_video_in_tweet)
                            }
                        }
                    }
                }
            } else {
                Timber.i("Failed to get tweet id")
                showErrorSnackBar(R.string.tweet_url_invalid)
            }
        } else {
            Timber.i("Tweet URL invalid")
            showErrorSnackBar(R.string.tweet_url_invalid)
        }
    }

    fun downloadVariant(variant: Variant) {
        viewModelScope.launch {
            isLoading = true

            val fileName = "${getTweetIdFromUrl(url)}_${variant.definition}"

            downloadService.download(variant.url, fileName).collect { result ->
                isLoading = false
                delay(50)
                isSheetHiding = true
                isSheetShowing = false
                delay(50)
                when (result) {
                    is DownloadResult.Error -> {
                        Timber.e(result.message)
                        showErrorSnackBar(R.string.download_failed)
                    }
                    is DownloadResult.Success -> {
                        showVariantSnackBar(result.uri)
                    }
                }
            }
        }
    }
}