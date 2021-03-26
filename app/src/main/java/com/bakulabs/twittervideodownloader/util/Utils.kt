package com.bakulabs.twittervideodownloader.util

import com.bakulabs.twittervideodownloader.BuildConfig

fun isTweetUrlValid(url: String): Boolean {
    val pattern = "^https://twitter\\.com/\\w+/status/\\d+.*\$"
    val regex = Regex(pattern = pattern, options = setOf(RegexOption.IGNORE_CASE))
    return regex.matches(url)
}

fun getTweetIdFromUrl(url: String): String? {
    val pattern = "^https://twitter\\.com/\\w+/status/(\\d+).*\$"
    val regex = Regex(pattern = pattern, options = setOf(RegexOption.IGNORE_CASE))
    return regex.find(url)?.groups?.get(1)?.value
}

fun getTwitterApiBearerToken(): String {
    return BuildConfig.TWITTER_API_BEARER_TOKEN
}

fun getVariantDefinitionFromUrl(variantUrl: String): String {
    val pattern = "^.*/(\\d+x\\d+).*\$"
    val regex = Regex(pattern = pattern, options = setOf(RegexOption.IGNORE_CASE))
    return regex.find(variantUrl)?.groups?.get(1)?.value!!
}