package com.bakulabs.twittervideodownloader.data

import com.bakulabs.twittervideodownloader.network.Tweet
import com.bakulabs.twittervideodownloader.network.TwitterApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultTweetRepository @Inject constructor(private val apiService: TwitterApiService): TweetRepository {
    override suspend fun getTweet(id: String): DataResult<Tweet> = try {
        val tweet = apiService.getTweet(id)
        DataResult.Success(tweet)
    } catch (e: Exception) {
        DataResult.Error(e)
    }
}