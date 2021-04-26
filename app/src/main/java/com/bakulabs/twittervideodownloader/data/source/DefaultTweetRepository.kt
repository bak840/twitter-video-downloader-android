package com.bakulabs.twittervideodownloader.data.source

import com.bakulabs.twittervideodownloader.data.Result
import com.bakulabs.twittervideodownloader.network.Tweet
import com.bakulabs.twittervideodownloader.network.TwitterApiService

class DefaultTweetRepository(private val apiService: TwitterApiService):TweetRepository {
    override suspend fun getTweet(id: String): Result<Tweet> = try {
        val tweet = apiService.getTweet(id)
        Result.Success(tweet)
    } catch (e: Exception) {
        Result.Error(e)
    }
}