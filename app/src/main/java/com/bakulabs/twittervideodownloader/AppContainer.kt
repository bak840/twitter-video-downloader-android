package com.bakulabs.twittervideodownloader

import android.content.ContentResolver
import com.bakulabs.twittervideodownloader.data.source.DefaultTweetRepository
import com.bakulabs.twittervideodownloader.network.BASE_URL
import com.bakulabs.twittervideodownloader.network.TwitterApiService
import com.bakulabs.twittervideodownloader.network.VideoDownloadService
import com.bakulabs.twittervideodownloader.util.getTwitterApiBearerToken
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class AppContainer(resolver: ContentResolver) {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
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
        .create(TwitterApiService::class.java)

    val tweetRepository = DefaultTweetRepository(retrofit)
    val videoDownloadService = VideoDownloadService(resolver)
}