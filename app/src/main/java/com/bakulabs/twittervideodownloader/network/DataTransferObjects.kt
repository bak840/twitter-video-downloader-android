package com.bakulabs.twittervideodownloader.network

import com.bakulabs.twittervideodownloader.util.getVariantDefinitionFromUrl
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tweet(
    val id: Long,

    @Json(name = "extended_entities")
    val extendedEntities: ExtendedEntities?,
)

@JsonClass(generateAdapter = true)
data class ExtendedEntities(
    val media: List<Media>
)

@JsonClass(generateAdapter = true)
data class Media(
    @Json(name = "video_info")
    val videoInfo: VideoInfo?,
)

@JsonClass(generateAdapter = true)
data class VideoInfo(
    val variants: List<Variant>
)

@JsonClass(generateAdapter = true)
data class Variant(
    val bitrate: Long? = null,

    @Json(name = "content_type")
    val contentType: String,

    val url: String
)

fun Tweet.getVariants() = extendedEntities?.media?.get(0)?.videoInfo?.variants?.filter { it.contentType == "video/mp4" }
    ?.map { it.asDomainModel() }
    ?: listOf()

fun Variant.asDomainModel(): com.bakulabs.twittervideodownloader.domain.Variant {
    return com.bakulabs.twittervideodownloader.domain.Variant(
        url = url,
        definition = getVariantDefinitionFromUrl(url) ?: ""
    )
}