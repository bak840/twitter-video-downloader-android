package com.bakulabs.twittervideodownloader.api

import com.bakulabs.twittervideodownloader.util.getVariantDefinitionFromUrl
import com.squareup.moshi.Json

data class Tweet(
    val id: Long,

    @Json(name = "extended_entities")
    val extendedEntities: ExtendedEntities?,
)

data class ExtendedEntities(
    val media: List<Media>
)

data class Media(
    @Json(name = "video_info")
    val videoInfo: VideoInfo?,
)

data class VideoInfo(
    val variants: List<Variant>
)

data class Variant(
    val bitrate: Long? = null,

    @Json(name = "content_type")
    val contentType: String,

    val url: String
)

fun Variant.asDomainModel(): com.bakulabs.twittervideodownloader.domain.Variant {
    return com.bakulabs.twittervideodownloader.domain.Variant(
        url = url,
        definition = getVariantDefinitionFromUrl(url),
        size = ""
    )
}