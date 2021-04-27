package com.bakulabs.twittervideodownloader.data.source

import com.bakulabs.twittervideodownloader.data.Result
import com.bakulabs.twittervideodownloader.network.Tweet
import java.lang.Exception

class FakeTweetRepository(private var tweets: List<Tweet>): TweetRepository {
    override suspend fun getTweet(id: String): Result<Tweet> {
        val tweet = tweets.firstOrNull { it.id.toString() == id }
        if (tweet != null) {
            return Result.Success(tweet)
        }
        else {
            return Result.Error(Exception("Tweet not found"))
        }
    }
}