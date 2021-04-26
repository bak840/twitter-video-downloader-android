package com.bakulabs.twittervideodownloader.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bakulabs.twittervideodownloader.services.VideoDownloadService

class HomeViewModelFactory(private val videoDownloadService: VideoDownloadService, private val initUrl: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(videoDownloadService, initUrl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}