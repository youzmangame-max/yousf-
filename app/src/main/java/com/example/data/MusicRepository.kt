package com.example.data

import com.example.model.FavoriteSongEntity
import com.example.model.HistoryItemEntity
import com.example.model.PlaylistEntity
import com.example.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MusicRepository(
    private val playlistDao: PlaylistDao,
    private val favoriteDao: FavoriteDao,
    private val historyDao: HistoryDao
) {
    val allPlaylists: Flow<List<PlaylistEntity>> = playlistDao.getAllPlaylists()
    val allFavorites: Flow<List<FavoriteSongEntity>> = favoriteDao.getAllFavorites()
    val recentHistory: Flow<List<HistoryItemEntity>> = historyDao.getRecentHistory()

    fun isFavoriteFlow(songId: String): Flow<Boolean> = favoriteDao.isFavoriteFlow(songId)

    suspend fun isFavorite(songId: String): Boolean = favoriteDao.isFavorite(songId)

    suspend fun toggleFavorite(song: Song): Boolean {
        val currentlyFav = favoriteDao.isFavorite(song.id)
        if (currentlyFav) {
            favoriteDao.deleteFavoriteBySongId(song.id)
            return false
        } else {
            favoriteDao.insertFavorite(
                FavoriteSongEntity(
                    songId = song.id,
                    title = song.title,
                    artist = song.artist,
                    album = song.album,
                    durationSeconds = song.durationSeconds,
                    coverUrl = song.coverUrl,
                    audioUrl = song.audioUrl,
                    genre = song.genre
                )
            )
            return true
        }
    }

    suspend fun recordHistory(song: Song) {
        historyDao.deleteBySongId(song.id)
        historyDao.insertHistory(
            HistoryItemEntity(
                songId = song.id,
                title = song.title,
                artist = song.artist,
                album = song.album,
                coverUrl = song.coverUrl,
                audioUrl = song.audioUrl,
                durationSeconds = song.durationSeconds
            )
        )
    }

    suspend fun createPlaylist(title: String, description: String = "", coverUrl: String? = null): Long {
        return playlistDao.insertPlaylist(
            PlaylistEntity(
                title = title,
                description = description,
                coverUrl = coverUrl ?: "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=600&auto=format&fit=crop&q=80",
                songIds = ""
            )
        )
    }

    suspend fun deletePlaylist(id: Long) {
        playlistDao.deletePlaylistById(id)
    }

    suspend fun addSongToPlaylist(playlistId: Long, songId: String) {
        val playlist = playlistDao.getPlaylistById(playlistId) ?: return
        val currentIds = playlist.songIds.split(",").filter { it.isNotBlank() }.toMutableList()
        if (!currentIds.contains(songId)) {
            currentIds.add(songId)
            val updated = playlist.copy(songIds = currentIds.joinToString(","))
            playlistDao.updatePlaylist(updated)
        }
    }

    suspend fun removeSongFromPlaylist(playlistId: Long, songId: String) {
        val playlist = playlistDao.getPlaylistById(playlistId) ?: return
        val currentIds = playlist.songIds.split(",").filter { it.isNotBlank() }.toMutableList()
        currentIds.remove(songId)
        val updated = playlist.copy(songIds = currentIds.joinToString(","))
        playlistDao.updatePlaylist(updated)
    }

    suspend fun getPlaylistById(id: Long): PlaylistEntity? {
        return playlistDao.getPlaylistById(id)
    }

    suspend fun seedInitialDataIfEmpty() {
        // We will seed an initial curated playlist if no playlists exist yet
    }
}
