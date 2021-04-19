package com.bakulabs.twittervideodownloader.api

import com.bakulabs.twittervideodownloader.util.getVariantDefinitionFromUrl
import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class Tweet(
    val id: Long,

    @Json(name = "extended_entities")
    val extendedEntities: ExtendedEntities?,
)

@JsonSerializable
data class ExtendedEntities(
    val media: List<Media>
)

@JsonSerializable
data class Media(
    @Json(name = "video_info")
    val videoInfo: VideoInfo?,
)

@JsonSerializable
data class VideoInfo(
    val variants: List<Variant>
)

@JsonSerializable
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
        definition = getVariantDefinitionFromUrl(url) ?: "",
        size = ""
    )
}