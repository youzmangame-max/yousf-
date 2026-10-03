package com.example.model

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationSeconds: Int,
    val coverUrl: String,
    val audioUrl: String,
    val genre: String = "Pop",
    val youtubeId: String? = null,
    val isLiked: Boolean = false,
    val plays: String = "1.2M"
) {
    val durationFormatted: String
        get() {
            val minutes = durationSeconds / 60
            val seconds = durationSeconds % 60
            return "%d:%02d".format(minutes, seconds)
        }
}
