package com.bakulabs.twittervideodownloader.api

import com.bakulabs.twittervideodownloader.util.getTwitterApiBearerToken
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import se.ansman.kotshi.KotshiJsonAdapterFactory

const val BASE_URL = "https://api.twitter.com"

interface TwitterApiService {
    @GET("/1.1/statuses/show/{id}.json?tweet_mode=extended")
    suspend fun getTweet(@Path("id") id: String): Tweet
}

@KotshiJsonAdapterFactory
object ApplicationJsonAdapterFactory : JsonAdapter.Factory by KotshiApplicationJsonAdapterFactory

private val moshi = Moshi.Builder()
    .add(ApplicationJsonAdapterFactory)
    .build()

private var client = OkHttpClient.Builder().addInterceptor { chain ->
    val newRequest: Request = chain.request().newBuilder()
        .addHeader("Authorization", "Bearer ${getTwitterApiBearerToken()}")
        .build()
    chain.proceed(newRequest)
}.build()

private val retrofit = Retrofit.Builder()
    .client(client)
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

object TwitterApi {
    val service: TwitterApiService by lazy { retrofit.create(TwitterApiService::class.java) }
}