package com.bakulabs.twittervideodownloader.api

import java.lang.Exception

interface ITweetRepository {
    suspend fun getTweet(id: String): Result<Tweet>
}

class TweetRepository: ITweetRepository {
    override suspend fun getTweet(id: String): Result<Tweet> = try {
        val tweet = TwitterApi.service.getTweet(id)
        Result.Success(tweet)
    } catch (e: Exception) {
        Result.Error(e)
    }
}