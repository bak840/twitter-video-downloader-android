package com.bakulabs.twittervideodownloader

import com.bakulabs.twittervideodownloader.util.getTweetIdFromUrl
import com.bakulabs.twittervideodownloader.util.getVariantDefinitionFromUrl
import com.bakulabs.twittervideodownloader.util.isTweetUrlValid
import org.junit.Test

import org.junit.Assert.*

class UtilsTest {
    @Test
    fun isTweetUrlValid_Valid() {
        val url = "https://twitter.com/bak840/status/1362860958092316675?s=20"

        val isValid = isTweetUrlValid(url)

        assertEquals(true, isValid)
    }

    @Test
    fun isTweetUrlValid_Valid_NoQuery() {
        val url = "https://twitter.com/bak840/status/1362860958092316675"

        val isValid = isTweetUrlValid(url)

        assertEquals(true, isValid)
    }

    @Test
    fun isTweetUrlValid_Invalid_WrongDomainName() {
        val url = "https://t.co/bak840/status/1362860958092316675?s=20"

        val isValid = isTweetUrlValid(url)

        assertEquals(false, isValid)
    }

    @Test
    fun isTweetUrlValid_Invalid_Http() {
        val url = "http://twitter.com/bak840/status/1362860958092316675?s=20"

        val isValid = isTweetUrlValid(url)

        assertEquals(false, isValid)
    }

    @Test
    fun getTweetIdFromUrl_isWorking() {
        val url = "https://twitter.com/bak840/status/1362860958092316675?s=20"

        val tweetId = getTweetIdFromUrl(url)

        assertEquals("1362860958092316675", tweetId)
    }

    @Test
    fun getVariantDefinition_isWorking() {
        val url =
            "https://video.twimg.com/ext_tw_video/1362860550460473345/pu/vid/1280x720/x0dMoNnpfSmZGqF8.mp4?tag=10"

        val definition = getVariantDefinitionFromUrl(url)

        assertEquals("1280x720", definition)
    }
}