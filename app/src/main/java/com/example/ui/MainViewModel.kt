package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MusicCatalog
import com.example.data.MusicDatabase
import com.example.data.MusicRepository
import com.example.data.SearchResult
import com.example.data.YouTubeMusicService
import com.example.data.api.YouTubeApiClient
import com.example.model.Album
import com.example.model.Artist
import com.example.model.FavoriteSongEntity
import com.example.model.HistoryItemEntity
import com.example.model.PlaylistEntity
import com.example.model.Song
import com.example.player.MusicPlayerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenTab {
    HOME,
    EXPLORE,
    SEARCH,
    LIBRARY
}

enum class SearchFilter {
    ALL,
    SONGS,
    ARTISTS,
    ALBUMS
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("nagham_music_prefs", Context.MODE_PRIVATE)

    private val database = MusicDatabase.getDatabase(application)
    val repository = MusicRepository(
        database.playlistDao(),
        database.favoriteDao(),
        database.historyDao()
    )
    val playerManager = MusicPlayerManager(application)

    // Navigation & UI States
    private val _currentTab = MutableStateFlow(ScreenTab.HOME)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    private val _selectedArtist = MutableStateFlow<Artist?>(null)
    val selectedArtist: StateFlow<Artist?> = _selectedArtist.asStateFlow()

    private val _selectedAlbum = MutableStateFlow<Album?>(null)
    val selectedAlbum: StateFlow<Album?> = _selectedAlbum.asStateFlow()

    private val _selectedPlaylist = MutableStateFlow<PlaylistEntity?>(null)
    val selectedPlaylist: StateFlow<PlaylistEntity?> = _selectedPlaylist.asStateFlow()

    private val _isPlayerExpanded = MutableStateFlow(false)
    val isPlayerExpanded: StateFlow<Boolean> = _isPlayerExpanded.asStateFlow()

    private val _songForPlaylistDialog = MutableStateFlow<Song?>(null)
    val songForPlaylistDialog: StateFlow<Song?> = _songForPlaylistDialog.asStateFlow()

    private val _showCreatePlaylistDialog = MutableStateFlow(false)
    val showCreatePlaylistDialog: StateFlow<Boolean> = _showCreatePlaylistDialog.asStateFlow()

    private val _showApiConfigDialog = MutableStateFlow(false)
    val showApiConfigDialog: StateFlow<Boolean> = _showApiConfigDialog.asStateFlow()

    // YouTube API state
    private val _youtubeApiKey = MutableStateFlow(
        prefs.getString("custom_youtube_api_key", "") ?: ""
    )
    val youtubeApiKey: StateFlow<String> = _youtubeApiKey.asStateFlow()

    // Search state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchFilter = MutableStateFlow(SearchFilter.ALL)
    val searchFilter: StateFlow<SearchFilter> = _searchFilter.asStateFlow()

    private val _searchResults = MutableStateFlow(
        SearchResult(
            songs = MusicCatalog.songs,
            artists = MusicCatalog.artists,
            albums = MusicCatalog.albums,
            isLiveFromApi = false
        )
    )
    val searchResults: StateFlow<SearchResult> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    // Room flows
    val playlists: StateFlow<List<PlaylistEntity>> = repository.allPlaylists.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favoriteSongs: StateFlow<List<FavoriteSongEntity>> = repository.allFavorites.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val historyItems: StateFlow<List<HistoryItemEntity>> = repository.recentHistory.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        playerManager.setOnSongPlayedListener { song ->
            viewModelScope.launch {
                repository.recordHistory(song)
            }
        }

