package com.bakulabs.twittervideodownloader.network

import kotlinx.coroutines.flow.Flow

interface VideoDownloadService {
    suspend fun download(url: String, name: String): Flow<DownloadResult>
}