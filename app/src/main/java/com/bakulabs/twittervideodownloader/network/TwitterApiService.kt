package com.bakulabs.twittervideodownloader.network

import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://api.twitter.com"

interface TwitterApiService {
    @GET("/1.1/statuses/show/{id}.json?tweet_mode=extended")
    suspend fun getTweet(@Path("id") id: String): Tweet
}
