package com.bakulabs.twittervideodownloader.data.source

import com.bakulabs.twittervideodownloader.data.Result
import com.bakulabs.twittervideodownloader.network.Tweet

class FakeTweetRepository(private var tweet: Tweet?): TweetRepository {
    override suspend fun getTweet(id: String): Result<Tweet> = if(tweet != null) {
        Result.Success(tweet!!)
    } else {
        Result.Error(Exception("tweet not found"))
    }
}