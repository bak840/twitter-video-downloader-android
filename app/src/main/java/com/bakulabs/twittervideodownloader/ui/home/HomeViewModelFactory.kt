package com.bakulabs.twittervideodownloader.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bakulabs.twittervideodownloader.api.VideoRepository

class HomeViewModelFactory(private val videoRepository: VideoRepository, private val initUrl: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(videoRepository, initUrl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}