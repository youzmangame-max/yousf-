package com.example.data

import com.example.model.Album
import com.example.model.Artist
import com.example.model.MusicCategory
import com.example.model.Song

object MusicCatalog {

    val categories = listOf(
        MusicCategory("cat_arabic_tarab", "طرب شرقي", "music_note", listOf(0xFF8A2387, 0xFFE94057)),
        MusicCategory("cat_pop_hits", "بوب عربي وعالمي", "whatshot", listOf(0xFFFF416C, 0xFFFF4B2B)),
        MusicCategory("cat_chill", "هدوء وتركيز", "spa", listOf(0xFF2193b0, 0xFF6dd5ed)),
        MusicCategory("cat_workout", "طاقة وتمرين", "fitness_center", listOf(0xFF11998e, 0xFF38ef7d)),
        MusicCategory("cat_rap", "راب وهيب هوب", "mic", listOf(0xFF7F00FF, 0xFFE100FF)),
        MusicCategory("cat_acoustic", "أكوستيك وجلسات", "graphic_eq", listOf(0xFFF7971E, 0xFFFFD200))
    )

    val artists = listOf(
        Artist(
            id = "art_amr_diab",
            name = "عمرو دياب",
            imageUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=600&auto=format&fit=crop&q=80",
            monthlyListeners = "8.4M مستمع شهرياً",
            bio = "الهضبة، أحد أبرز نجوم الموسيقى العربية والموسيقى المتوسطية في العالم العربي، حاصل على جوائز الموسيقى العالمية.",
            genre = "بوب عربي",
            songIds = listOf("song_1", "song_2", "song_11"),
            albumIds = listOf("alb_ya_ana_ya_la")
        ),
        Artist(
            id = "art_nancy",
            name = "نانسي عجرم",
            imageUrl = "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=600&auto=format&fit=crop&q=80",
            monthlyListeners = "6.9M مستمع شهرياً",
            bio = "نجمة البوب اللبنانية المحبوبة وصاحبة الأغاني الأكثر انتشاراً في الشرق الأوسط وشمال أفريقيا.",
            genre = "بوب",
            songIds = listOf("song_3", "song_4"),
            albumIds = listOf("alb_nancy_10")
        ),
        Artist(
            id = "art_sherine",
            name = "شيرين عبد الوهاب",
            imageUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=600&auto=format&fit=crop&q=80",
            monthlyListeners = "7.2M مستمع شهرياً",
            bio = "صوت مصر الدافئ، تتميز بالأغاني العاطفية والطرب المعاصر وإحساسها الاستثنائي.",
            genre = "طرب معاصر",
            songIds = listOf("song_5", "song_6"),
            albumIds = listOf("alb_nassay")
        ),
        Artist(
            id = "art_the_weeknd",
            name = "The Weeknd",
            imageUrl = "https://images.unsplash.com/photo-1501386761578-eac5c94b800a?w=600&auto=format&fit=crop&q=80",
            monthlyListeners = "105M مستمع شهرياً",
            bio = "أيقونة موسيقى الآر أند بي والسينث بوب الكندية العالمية، وحامل الأرقام القياسية على منصات البث.",
            genre = "R&B / Synthwave",
            songIds = listOf("song_7", "song_8", "song_12"),
            albumIds = listOf("alb_after_hours")
        ),
        Artist(
            id = "art_coldplay",
            name = "Coldplay",
            imageUrl = "https://images.unsplash.com/photo-1465847899084-d164df4dedc6?w=600&auto=format&fit=crop&q=80",
            monthlyListeners = "82M مستمع شهرياً",
            bio = "الفرقة البريطانية الأسطورية المتألقة في موسيقى الروك البديل والبوب الحماسي المليء بالألوان.",
            genre = "Alternative Rock",
            songIds = listOf("song_9", "song_10"),
            albumIds = listOf("alb_music_spheres")
        ),
        Artist(
            id = "art_hamaki",
            name = "محمد حماقي",
            imageUrl = "https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=600&auto=format&fit=crop&q=80",
            monthlyListeners = "5.8M مستمع شهرياً",
            bio = "نجم البوب المصري المعاصر، يشتهر بإيقاعاته الحديثة وألحانه الشبابية المبتكرة.",
            genre = "بوب مصري",
            songIds = listOf("song_13", "song_14"),
            albumIds = listOf("alb_ya_fatenny")
        )
    )

