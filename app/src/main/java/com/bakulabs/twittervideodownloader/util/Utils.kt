package com.bakulabs.twittervideodownloader.util

import com.bakulabs.twittervideodownloader.BuildConfig


const val PATTERN = "^https://twitter\\.com/\\w+/status/(\\d+).*\$"

fun isTweetUrlValid(url: String): Boolean {
    val regex = Regex(pattern = PATTERN, options = setOf(RegexOption.IGNORE_CASE))
    return regex.matches(url)
}

fun getTweetIdFromUrl(url: String): String? {
    val regex = Regex(pattern = PATTERN, options = setOf(RegexOption.IGNORE_CASE))
    return regex.find(url)?.groups?.get(1)?.value
}

fun getTwitterApiBearerToken(): String {
    return BuildConfig.TWITTER_API_BEARER_TOKEN
}