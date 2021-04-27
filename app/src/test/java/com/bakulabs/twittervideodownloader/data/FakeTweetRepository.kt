package com.bakulabs.twittervideodownloader.data

import com.bakulabs.twittervideodownloader.network.Tweet
import java.lang.Exception

class FakeTweetRepository(private var tweets: List<Tweet>): TweetRepository {
    override suspend fun getTweet(id: String): DataResult<Tweet> {
        val tweet = tweets.firstOrNull { it.id.toString() == id }
        if (tweet != null) {
            return DataResult.Success(tweet)
        }
        else {
            return DataResult.Error(Exception("Tweet not found"))
        }
    }
}