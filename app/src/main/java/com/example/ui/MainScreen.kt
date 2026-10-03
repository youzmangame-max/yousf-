package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.MusicCatalog
import com.example.model.Song
import com.example.ui.components.AddToPlaylistDialog
import com.example.ui.components.CreatePlaylistDialog
import com.example.ui.components.MiniPlayer
import com.example.ui.components.PlayerFullScreen
import com.example.ui.components.YouTubeApiDialog
import com.example.ui.screens.AlbumDetailScreen
import com.example.ui.screens.ArtistDetailScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.PlaylistDetailScreen
import com.example.ui.screens.SearchScreen

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    // Collect all ViewModel states
    val currentTab by viewModel.currentTab.collectAsState()
    val selectedArtist by viewModel.selectedArtist.collectAsState()
    val selectedAlbum by viewModel.selectedAlbum.collectAsState()
    val selectedPlaylist by viewModel.selectedPlaylist.collectAsState()
    val isPlayerExpanded by viewModel.isPlayerExpanded.collectAsState()
    val songForPlaylistDialog by viewModel.songForPlaylistDialog.collectAsState()
    val showCreatePlaylistDialog by viewModel.showCreatePlaylistDialog.collectAsState()
    val showApiConfigDialog by viewModel.showApiConfigDialog.collectAsState()
    val youtubeApiKey by viewModel.youtubeApiKey.collectAsState()

    val currentSong by viewModel.playerManager.currentSong.collectAsState()
    val isPlaying by viewModel.playerManager.isPlaying.collectAsState()
    val isBuffering by viewModel.playerManager.isBuffering.collectAsState()
    val currentPositionMs by viewModel.playerManager.currentPositionMs.collectAsState()
    val durationMs by viewModel.playerManager.durationMs.collectAsState()
    val repeatMode by viewModel.playerManager.repeatMode.collectAsState()
    val isShuffle by viewModel.playerManager.isShuffle.collectAsState()
    val queue by viewModel.playerManager.queue.collectAsState()

    val playlists by viewModel.playlists.collectAsState()
    val favorites by viewModel.favoriteSongs.collectAsState()
    val historyItems by viewModel.historyItems.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchFilter by viewModel.searchFilter.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    val isCurrentSongFavorite = currentSong?.let { viewModel.isFavorite(it.id) } ?: false

    BackHandler(enabled = isPlayerExpanded || selectedArtist != null || selectedAlbum != null || selectedPlaylist != null) {
        viewModel.navigateBack()
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    // Persistent Mini Player above bottom bar
                    MiniPlayer(
                        song = currentSong,
                        isPlaying = isPlaying,
                        isBuffering = isBuffering,
                        isFavorite = isCurrentSongFavorite,
                        currentPositionMs = currentPositionMs,
                        durationMs = durationMs,
                        onExpandClick = { viewModel.expandPlayer() },
                        onTogglePlayPause = { viewModel.togglePlayPause() },
                        onPlayNext = { viewModel.playNext() },
                        onToggleFavorite = { currentSong?.let { viewModel.toggleFavorite(it) } }
                    )

                    // Bottom Navigation Bar
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp,
                        modifier = Modifier.testTag("bottom_nav_bar")
                    ) {
                        // Home
                        NavigationBarItem(
                            selected = currentTab == ScreenTab.HOME && selectedArtist == null && selectedAlbum == null && selectedPlaylist == null,
                            onClick = { viewModel.selectTab(ScreenTab.HOME) },
                            icon = {
                                Icon(
                                    imageVector = if (currentTab == ScreenTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                                    contentDescription = "الرئيسية"
                                )
                            },
                            label = { Text("الرئيسية", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.testTag("nav_home")
                        )

                        // Explore
                        NavigationBarItem(
                            selected = currentTab == ScreenTab.EXPLORE && selectedArtist == null && selectedAlbum == null && selectedPlaylist == null,
                            onClick = { viewModel.selectTab(ScreenTab.EXPLORE) },
                            icon = {
                                Icon(
                                    imageVector = if (currentTab == ScreenTab.EXPLORE) Icons.Filled.Explore else Icons.Outlined.Explore,
                                    contentDescription = "استكشاف"
                                )
                            },
                            label = { Text("استكشاف", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.testTag("nav_explore")
                        )

                        // Search
                        NavigationBarItem(
                            selected = currentTab == ScreenTab.SEARCH && selectedArtist == null && selectedAlbum == null && selectedPlaylist == null,
                            onClick = { viewModel.selectTab(ScreenTab.SEARCH) },
                            icon = {
                                Icon(
                                    imageVector = if (currentTab == ScreenTab.SEARCH) Icons.Filled.Search else Icons.Outlined.Search,
                                    contentDescription = "بحث"
                                )
                            },
                            label = { Text("بحث", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.testTag("nav_search")
                        )

                        // Library
                        NavigationBarItem(
                            selected = currentTab == ScreenTab.LIBRARY && selectedArtist == null && selectedAlbum == null && selectedPlaylist == null,
                            onClick = { viewModel.selectTab(ScreenTab.LIBRARY) },
                            icon = {
                                Icon(
                                    imageVector = if (currentTab == ScreenTab.LIBRARY) Icons.Filled.LibraryMusic else Icons.Outlined.LibraryMusic,
                                    contentDescription = "المكتبة"
                                )
                            },
                            label = { Text("المكتبة", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.testTag("nav_library")
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Determine active Screen
                when {
                    selectedArtist != null -> {
                        ArtistDetailScreen(
                            artist = selectedArtist!!,
                            currentSong = currentSong,
                            isPlaying = isPlaying,
                            onBack = { viewModel.navigateBack() },
                            onSongClick = { viewModel.playSong(it) },
                            onPlayAll = { songs, shuffle -> viewModel.playAll(songs, shuffle) },
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            onAddToPlaylist = { viewModel.openAddToPlaylist(it) },
                            isFavorite = { viewModel.isFavorite(it) }
                        )
                    }
                    selectedAlbum != null -> {
                        AlbumDetailScreen(
                            album = selectedAlbum!!,
                            currentSong = currentSong,
                            isPlaying = isPlaying,
                            onBack = { viewModel.navigateBack() },
                            onSongClick = { viewModel.playSong(it) },
                            onPlayAll = { songs, shuffle -> viewModel.playAll(songs, shuffle) },
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            onAddToPlaylist = { viewModel.openAddToPlaylist(it) },
                            isFavorite = { viewModel.isFavorite(it) }
                        )
                    }
                    selectedPlaylist != null -> {
                        PlaylistDetailScreen(
                            playlist = selectedPlaylist!!,
                            currentSong = currentSong,
                            isPlaying = isPlaying,
                            onBack = { viewModel.navigateBack() },
                            onSongClick = { viewModel.playSong(it) },
                            onPlayAll = { songs, shuffle -> viewModel.playAll(songs, shuffle) },
                            onRemoveSong = { songId ->
                                selectedPlaylist?.let { viewModel.removeSongFromPlaylist(it.id, songId) }
                            },
                            onDeletePlaylist = {
                                selectedPlaylist?.let { viewModel.deletePlaylist(it.id) }
                            },
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            onAddToPlaylist = { viewModel.openAddToPlaylist(it) },
                            isFavorite = { viewModel.isFavorite(it) }
                        )
                    }
                    else -> {
                        when (currentTab) {
                            ScreenTab.HOME -> {
                                HomeScreen(
                                    currentSong = currentSong,
                                    isPlaying = isPlaying,
                                    historyItems = historyItems,
                                    playlists = playlists,
                                    isApiConnected = viewModel.isYouTubeApiConnected(),
                                    onApiConfigClick = { viewModel.openApiConfigDialog() },
                                    onSongClick = { viewModel.playSong(it) },
                                    onArtistClick = { viewModel.openArtist(it) },
                                    onAlbumClick = { viewModel.openAlbum(it) },
                                    onPlaylistClick = { viewModel.openPlaylist(it) },
                                    onSearchClick = { viewModel.selectTab(ScreenTab.SEARCH) },
                                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                                    onAddToPlaylist = { viewModel.openAddToPlaylist(it) },
                                    isFavorite = { viewModel.isFavorite(it) }
                                )
                            }
                            ScreenTab.EXPLORE -> {
                                ExploreScreen(
                                    currentSong = currentSong,
                                    isPlaying = isPlaying,
                                    onSongClick = { viewModel.playSong(it) },
                                    onCategoryClick = { category ->
                                        viewModel.selectTab(ScreenTab.SEARCH)
                                        viewModel.onSearchQueryChanged(category.title)
                                    },
                                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                                    onAddToPlaylist = { viewModel.openAddToPlaylist(it) },
                                    isFavorite = { viewModel.isFavorite(it) }
                                )
                            }
                            ScreenTab.SEARCH -> {
                                SearchScreen(
                                    query = searchQuery,
                                    filter = searchFilter,
                                    results = searchResults,
                                    isSearching = isSearching,
                                    currentSong = currentSong,
                                    isPlaying = isPlaying,
                                    onQueryChange = { viewModel.onSearchQueryChanged(it) },
                                    onFilterChange = { viewModel.setFilterChip(it) },
                                    onSongClick = { viewModel.playSong(it) },
                                    onArtistClick = { viewModel.openArtist(it) },
                                    onAlbumClick = { viewModel.openAlbum(it) },
                                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                                    onAddToPlaylist = { viewModel.openAddToPlaylist(it) },
                                    isFavorite = { viewModel.isFavorite(it) }
                                )
                            }
                            ScreenTab.LIBRARY -> {
                                LibraryScreen(
                                    currentSong = currentSong,
                                    isPlaying = isPlaying,
                                    playlists = playlists,
                                    favorites = favorites,
                                    history = historyItems,
                                    onCreatePlaylistClick = { viewModel.openCreatePlaylistDialog() },
                                    onPlaylistClick = { viewModel.openPlaylist(it) },
                                    onSongClick = { viewModel.playSong(it) },
                                    onPlayAllFavorites = {
                                        val favSongs = favorites.map { fav ->
                                            Song(
                                                id = fav.songId,
                                                title = fav.title,
                                                artist = fav.artist,
                                                album = fav.album,
                                                durationSeconds = fav.durationSeconds,
                                                coverUrl = fav.coverUrl,
                                                audioUrl = fav.audioUrl,
                                                genre = fav.genre
                                            )
                                        }
                                        viewModel.playAll(favSongs, false)
                                    },
                                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                                    onAddToPlaylist = { viewModel.openAddToPlaylist(it) },
                                    isFavorite = { viewModel.isFavorite(it) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Full Screen Player Modal (animated from bottom)
        AnimatedVisibility(
            visible = isPlayerExpanded && currentSong != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            PlayerFullScreen(
                song = currentSong,
                isPlaying = isPlaying,
                isBuffering = isBuffering,
                isFavorite = isCurrentSongFavorite,
                currentPositionMs = currentPositionMs,
                durationMs = durationMs,
                repeatMode = repeatMode,
                isShuffle = isShuffle,
                queue = queue,
                onDismiss = { viewModel.collapsePlayer() },
                onTogglePlayPause = { viewModel.togglePlayPause() },
                onPlayNext = { viewModel.playNext() },
                onPlayPrevious = { viewModel.playPrevious() },
                onSeekTo = { viewModel.seekTo(it) },
                onToggleFavorite = { currentSong?.let { viewModel.toggleFavorite(it) } },
                onToggleRepeat = { viewModel.toggleRepeat() },
                onToggleShuffle = { viewModel.toggleShuffle() },
                onAddToPlaylist = { currentSong?.let { viewModel.openAddToPlaylist(it) } },
                onPlaySongFromQueue = { viewModel.playSong(it, queue) }
            )
        }

        // Dialogs
        if (songForPlaylistDialog != null) {
            AddToPlaylistDialog(
                song = songForPlaylistDialog,
                playlists = playlists,
                onSelectPlaylist = { playlistId ->
                    songForPlaylistDialog?.let { viewModel.addSongToPlaylist(playlistId, it.id) }
                },
                onCreateNewPlaylistClick = {
                    viewModel.openCreatePlaylistDialog()
                },
                onDismiss = { viewModel.closeAddToPlaylist() }
            )
        }

        if (showCreatePlaylistDialog) {
            CreatePlaylistDialog(
                onDismiss = { viewModel.closeCreatePlaylistDialog() },
                onConfirm = { name, desc ->
                    viewModel.createPlaylist(name, desc)
                }
            )
        }

        if (showApiConfigDialog) {
            YouTubeApiDialog(
                currentApiKey = youtubeApiKey,
                isConnected = viewModel.isYouTubeApiConnected(),
                onDismiss = { viewModel.closeApiConfigDialog() },
                onSaveKey = { key -> viewModel.saveYouTubeApiKey(key) }
            )
        }
    }
}
