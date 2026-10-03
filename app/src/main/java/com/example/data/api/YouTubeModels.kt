package com.example.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class YouTubeSearchResponse(
    @Json(name = "items") val items: List<YouTubeSearchItem> = emptyList(),
    @Json(name = "nextPageToken") val nextPageToken: String? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeSearchItem(
    @Json(name = "id") val id: YouTubeIdInfo,
    @Json(name = "snippet") val snippet: YouTubeSnippet
)

@JsonClass(generateAdapter = true)
data class YouTubeIdInfo(
    @Json(name = "kind") val kind: String? = null,
    @Json(name = "videoId") val videoId: String? = null,
    @Json(name = "channelId") val channelId: String? = null,
    @Json(name = "playlistId") val playlistId: String? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeSnippet(
    @Json(name = "title") val title: String = "",
    @Json(name = "description") val description: String = "",
    @Json(name = "channelTitle") val channelTitle: String = "",
    @Json(name = "channelId") val channelId: String = "",
    @Json(name = "publishedAt") val publishedAt: String = "",
    @Json(name = "thumbnails") val thumbnails: YouTubeThumbnails? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeThumbnails(
    @Json(name = "default") val default: YouTubeThumbnailInfo? = null,
    @Json(name = "medium") val medium: YouTubeThumbnailInfo? = null,
    @Json(name = "high") val high: YouTubeThumbnailInfo? = null,
    @Json(name = "maxres") val maxres: YouTubeThumbnailInfo? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeThumbnailInfo(
    @Json(name = "url") val url: String = "",
    @Json(name = "width") val width: Int? = null,
    @Json(name = "height") val height: Int? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeVideoListResponse(
    @Json(name = "items") val items: List<YouTubeVideoItem> = emptyList()
)

@JsonClass(generateAdapter = true)
data class YouTubeVideoItem(
    @Json(name = "id") val id: String = "",
    @Json(name = "snippet") val snippet: YouTubeSnippet? = null,
    @Json(name = "contentDetails") val contentDetails: YouTubeContentDetails? = null,
    @Json(name = "statistics") val statistics: YouTubeStatistics? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeContentDetails(
    @Json(name = "duration") val duration: String = ""
)

@JsonClass(generateAdapter = true)
data class YouTubeStatistics(
    @Json(name = "viewCount") val viewCount: String = "",
    @Json(name = "likeCount") val likeCount: String? = null
)
