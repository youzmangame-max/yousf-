package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import com.example.model.Song
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

object YouTubeApiClient {
    private const val TAG = "YouTubeApiClient"
    private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val api: YouTubeApiService = retrofit.create(YouTubeApiService::class.java)

    private val audioPool = listOf(
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-9.mp3",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-10.mp3",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-11.mp3",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-12.mp3",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-13.mp3",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-14.mp3",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-15.mp3",
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-16.mp3"
    )

    fun getApiKey(customKey: String? = null): String {
        if (!customKey.isNullOrBlank()) return customKey.trim()
        val buildKey = BuildConfig.YOUTUBE_API_KEY
        return if (buildKey.isNotBlank() && buildKey != "YOUR_YOUTUBE_API_KEY") buildKey else ""
    }

    suspend fun searchLiveYouTube(query: String, apiKey: String): List<Song> {
        val trimmedKey = apiKey.trim()
        if (trimmedKey.isEmpty() || trimmedKey == "YOUR_YOUTUBE_API_KEY") {
            return emptyList()
        }

        return try {
            val response = api.searchMusicVideos(
                query = query,
                apiKey = trimmedKey
            )

            val videoIds = response.items.mapNotNull { it.id.videoId }
            if (videoIds.isEmpty()) return emptyList()

            // Fetch video details for durations and view counts
            val detailsMap = try {
                val detailsResponse = api.getVideoDetails(
                    ids = videoIds.joinToString(","),
                    apiKey = trimmedKey
                )
                detailsResponse.items.associateBy { it.id }
            } catch (e: Exception) {
                Log.w(TAG, "Failed to fetch video details: ${e.message}")
                emptyMap()
            }

            response.items.mapIndexedNotNull { index, item ->
                val videoId = item.id.videoId ?: return@mapIndexedNotNull null
                val snippet = item.snippet
                val detail = detailsMap[videoId]

                val durationSec = detail?.contentDetails?.duration?.let { parseIsoDuration(it) } ?: 210
                val viewsFormatted = detail?.statistics?.viewCount?.let { formatViews(it) } ?: "YouTube Music"

                val highThumb = snippet.thumbnails?.maxres?.url
                    ?: snippet.thumbnails?.high?.url
                    ?: snippet.thumbnails?.medium?.url
                    ?: "https://i.ytimg.com/vi/$videoId/hqdefault.jpg"

                // Clean title (remove official music video tags)
                val cleanTitle = snippet.title
                    .replace("&quot;", "\"")
                    .replace("&amp;", "&")
                    .replace("&#39;", "'")
                    .replace("(Official Music Video)", "", ignoreCase = true)
                    .replace("(Official Audio)", "", ignoreCase = true)
                    .replace("[Official Music Video]", "", ignoreCase = true)
                    .replace("(Official Video)", "", ignoreCase = true)
                    .trim()

                val audioUrl = audioPool[Math.abs(videoId.hashCode()) % audioPool.size]

                Song(
                    id = "yt_$videoId",
                    title = cleanTitle,
                    artist = snippet.channelTitle,
                    album = "YouTube Music",
                    durationSeconds = durationSec,
                    coverUrl = highThumb,
                    audioUrl = audioUrl,
                    genre = "YouTube Music",
                    youtubeId = videoId,
                    plays = viewsFormatted
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "YouTube API search failed: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun getPopularMusic(apiKey: String): List<Song> {
        val trimmedKey = apiKey.trim()
        if (trimmedKey.isEmpty() || trimmedKey == "YOUR_YOUTUBE_API_KEY") {
            return emptyList()
        }

        return try {
            val response = api.getMostPopularMusic(apiKey = trimmedKey)
            response.items.mapIndexed { index, item ->
                val videoId = item.id
                val snippet = item.snippet
                val durationSec = item.contentDetails?.duration?.let { parseIsoDuration(it) } ?: 220
                val viewsFormatted = item.statistics?.viewCount?.let { formatViews(it) } ?: "YouTube Music"

                val highThumb = snippet?.thumbnails?.maxres?.url
                    ?: snippet?.thumbnails?.high?.url
                    ?: "https://i.ytimg.com/vi/$videoId/hqdefault.jpg"

                val audioUrl = audioPool[Math.abs(videoId.hashCode()) % audioPool.size]

                Song(
                    id = "yt_pop_$videoId",
                    title = snippet?.title ?: "أغنية مميزة",
                    artist = snippet?.channelTitle ?: "YouTube Artist",
                    album = "YouTube Charts",
                    durationSeconds = durationSec,
                    coverUrl = highThumb,
                    audioUrl = audioUrl,
                    genre = "Trending",
                    youtubeId = videoId,
                    plays = viewsFormatted
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "YouTube popular music fetch failed: ${e.message}", e)
            emptyList()
        }
    }

    fun parseIsoDuration(durationStr: String): Int {
        try {
            val pattern = Pattern.compile("PT(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?")
            val matcher = pattern.matcher(durationStr)
            if (matcher.matches()) {
                val hours = matcher.group(1)?.toIntOrNull() ?: 0
                val minutes = matcher.group(2)?.toIntOrNull() ?: 0
                val seconds = matcher.group(3)?.toIntOrNull() ?: 0
                return hours * 3600 + minutes * 60 + seconds
            }
        } catch (e: Exception) {
            Log.w(TAG, "Could not parse duration: $durationStr")
        }
        return 200
    }

    fun formatViews(viewsStr: String): String {
        return try {
            val count = viewsStr.toLong()
            when {
                count >= 1_000_000_000 -> "%.1fB".format(count / 1_000_000_000.0)
                count >= 1_000_000 -> "%.1fM".format(count / 1_000_000.0)
                count >= 1_000 -> "%.0fK".format(count / 1_000.0)
                else -> count.toString()
            }
        } catch (e: Exception) {
            viewsStr
        }
    }
}
