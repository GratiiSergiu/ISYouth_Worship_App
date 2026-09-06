package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleData
import com.example.model.*
import kotlinx.coroutines.flow.*
import java.util.UUID

class WorshipViewModel : ViewModel() {

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Dashboard)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _backStack = MutableStateFlow<List<Screen>>(listOf(Screen.Dashboard))

    private val _categories = MutableStateFlow<List<String>>(
        listOf("Toate", "Repertoriu", "Închinare", "Lăudare", "Tineret", "În lucru", "Nou", "Favorite")
    )
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    private val _songs = MutableStateFlow<List<Song>>(
        SampleData.songs.map { song ->
            song.copy(
                title = song.title.replace("\r", "").replace("\n", " ").replace(Regex("\\s+"), " ").trim(),
                artist = song.artist.replace("\r", "").replace("\n", " ").replace(Regex("\\s+"), " ").trim(),
                key = song.key.replace("\r", "").replace("\n", "").trim()
            )
        }
    )
    val songs: StateFlow<List<Song>> = _songs.asStateFlow()

    private val _programs = MutableStateFlow<List<Program>>(SampleData.programs)
    val programs: StateFlow<List<Program>> = _programs.asStateFlow()

    private val _rehearsals = MutableStateFlow<List<Rehearsal>>(SampleData.rehearsals)
    val rehearsals: StateFlow<List<Rehearsal>> = _rehearsals.asStateFlow()

    private val _teamMembers = MutableStateFlow<List<TeamMember>>(SampleData.teamMembers)
    val teamMembers: StateFlow<List<TeamMember>> = _teamMembers.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(SampleData.notifications)
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    val songStats: StateFlow<List<SongStat>> = combine(_songs, _programs) { songsList, programsList ->
        songsList.map { song ->
            val matchingPrograms = programsList.filter { p -> p.setlist.any { it.songId == song.id } }
            val count = matchingPrograms.size + song.usageCount
            val statPrograms = matchingPrograms.mapIndexed { _, p ->
                val pos = p.setlist.indexOfFirst { it.songId == song.id } + 1
                SongStatProgram(date = p.date, program = p.title, position = pos)
            }
            SongStat(
                title = song.title,
                artist = song.artist,
                key = song.key,
                playCount = count,
                lastPlayed = matchingPrograms.maxByOrNull { it.date }?.date ?: song.lastUsed,
                firstPlayed = matchingPrograms.minByOrNull { it.date }?.date ?: "-",
                programs = statPrograms,
                trend = if (count >= 3) "up" else if (count > 0) "stable" else "down"
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    // Navigation methods
    fun navigateTo(screen: Screen) {
        _backStack.update { it + screen }
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        if (_backStack.value.size > 1) {
            val updated = _backStack.value.dropLast(1)
            _backStack.value = updated
            _currentScreen.value = updated.last()
            return true
        }
        return false
    }

    // Category methods
    fun addCategory(categoryName: String) {
        val trimmed = categoryName.trim()
        if (trimmed.isNotBlank() && !_categories.value.any { it.equals(trimmed, ignoreCase = true) }) {
            _categories.update { it + trimmed }
        }
    }

    fun updateCategory(oldName: String, newName: String) {
        val trimmed = newName.trim()
        if (trimmed.isBlank() || oldName == "Toate" || oldName == "Favorite") return
        _categories.update { list ->
            list.map { if (it == oldName) trimmed else it }
        }
        _songs.update { songsList ->
            songsList.map { if (it.category == oldName) it.copy(category = trimmed) else it }
        }
    }

    fun deleteCategory(categoryName: String) {
        if (categoryName == "Toate" || categoryName == "Favorite" || categoryName == "Repertoriu") return
        _categories.update { list -> list.filter { it != categoryName } }
        _songs.update { songsList ->
            songsList.map { if (it.category == categoryName) it.copy(category = "Repertoriu") else it }
        }
    }

    // Reset & Demo data management
    fun clearAllDemoData() {
        _songs.value = emptyList()
        _programs.value = emptyList()
        _rehearsals.value = emptyList()
        _teamMembers.value = emptyList()
        _categories.value = listOf("Toate", "Repertoriu", "Favorite")
    }

    fun resetToSampleData() {
        _songs.value = SampleData.songs
        _programs.value = SampleData.programs
        _rehearsals.value = SampleData.rehearsals
        _teamMembers.value = SampleData.teamMembers
        _categories.value = listOf("Toate", "Repertoriu", "Închinare", "Lăudare", "Tineret", "În lucru", "Nou", "Favorite")
    }

    // Song methods
    fun toggleFavorite(songId: String) {
        _songs.update { list ->
            list.map { if (it.id == songId) it.copy(isFavorite = !it.isFavorite) else it }
        }
    }

    fun addSong(song: Song) {
        _songs.update { it + song }
        _notifications.update { notifs ->
            listOf(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    type = "song_added",
                    title = "Cântare nouă adăugată",
                    body = "\"${song.title}\" (${song.artist}) a fost adăugată în repertoriu.",
                    time = "Acum",
                    isRead = false,
                    icon = "🎵",
                    colorHex = 0xFFF59E0B,
                    action = "songs"
                )
            ) + notifs
        }
    }

    fun updateSong(updated: Song) {
        _songs.update { list ->
            list.map { if (it.id == updated.id) updated else it }
        }
    }

    fun deleteSong(songId: String) {
        _songs.update { list -> list.filter { it.id != songId } }
        _programs.update { progs ->
            progs.map { p ->
                p.copy(setlist = p.setlist.filter { it.songId != songId })
            }
        }
    }

    fun getSongById(id: String): Song? {
        return _songs.value.firstOrNull { it.id == id }
    }

    // Program methods
    fun addProgram(program: Program) {
        _programs.update { it + program }
        _notifications.update { notifs ->
            listOf(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    type = "program_updated",
                    title = "Program nou creat",
                    body = "\"${program.title}\" a fost creat pentru data de ${program.date}.",
                    time = "Acum",
                    isRead = false,
                    icon = "📝",
                    colorHex = 0xFFE85D5D,
                    action = "builder"
                )
            ) + notifs
        }
    }

    fun updateProgram(updated: Program) {
        _programs.update { list ->
            list.map { if (it.id == updated.id) updated else it }
        }
    }

    fun deleteProgram(programId: String) {
        _programs.update { list -> list.filter { it.id != programId } }
    }

    fun getProgramById(id: String): Program? {
        return _programs.value.firstOrNull { it.id == id }
    }

    fun addSongToProgram(programId: String, songId: String, segment: String = "worship") {
        _programs.update { list ->
            list.map { program ->
                if (program.id == programId) {
                    val newItem = SetlistItem(
                        id = "si_${System.currentTimeMillis()}",
                        songId = songId,
                        segment = segment
                    )
                    program.copy(setlist = program.setlist + newItem)
                } else program
            }
        }
    }

    fun removeSongFromProgram(programId: String, itemId: String) {
        _programs.update { list ->
            list.map { program ->
                if (program.id == programId) {
                    program.copy(setlist = program.setlist.filter { it.id != itemId })
                } else program
            }
        }
    }

    fun moveSongInProgram(programId: String, fromIndex: Int, toIndex: Int) {
        _programs.update { list ->
            list.map { program ->
                if (program.id == programId) {
                    val mutable = program.setlist.toMutableList()
                    if (fromIndex in mutable.indices && toIndex in mutable.indices) {
                        val item = mutable.removeAt(fromIndex)
                        mutable.add(toIndex, item)
                        program.copy(setlist = mutable)
                    } else program
                } else program
            }
        }
    }

    // Rehearsals
    fun addRehearsal(rehearsal: Rehearsal) {
        _rehearsals.update { it + rehearsal }
        _notifications.update { notifs ->
            listOf(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    type = "rehearsal_scheduled",
                    title = "Repetiție programată",
                    body = "Repetiție pentru \"${rehearsal.programName}\" pe ${rehearsal.date} la ${rehearsal.time}.",
                    time = "Acum",
                    isRead = false,
                    icon = "🎸",
                    colorHex = 0xFF4A3B6B,
                    action = "practice"
                )
            ) + notifs
        }
    }

    fun updateRehearsal(updated: Rehearsal) {
        _rehearsals.update { list ->
            list.map { if (it.id == updated.id) updated else it }
        }
    }

    fun deleteRehearsal(rehearsalId: String) {
        _rehearsals.update { list -> list.filter { it.id != rehearsalId } }
    }

    fun getRehearsalById(id: String): Rehearsal? {
        return _rehearsals.value.firstOrNull { it.id == id }
    }

    fun updateAttendeeStatus(rehearsalId: String, attendeeName: String, newStatus: String) {
        _rehearsals.update { list ->
            list.map { reh ->
                if (reh.id == rehearsalId) {
                    val updatedAttendees = reh.attendees.map { att ->
                        if (att.name == attendeeName) att.copy(status = newStatus) else att
                    }
                    reh.copy(attendees = updatedAttendees)
                } else reh
            }
        }
    }

    // Team
    fun addTeamMember(member: TeamMember) {
        _teamMembers.update { it + member }
    }

    fun updateTeamMember(updated: TeamMember) {
        _teamMembers.update { list ->
            list.map { if (it.id == updated.id) updated else it }
        }
    }

    fun deleteTeamMember(memberId: String) {
        _teamMembers.update { list -> list.filter { it.id != memberId } }
    }

    fun getTeamMemberById(id: String): TeamMember? {
        return _teamMembers.value.firstOrNull { it.id == id }
    }

    // Notifications
    fun markNotificationAsRead(id: String) {
        _notifications.update { list ->
            list.map { if (it.id == id) it.copy(isRead = true) else it }
        }
    }

    fun markAllNotificationsAsRead() {
        _notifications.update { list ->
            list.map { it.copy(isRead = true) }
        }
    }

    // Music transposer helper
    companion object {
        val chromaticScale = com.example.util.MusicTransposer.chromaticRomanian

        fun transposeChord(chord: String, semitones: Int): String {
            return com.example.util.MusicTransposer.transposeSingleChord(chord, semitones)
        }

        fun transposeText(text: String, semitones: Int): String {
            return com.example.util.MusicTransposer.transposeTextContent(text, semitones)
        }
    }
}
