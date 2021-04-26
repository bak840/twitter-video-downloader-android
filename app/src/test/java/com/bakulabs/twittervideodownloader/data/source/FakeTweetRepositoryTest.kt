package com.bakulabs.twittervideodownloader.data.source

import com.bakulabs.twittervideodownloader.data.Result
import com.bakulabs.twittervideodownloader.network.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Test

@ExperimentalCoroutinesApi
class FakeTweetRepositoryTest {
    private val tweet = Tweet(
        id = 123456789L,
        extendedEntities = ExtendedEntities(
            media = listOf(
                Media(
                    videoInfo = VideoInfo(
                        variants = listOf(
                            Variant(
                                bitrate = 2176000L,
                                contentType = "video/mp4",
                                url = "https://video.twimg.com/ext_tw_video/123456789/pu/vid/1280x720/123456789.mp4?tag=10"
                            ),
                            Variant(
                                bitrate = 832000L,
                                contentType = "video/mp4",
                                url = "https://video.twimg.com/ext_tw_video/123456789/pu/vid/1280x720/123456789.mp4?tag=10"
                            ),
                        )
                    )
                )
            )
        )
    )

    @Test
    fun getTweet_success() = runBlockingTest {
        val response = FakeTweetRepository(tweet).getTweet("") as Result.Success

        assertThat(response.data, IsEqual(tweet))
    }

    @Test
    fun getTweet_error() = runBlockingTest {
        val response = FakeTweetRepository(null).getTweet("") as Result.Error

        assertThat(response.exception.message, IsEqual("tweet not found"))
    }
}