    val albums = listOf(
        Album(
            id = "alb_ya_ana_ya_la",
            title = "يا أنا يا لأ",
            artist = "عمرو دياب",
            coverUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=600&auto=format&fit=crop&q=80",
            year = "2021",
            genre = "بوب عربي",
            songIds = listOf("song_1", "song_2")
        ),
        Album(
            id = "alb_nancy_10",
            title = "نانسي 10",
            artist = "نانسي عجرم",
            coverUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=600&auto=format&fit=crop&q=80",
            year = "2021",
            genre = "بوب",
            songIds = listOf("song_3", "song_4")
        ),
        Album(
            id = "alb_after_hours",
            title = "After Hours",
            artist = "The Weeknd",
            coverUrl = "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?w=600&auto=format&fit=crop&q=80",
            year = "2020",
            genre = "Synth Pop",
            songIds = listOf("song_7", "song_8")
        ),
        Album(
            id = "alb_music_spheres",
            title = "Music of the Spheres",
            artist = "Coldplay",
            coverUrl = "https://images.unsplash.com/photo-1459749411175-04bf5292ceea?w=600&auto=format&fit=crop&q=80",
            year = "2021",
            genre = "Alternative",
            songIds = listOf("song_9", "song_10")
        )
    )

    val songs = listOf(
        Song(
            id = "song_1",
            title = "يا أنا يا لأ",
            artist = "عمرو دياب",
            album = "يا أنا يا لأ",
            durationSeconds = 245,
            coverUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=600&auto=format&fit=crop&q=80",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
            genre = "بوب عربي",
            youtubeId = "X2_3H5Z3o0Q",
            plays = "45.2M"
        ),
        Song(
            id = "song_2",
            title = "أماكن السهر",
            artist = "عمرو دياب",
            album = "يا أنا يا لأ",
            durationSeconds = 210,
            coverUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=600&auto=format&fit=crop&q=80",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
            genre = "بوب عربي",
            youtubeId = "k1cQ45z3k0L",
            plays = "38.6M"
        ),
        Song(
            id = "song_3",
            title = "سلامات سلامات",
            artist = "نانسي عجرم",
            album = "نانسي 10",
            durationSeconds = 195,
            coverUrl = "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=600&auto=format&fit=crop&q=80",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
            genre = "بوب",
            youtubeId = "P98df5b3k7K",
            plays = "29.4M"
        ),
        Song(
            id = "song_4",
            title = "صح صح",
            artist = "نانسي عجرم & مارشميلو",
            album = "نانسي 10",
            durationSeconds = 218,
            coverUrl = "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=600&auto=format&fit=crop&q=80",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
            genre = "EDM بوب",
            youtubeId = "Qw4rtY76bVz",
            plays = "51.1M"
        ),
        Song(
            id = "song_5",
            title = "مشاعر",
            artist = "شيرين عبد الوهاب",
            album = "طريقي",
            durationSeconds = 280,
            coverUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=600&auto=format&fit=crop&q=80",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3",
            genre = "طرب",
            youtubeId = "Zx8kL2mn09A",
            plays = "67.8M"
        ),
        Song(
            id = "song_6",
            title = "يا بتفكر يا بتحس",
            artist = "شيرين عبد الوهاب",
            album = "نساي",
            durationSeconds = 232,
            coverUrl = "https://images.unsplash.com/photo-1487180144351-b8472da7d491?w=600&auto=format&fit=crop&q=80",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3",
            genre = "طرب",
            youtubeId = "Bn5mK11xL0Q",
            plays = "42.0M"
        ),
        Song(
            id = "song_7",
            title = "Blinding Lights",
            artist = "The Weeknd",
            album = "After Hours",
            durationSeconds = 200,
            coverUrl = "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?w=600&auto=format&fit=crop&q=80",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3",
            genre = "Synthwave",
            youtubeId = "4NRXx6U8ABQ",
            plays = "3.8B"
        ),
        Song(
            id = "song_8",
            title = "Save Your Tears",
            artist = "The Weeknd",
            album = "After Hours",
            durationSeconds = 215,
            coverUrl = "https://images.unsplash.com/photo-1518609878373-06d740f60d8b?w=600&auto=format&fit=crop&q=80",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3",
            genre = "Pop",
            youtubeId = "XXYlFuWEuKI",
            plays = "2.1B"
        ),
        Song(
            id = "song_9",
            title = "Viva La Vida",
            artist = "Coldplay",
            album = "Viva la Vida",
            durationSeconds = 242,
            coverUrl = "https://images.unsplash.com/photo-1459749411175-04bf5292ceea?w=600&auto=format&fit=crop&q=80",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-9.mp3",
            genre = "Alternative",
            youtubeId = "dvgZkm1xWPE",
            plays = "1.9B"
        ),
        Song(
            id = "song_10",
            title = "My Universe",
            artist = "Coldplay & BTS",
            album = "Music of the Spheres",
            durationSeconds = 228,
            coverUrl = "https://images.unsplash.com/photo-1465847899084-d164df4dedc6?w=600&auto=format&fit=crop&q=80",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-10.mp3",
            genre = "Pop / Rock",
            youtubeId = "3YqPKLZF_WU",
            plays = "1.4B"
        ),
        Song(
            id = "song_11",
            title = "بحبه",
            artist = "عمرو دياب",
            album = "سهران",
            durationSeconds = 220,
            coverUrl = "https://images.unsplash.com/photo-1501386761578-eac5c94b800a?w=600&auto=format&fit=crop&q=80",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-11.mp3",
            genre = "بوب عربي",
            youtubeId = "Op8KmnX94hY",
            plays = "22.5M"
        ),
        Song(
            id = "song_12",
            title = "Starboy",
            artist = "The Weeknd & Daft Punk",
            album = "Starboy",
            durationSeconds = 230,
            coverUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=600&auto=format&fit=crop&q=80",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-12.mp3",
            genre = "R&B",
            youtubeId = "34Na4j8AVgA",
            plays = "2.9B"
        ),
        Song(
            id = "song_13",
            title = "لا ملامة",
            artist = "محمد حماقي",
            album = "يا فاتني",
            durationSeconds = 212,
            coverUrl = "https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=600&auto=format&fit=crop&q=80",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-13.mp3",
            genre = "بوب مصري",
            youtubeId = "Kl9Jk2n4L5P",
            plays = "31.2M"
        ),
        Song(
            id = "song_14",
            title = "لو هتسيب",
            artist = "محمد حماقي",
            album = "يا فاتني",
            durationSeconds = 240,
            coverUrl = "https://images.unsplash.com/photo-1518609878373-06d740f60d8b?w=600&auto=format&fit=crop&q=80",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-14.mp3",
            genre = "بوب مصري",
            youtubeId = "Ty7K0l9M321",
            plays = "48.7M"
        ),
        Song(
            id = "song_15",
            title = "نسم علينا الهوى",
            artist = "فيروز",
            album = "أغاني الخالدة",
            durationSeconds = 255,
            coverUrl = "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?w=600&auto=format&fit=crop&q=80",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-15.mp3",
            genre = "طرب أصيل",
            youtubeId = "AbCdEfGh123",
            plays = "120M"
        ),
        Song(
            id = "song_16",
            title = "Midnight City",
            artist = "M83",
            album = "Hurry Up, We're Dreaming",
            durationSeconds = 243,
            coverUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=600&auto=format&fit=crop&q=80",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-16.mp3",
            genre = "Electro Pop",
            youtubeId = "dX3k_PDnzHE",
            plays = "890M"
        )
    )

    fun getSongById(id: String): Song? = songs.find { it.id == id }

    fun getSongsByIds(ids: List<String>): List<Song> {
        val map = songs.associateBy { it.id }
        return ids.mapNotNull { map[it] }
    }

    fun getSongsByArtist(artistName: String): List<Song> {
        return songs.filter { it.artist.contains(artistName, ignoreCase = true) }
    }

    fun getSongsByAlbum(albumTitle: String): List<Song> {
        return songs.filter { it.album.equals(albumTitle, ignoreCase = true) }
    }

    fun getRecommendedSongs(): List<Song> = songs.shuffled().take(8)

    fun getTopCharts(): List<Song> = songs.sortedByDescending { it.durationSeconds }

    fun getSimilarSongs(song: Song): List<Song> {
        return songs.filter { it.id != song.id && (it.genre == song.genre || it.artist == song.artist) }
            .ifEmpty { songs.filter { it.id != song.id }.take(5) }
    }
}
