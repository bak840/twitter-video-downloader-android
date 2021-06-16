package com.bakulabs.twittervideodownloader.network

import android.net.Uri

sealed class DownloadResult {
    data class Success(val uri: Uri) : DownloadResult()
    data class Error(val message: String) : DownloadResult()
}