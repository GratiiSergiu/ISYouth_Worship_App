package com.example.model

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val key: String,
    val durationSeconds: Int,
    val tempo: Int = 70,
    val timeSignature: String = "4/4",
    val status: String = "repertoire", // 'repertoire' | 'learning' | 'new'
    val category: String = "Repertoriu",
    val usageCount: Int = 0,
    val lead: String = "-",
    val guitar: String = "-",
    val drums: String = "-",
    val lastUsed: String = "-",
    val isFavorite: Boolean = false,
    val lyrics: String = "",
    val chords: String = ""
)

data class SetlistItem(
    val id: String,
    val songId: String,
    val segment: String = "worship", // 'intro' | 'worship' | 'special' | 'sermon' | 'sending'
    val notes: String = ""
)

data class Program(
    val id: String,
    val title: String,
    val theme: String = "",
    val date: String,
    val status: String = "draft", // 'draft' | 'ready' | 'completed'
    val setlist: List<SetlistItem> = emptyList(),
    val tags: List<String> = emptyList(),
    val targetDurationMinutes: Int = 60,
    val notes: String = ""
)

data class Attendee(
    val name: String,
    val status: String, // 'confirmed' | 'maybe' | 'declined' | 'no-response'
    val avatar: String
)

data class AgendaItem(
    val song: String,
    val focus: String,
    val duration: Int
)

data class Rehearsal(
    val id: String,
    val programName: String,
    val date: String,
    val time: String,
    val location: String,
    val duration: Int,
    val status: String, // 'scheduled' | 'completed'
    val attendees: List<Attendee> = emptyList(),
    val agenda: List<AgendaItem> = emptyList(),
    val hasRecording: Boolean = false,
    val recordingUrl: String = ""
)

data class TeamMember(
    val id: String,
    val name: String,
    val role: String,
    val initial: String,
    val badge: String, // 'Admin' | 'Membru' | 'Tehnic'
    val primaryColor: Long,
    val secondaryColor: Long
)

data class NotificationItem(
    val id: String,
    val type: String,
    val title: String,
    val body: String,
    val time: String,
    val isRead: Boolean = false,
    val icon: String,
    val colorHex: Long,
    val action: String
)

data class SongStatProgram(
    val date: String,
    val program: String,
    val position: Int
)

data class SongStat(
    val title: String,
    val artist: String,
    val key: String,
    val playCount: Int,
    val lastPlayed: String,
    val firstPlayed: String,
    val programs: List<SongStatProgram> = emptyList(),
    val trend: String = "stable" // 'up' | 'stable' | 'down'
)

sealed interface Screen {
    data object Dashboard : Screen
    data object Songs : Screen
    data class SetlistBuilder(val programId: String? = null) : Screen
    data class LiveMode(val programId: String? = null) : Screen
    data object PracticeMode : Screen
    data object Calendar : Screen
    data object Statistics : Screen
    data object Team : Screen
    data object Notifications : Screen
    data object Profile : Screen
}
