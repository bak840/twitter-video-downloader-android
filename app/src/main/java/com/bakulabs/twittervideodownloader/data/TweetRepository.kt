package com.bakulabs.twittervideodownloader.data

import com.bakulabs.twittervideodownloader.network.Tweet

interface TweetRepository {
    suspend fun getTweet(id: String): DataResult<Tweet>
}