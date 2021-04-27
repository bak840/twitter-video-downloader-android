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
    private val tweets = listOf(
        Tweet(
            id = 11111111L,
            extendedEntities = ExtendedEntities(
                media = listOf(
                    Media(
                        videoInfo = VideoInfo(
                            variants = listOf(
                                Variant(
                                    bitrate = 2176000L,
                                    contentType = "video/mp4",
                                    url = "https://video.twimg.com/ext_tw_video/11111111/pu/vid/1280x720/11111111.mp4?tag=10"
                                ),
                                Variant(
                                    bitrate = 832000L,
                                    contentType = "video/mp4",
                                    url = "https://video.twimg.com/ext_tw_video/11111111/pu/vid/1280x720/11111111.mp4?tag=10"
                                ),
                            )
                        )
                    )
                )
            )
        ),
        Tweet(
            id = 22222222L,
            extendedEntities = ExtendedEntities(
                media = listOf(
                    Media(
                        videoInfo = VideoInfo(
                            variants = listOf(
                                Variant(
                                    bitrate = 2176000L,
                                    contentType = "video/mp4",
                                    url = "https://video.twimg.com/ext_tw_video/22222222/pu/vid/1280x720/22222222.mp4?tag=10"
                                ),
                                Variant(
                                    bitrate = 832000L,
                                    contentType = "video/mp4",
                                    url = "https://video.twimg.com/ext_tw_video/22222222/pu/vid/1280x720/22222222.mp4?tag=10"
                                ),
                            )
                        )
                    )
                )
            )
        )
    )

    @Test
    fun getTweet_success() = runBlockingTest {
        val response = FakeTweetRepository(tweets).getTweet("11111111") as Result.Success

        assertThat(response.data, IsEqual(tweets[0]))
    }

    @Test
    fun getTweet_error() = runBlockingTest {
        val response = FakeTweetRepository(tweets).getTweet("33333333") as Result.Error

        assertThat(response.exception.message, IsEqual("Tweet not found"))
    }
}