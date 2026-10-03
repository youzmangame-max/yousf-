package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val coverUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val songIds: String = "" // Comma-separated song IDs
)

@Entity(tableName = "favorite_songs")
data class FavoriteSongEntity(
    @PrimaryKey val songId: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationSeconds: Int,
    val coverUrl: String,
    val audioUrl: String,
    val genre: String = "Pop",
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "play_history")
data class HistoryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val songId: String,
    val title: String,
    val artist: String,
    val album: String,
    val coverUrl: String,
    val audioUrl: String,
    val durationSeconds: Int,
    val playedAt: Long = System.currentTimeMillis()
)
