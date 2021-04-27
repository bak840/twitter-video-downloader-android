package com.bakulabs.twittervideodownloader.di

import android.content.Context
import com.bakulabs.twittervideodownloader.network.VideoDownloadService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DownloadModule {
    @Provides
    @Singleton
    fun provideDownloadService(@ApplicationContext context: Context) = VideoDownloadService(context.contentResolver)
}