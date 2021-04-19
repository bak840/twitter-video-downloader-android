package com.bakulabs.twittervideodownloader.api

import java.lang.Exception

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

interface ITweetRepository {
    suspend fun getTweet(id: String): Result<Tweet>
}

class TweetRepository : ITweetRepository {
    override suspend fun getTweet(id: String): Result<Tweet> = try {
        val tweet = TwitterApi.service.getTweet(id)
        Result.Success(tweet)
    } catch (e: Exception) {
        Result.Error(e)
    }
}