        // Seed sample playlist if empty
        viewModelScope.launch {
            if (playlists.value.isEmpty()) {
                repository.createPlaylist(
                    title = "مختارات YouTube Music",
                    description = "أحدث وأشهر الأغاني الرائجة والأكثر طلباً",
                    coverUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=600&auto=format&fit=crop&q=80"
                )
            }
        }
    }

    fun selectTab(tab: ScreenTab) {
        _currentTab.value = tab
        _selectedArtist.value = null
        _selectedAlbum.value = null
        _selectedPlaylist.value = null
    }

    fun openArtist(artist: Artist) {
        _selectedArtist.value = artist
    }

    fun openAlbum(album: Album) {
        _selectedAlbum.value = album
    }

    fun openPlaylist(playlist: PlaylistEntity) {
        _selectedPlaylist.value = playlist
    }

    fun navigateBack(): Boolean {
        return when {
            _isPlayerExpanded.value -> {
                _isPlayerExpanded.value = false
                true
            }
            _selectedArtist.value != null -> {
                _selectedArtist.value = null
                true
            }
            _selectedAlbum.value != null -> {
                _selectedAlbum.value = null
                true
            }
            _selectedPlaylist.value != null -> {
                _selectedPlaylist.value = null
                true
            }
            else -> false
        }
    }

    fun expandPlayer() {
        _isPlayerExpanded.value = true
    }

    fun collapsePlayer() {
        _isPlayerExpanded.value = false
    }

    fun openAddToPlaylist(song: Song) {
        _songForPlaylistDialog.value = song
    }

    fun closeAddToPlaylist() {
        _songForPlaylistDialog.value = null
    }

    fun openCreatePlaylistDialog() {
        _showCreatePlaylistDialog.value = true
    }

    fun closeCreatePlaylistDialog() {
        _showCreatePlaylistDialog.value = false
    }

    fun openApiConfigDialog() {
        _showApiConfigDialog.value = true
    }

    fun closeApiConfigDialog() {
        _showApiConfigDialog.value = false
    }

    fun saveYouTubeApiKey(key: String) {
        val trimmed = key.trim()
        _youtubeApiKey.value = trimmed
        prefs.edit().putString("custom_youtube_api_key", trimmed).apply()
        // Re-execute search if active
        if (_searchQuery.value.isNotBlank()) {
            onSearchQueryChanged(_searchQuery.value)
        }
    }

    fun isYouTubeApiConnected(): Boolean {
        val effective = YouTubeApiClient.getApiKey(_youtubeApiKey.value)
        return effective.isNotBlank() && effective != "YOUR_YOUTUBE_API_KEY"
    }

    fun playSong(song: Song, queue: List<Song> = emptyList()) {
        val q = if (queue.isNotEmpty()) queue else MusicCatalog.songs
        playerManager.playSong(song, q)
    }

    fun playAll(songs: List<Song>, shuffle: Boolean = false) {
        if (songs.isEmpty()) return
        val list = if (shuffle) songs.shuffled() else songs
        playerManager.playSong(list[0], list)
    }

    fun togglePlayPause() = playerManager.togglePlayPause()
    fun playNext() = playerManager.playNext()
    fun playPrevious() = playerManager.playPrevious()
    fun seekTo(positionMs: Long) = playerManager.seekTo(positionMs)
    fun toggleRepeat() = playerManager.toggleRepeatMode()
    fun toggleShuffle() = playerManager.toggleShuffle()

    fun toggleFavorite(song: Song) {
        viewModelScope.launch {
            repository.toggleFavorite(song)
        }
    }

    fun isFavorite(songId: String): Boolean {
        return favoriteSongs.value.any { it.songId == songId }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _isSearching.value = true
            val results = YouTubeMusicService.search(query, _youtubeApiKey.value)
            _searchResults.value = results
            _isSearching.value = false
        }
    }

    fun setFilterChip(filter: SearchFilter) {
        _searchFilter.value = filter
    }

    fun createPlaylist(name: String, desc: String) {
        viewModelScope.launch {
            if (name.isNotBlank()) {
                val newId = repository.createPlaylist(name.trim(), desc.trim())
                _songForPlaylistDialog.value?.let { song ->
                    repository.addSongToPlaylist(newId, song.id)
                }
                closeCreatePlaylistDialog()
            }
        }
    }

    fun addSongToPlaylist(playlistId: Long, songId: String) {
        viewModelScope.launch {
            repository.addSongToPlaylist(playlistId, songId)
            closeAddToPlaylist()
        }
    }

    fun removeSongFromPlaylist(playlistId: Long, songId: String) {
        viewModelScope.launch {
            repository.removeSongFromPlaylist(playlistId, songId)
            // Refresh currently selected playlist view
            val updated = repository.getPlaylistById(playlistId)
            _selectedPlaylist.value = updated
        }
    }

    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            repository.deletePlaylist(playlistId)
            if (_selectedPlaylist.value?.id == playlistId) {
                _selectedPlaylist.value = null
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }
}
