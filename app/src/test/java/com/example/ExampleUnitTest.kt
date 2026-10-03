package com.example

import com.example.data.MusicCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun musicCatalog_hasSongsAndArtists() {
        assertTrue(MusicCatalog.songs.isNotEmpty())
        assertTrue(MusicCatalog.artists.isNotEmpty())
        assertTrue(MusicCatalog.albums.isNotEmpty())

        val firstSong = MusicCatalog.songs.first()
        assertNotNull(firstSong.audioUrl)
        assertTrue(firstSong.durationSeconds > 0)
        assertTrue(firstSong.durationFormatted.contains(":"))
    }
}
