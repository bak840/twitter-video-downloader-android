package com.bakulabs.twittervideodownloader.di

import com.bakulabs.twittervideodownloader.data.DefaultTweetRepository
import com.bakulabs.twittervideodownloader.network.BASE_URL
import com.bakulabs.twittervideodownloader.network.TwitterApiService
import com.bakulabs.twittervideodownloader.util.getTwitterApiBearerToken
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {
    @Provides
    fun provideBaseUrl() = BASE_URL

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder().addInterceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${getTwitterApiBearerToken()}")
            .build()
        chain.proceed(newRequest)
    }.build()

    @Provides
    fun providesRetrofit(client: OkHttpClient, moshi: Moshi, baseUrl: String): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    fun provideApiService(retrofit: Retrofit): TwitterApiService = retrofit.create(TwitterApiService::class.java)

    @Provides
    @Singleton
    fun provideTweetRepository(apiService: TwitterApiService) = DefaultTweetRepository(apiService)
}