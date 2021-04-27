package com.bakulabs.twittervideodownloader.data

import com.bakulabs.twittervideodownloader.network.Tweet
import com.bakulabs.twittervideodownloader.network.TwitterApiService

class DefaultTweetRepository(private val apiService: TwitterApiService): TweetRepository {
    override suspend fun getTweet(id: String): DataResult<Tweet> = try {
        val tweet = apiService.getTweet(id)
        DataResult.Success(tweet)
    } catch (e: Exception) {
        DataResult.Error(e)
    }
}