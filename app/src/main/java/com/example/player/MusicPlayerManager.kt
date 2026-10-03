package com.example.player

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.PowerManager
import android.util.Log
import com.example.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.IOException

class MusicPlayerManager(private val context: Context) {

    private val tag = "MusicPlayerManager"
    private val scope = CoroutineScope(Dispatchers.Main)

    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: Job? = null

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isBuffering = MutableStateFlow(false)
    val isBuffering: StateFlow<Boolean> = _isBuffering.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0L)
    val currentPositionMs: StateFlow<Long> = _currentPositionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(1L)
    val durationMs: StateFlow<Long> = _durationMs.asStateFlow()

    private val _queue = MutableStateFlow<List<Song>>(emptyList())
    val queue: StateFlow<List<Song>> = _queue.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _repeatMode = MutableStateFlow(RepeatMode.ALL)
    val repeatMode: StateFlow<RepeatMode> = _repeatMode.asStateFlow()

    private val _isShuffle = MutableStateFlow(false)
    val isShuffle: StateFlow<Boolean> = _isShuffle.asStateFlow()

    private var onSongPlayedListener: ((Song) -> Unit)? = null

    fun setOnSongPlayedListener(listener: (Song) -> Unit) {
        onSongPlayedListener = listener
    }

    private fun initPlayerIfNeeded(): MediaPlayer {
        return mediaPlayer ?: MediaPlayer().apply {
            setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setOnPreparedListener { mp ->
                _isBuffering.value = false
                val dur = mp.duration.toLong().coerceAtLeast(1000L)
                _durationMs.value = dur
                mp.start()
                _isPlaying.value = true
                startProgressTicker()
            }
            setOnCompletionListener {
                handleSongCompletion()
            }
            setOnErrorListener { _, what, extra ->
                Log.e(tag, "MediaPlayer Error: what=$what, extra=$extra")
                _isBuffering.value = false
                _isPlaying.value = false
                stopProgressTicker()
                true
            }
            setOnBufferingUpdateListener { _, percent ->
                // Can track buffer percent if needed
            }
            mediaPlayer = this
        }
    }

    fun playSong(song: Song, newQueue: List<Song> = emptyList()) {
        if (newQueue.isNotEmpty()) {
            _queue.value = newQueue
            val index = newQueue.indexOfFirst { it.id == song.id }
            _currentIndex.value = if (index >= 0) index else 0
        } else if (!_queue.value.any { it.id == song.id }) {
            _queue.value = _queue.value + song
            _currentIndex.value = _queue.value.lastIndex
        } else {
            _currentIndex.value = _queue.value.indexOfFirst { it.id == song.id }
        }

        _currentSong.value = song
        _currentPositionMs.value = 0L
        _durationMs.value = (song.durationSeconds * 1000L).coerceAtLeast(1000L)
        _isBuffering.value = true

        onSongPlayedListener?.invoke(song)

        val player = initPlayerIfNeeded()
        try {
            player.reset()
            player.setDataSource(song.audioUrl)
            player.prepareAsync()
        } catch (e: IOException) {
            Log.e(tag, "Error setting data source: ${e.message}", e)
            _isBuffering.value = false
            _isPlaying.value = false
        }
    }

    fun togglePlayPause() {
        val player = mediaPlayer ?: return
        if (player.isPlaying) {
            pause()
        } else {
            resume()
        }
    }

    fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                _isPlaying.value = false
                stopProgressTicker()
            }
        }
    }

    fun resume() {
        mediaPlayer?.let {
            it.start()
            _isPlaying.value = true
            startProgressTicker()
        }
    }

    fun seekTo(positionMs: Long) {
        val pos = positionMs.coerceIn(0L, _durationMs.value)
        _currentPositionMs.value = pos
        mediaPlayer?.seekTo(pos.toInt())
    }

    fun playNext() {
        val currentQ = _queue.value
        if (currentQ.isEmpty()) return

        if (_isShuffle.value && currentQ.size > 1) {
            var randomIndex = (0 until currentQ.size).random()
            while (randomIndex == _currentIndex.value && currentQ.size > 1) {
                randomIndex = (0 until currentQ.size).random()
            }
            _currentIndex.value = randomIndex
            playSong(currentQ[randomIndex])
            return
        }

        val nextIndex = _currentIndex.value + 1
        if (nextIndex < currentQ.size) {
            _currentIndex.value = nextIndex
            playSong(currentQ[nextIndex])
        } else if (_repeatMode.value == RepeatMode.ALL) {
            _currentIndex.value = 0
            playSong(currentQ[0])
        } else {
            pause()
            seekTo(0L)
        }
    }

    fun playPrevious() {
        if (_currentPositionMs.value > 3000L) {
            seekTo(0L)
            return
        }

        val currentQ = _queue.value
        if (currentQ.isEmpty()) return

        val prevIndex = _currentIndex.value - 1
        if (prevIndex >= 0) {
            _currentIndex.value = prevIndex
            playSong(currentQ[prevIndex])
        } else if (_repeatMode.value == RepeatMode.ALL) {
            _currentIndex.value = currentQ.lastIndex
            playSong(currentQ[currentQ.lastIndex])
        } else {
            seekTo(0L)
        }
    }

    private fun handleSongCompletion() {
        when (_repeatMode.value) {
            RepeatMode.ONE -> {
                _currentSong.value?.let { playSong(it) }
            }
            RepeatMode.ALL -> {
                playNext()
            }
            RepeatMode.OFF -> {
                if (_currentIndex.value < _queue.value.lastIndex) {
                    playNext()
                } else {
                    _isPlaying.value = false
                    stopProgressTicker()
                    seekTo(0L)
                }
            }
        }
    }

    fun toggleRepeatMode() {
        _repeatMode.value = when (_repeatMode.value) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
    }

    fun toggleShuffle() {
        _isShuffle.value = !_isShuffle.value
    }

    fun addToQueue(song: Song) {
        if (!_queue.value.any { it.id == song.id }) {
            _queue.value = _queue.value + song
        }
    }

    private fun startProgressTicker() {
        stopProgressTicker()
        progressJob = scope.launch {
            while (isActive) {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        _currentPositionMs.value = it.currentPosition.toLong()
                    }
                }
                delay(250)
            }
        }
    }

    private fun stopProgressTicker() {
        progressJob?.cancel()
        progressJob = null
    }

    fun release() {
        stopProgressTicker()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
