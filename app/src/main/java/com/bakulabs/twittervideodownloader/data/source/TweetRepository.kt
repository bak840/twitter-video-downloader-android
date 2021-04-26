package com.bakulabs.twittervideodownloader.data.source

import com.bakulabs.twittervideodownloader.data.Result
import com.bakulabs.twittervideodownloader.network.Tweet

interface TweetRepository {
    suspend fun getTweet(id: String): Result<Tweet>
}