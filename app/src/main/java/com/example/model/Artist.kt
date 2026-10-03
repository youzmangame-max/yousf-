package com.example.model

data class Artist(
    val id: String,
    val name: String,
    val imageUrl: String,
    val monthlyListeners: String,
    val bio: String,
    val genre: String,
    val songIds: List<String> = emptyList(),
    val albumIds: List<String> = emptyList(),
    val isFollowed: Boolean = false
)

data class Album(
    val id: String,
    val title: String,
    val artist: String,
    val coverUrl: String,
    val year: String,
    val genre: String,
    val songIds: List<String> = emptyList()
)

data class MusicCategory(
    val id: String,
    val title: String,
    val iconName: String,
    val gradientColors: List<Long>
)
