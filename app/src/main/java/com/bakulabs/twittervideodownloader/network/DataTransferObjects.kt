package com.bakulabs.twittervideodownloader.network

import com.bakulabs.twittervideodownloader.util.getVariantDefinitionFromUrl
import com.squareup.moshi.Json

data class Status(
    @Json(name = "created_at")
    val createdAt: String,

    val id: Long,

    @Json(name = "id_str")
    val idStr: String,

    @Json(name = "full_text")
    val fullText: String,

    val truncated: Boolean,

    @Json(name = "display_text_range")
    val displayTextRange: List<Long>,

    val entities: Any?,

    @Json(name = "extended_entities")
    val extendedEntities: ExtendedEntities,

    val source: String,

    @Json(name = "in_reply_to_status_id")
    val inReplyToStatusID: Long?,

    @Json(name = "in_reply_to_status_id_str")
    val inReplyToStatusIDStr: String?,

    @Json(name = "in_reply_to_user_id")
    val inReplyToUserID: Long?,

    @Json(name = "in_reply_to_user_id_str")
    val inReplyToUserIDStr: String?,

    @Json(name = "in_reply_to_screen_name")
    val inReplyToScreenName: String?,

    val user: Any?,
    val geo: Any?,
    val coordinates: Any?,
    val place: Any?,
    val contributors: Any?,

    @Json(name = "is_quote_status")
    val isQuoteStatus: Boolean,

    @Json(name = "retweet_count")
    val retweetCount: Long,

    @Json(name = "favorite_count")
    val favoriteCount: Long,

    val favorited: Boolean,
    val retweeted: Boolean,

    @Json(name = "possibly_sensitive")
    val possiblySensitive: Boolean,

    @Json(name = "possibly_sensitive_appealable")
    val possiblySensitiveAppealable: Boolean,

    val lang: String
)

data class ExtendedEntities(
    val media: List<Media>
)

data class Media(
    val id: Long,

    @Json(name = "id_str")
    val idStr: String,

    val indices: List<Long>,

    @Json(name = "media_url")
    val mediaURL: String,

    @Json(name = "media_url_https")
    val mediaURLHTTPS: String,

    val url: String,

    @Json(name = "display_url")
    val displayURL: String,

    @Json(name = "expanded_url")
    val expandedURL: String,

    val type: String,
    val sizes: Any,

    @Json(name = "video_info")
    val videoInfo: VideoInfo? = null,

    @Json(name = "additional_media_info")
    val additionalMediaInfo: Any?
)

data class VideoInfo(
    @Json(name = "aspect_ratio")
    val aspectRatio: List<Long>,

    @Json(name = "duration_millis")
    val durationMillis: Long,

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

