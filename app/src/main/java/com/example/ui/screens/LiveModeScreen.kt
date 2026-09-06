package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Program
import com.example.model.Song
import com.example.theme.*
import com.example.ui.dialogs.ChordTranspositionDialog
import com.example.viewmodel.WorshipViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveModeScreen(
    programId: String?,
    viewModel: WorshipViewModel,
    modifier: Modifier = Modifier
) {
    val programs by viewModel.programs.collectAsState()
    val allSongs by viewModel.songs.collectAsState()

    val program = remember(programId, programs) {
        programId?.let { id -> programs.firstOrNull { it.id == id } }
            ?: programs.firstOrNull { it.status == "ready" }
            ?: programs.firstOrNull()
    }

    val setlistSongs = remember(program, allSongs) {
        program?.setlist?.mapNotNull { item ->
            allSongs.firstOrNull { it.id == item.songId }
        } ?: allSongs.take(3)
    }

    var currentSongIndex by remember { mutableIntStateOf(0) }
    var isMusicianView by remember { mutableStateOf(false) }
    var fontSizeSp by remember { mutableIntStateOf(18) }
    var semitoneShift by remember { mutableIntStateOf(0) }
    var showChordTranspositionDialog by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(true) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }

    // Timer loop for stage clock
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(1000)
            elapsedSeconds += 1
        }
    }

    val currentSong: Song? = setlistSongs.getOrNull(currentSongIndex)

    val currentKey = remember(currentSong, semitoneShift) {
        currentSong?.let { WorshipViewModel.transposeChord(it.key, semitoneShift) } ?: "-"
    }

    val displayedContent = remember(currentSong, isMusicianView, semitoneShift) {
        if (currentSong == null) "Nicio cântare în setlist"
        else if (isMusicianView && currentSong.chords.isNotEmpty()) {
            WorshipViewModel.transposeText(currentSong.chords, semitoneShift)
        } else {
            currentSong.lyrics
        }
    }

    if (showChordTranspositionDialog && currentSong != null) {
        ChordTranspositionDialog(
            originalKey = currentSong.key,
            currentKey = currentKey,
            onKeySelected = { _, delta ->
                semitoneShift = delta
            },
            onDismiss = { showChordTranspositionDialog = false }
        )
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("live_mode_screen"),
        containerColor = ISYBlack,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = program?.title ?: "Live Mode",
                            color = ISYWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Cântarea ${currentSongIndex + 1} din ${setlistSongs.size}",
                            fontSize = 12.sp,
                            color = ISYCoralLight
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Ieșire",
                            tint = ISYWhite
                        )
                    }
                },
                actions = {
                    // Zoom controls
                    IconButton(onClick = { if (fontSizeSp > 14) fontSizeSp -= 2 }) {
                        Text("A-", color = ISYGray300, fontWeight = FontWeight.Bold)
                    }
                    IconButton(onClick = { if (fontSizeSp < 32) fontSizeSp += 2 }) {
                        Text("A+", color = ISYWhite, fontWeight = FontWeight.Bold)
                    }
                    // Musician view toggle
                    FilterChip(
                        selected = isMusicianView,
                        onClick = { isMusicianView = !isMusicianView },
                        label = { Text(if (isMusicianView) "🎸 Acorduri" else "🎤 Versuri") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ISYCoral,
                            selectedLabelColor = ISYWhite,
                            containerColor = ISYSurface,
                            labelColor = ISYGray300
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ISYBlack)
            )
        },
        bottomBar = {
            // Live Mode Controls Bar
            Card(
                colors = CardDefaults.cardColors(containerColor = ISYSurface),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    // Setlist pills navigation
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(setlistSongs) { idx, song ->
                            val isCurrent = idx == currentSongIndex
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isCurrent) ISYCoral else ISYSurfaceElevated)
                                    .clickable {
                                        currentSongIndex = idx
                                        semitoneShift = 0
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${idx + 1}. ${song.title.take(14)}...",
                                    fontSize = 12.sp,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isCurrent) ISYWhite else ISYGray300
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Player buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Previous button
                        IconButton(
                            onClick = {
                                if (currentSongIndex > 0) {
                                    currentSongIndex -= 1
                                    semitoneShift = 0
                                }
                            },
                            enabled = currentSongIndex > 0
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipPrevious,
                                contentDescription = "Precedenta",
                                tint = if (currentSongIndex > 0) ISYWhite else ISYGray500,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        // Play/Pause timer
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = { isPlaying = !isPlaying },
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(ISYCoral)
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Pauză",
                                    tint = ISYWhite
                                )
                            }
                            val min = elapsedSeconds / 60
                            val sec = elapsedSeconds % 60
                            Text(
                                text = "%02d:%02d".format(min, sec),
                                color = ISYWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        // Next button
                        IconButton(
                            onClick = {
                                if (currentSongIndex < setlistSongs.size - 1) {
                                    currentSongIndex += 1
                                    semitoneShift = 0
                                }
                            },
                            enabled = currentSongIndex < setlistSongs.size - 1
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipNext,
                                contentDescription = "Următoarea",
                                tint = if (currentSongIndex < setlistSongs.size - 1) ISYWhite else ISYGray500,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Current Song Header Banner
            if (currentSong != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ISYSurface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentSong.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = ISYWhite
                            )
                            Text(
                                text = "${currentSong.artist} • ${currentSong.tempo} BPM",
                                fontSize = 13.sp,
                                color = ISYGray300
                            )
                        }

                        // Chord Key Selector Pill (Click to open list of all transposition options)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(ISYCoral.copy(alpha = 0.2f))
                                .clickable { showChordTranspositionDialog = true }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("live_chord_selector")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = currentKey,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ISYCoralLight
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Alege acordul",
                                    tint = ISYCoralLight,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Lyrics / Chords Display
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = displayedContent,
                    fontSize = fontSizeSp.sp,
                    lineHeight = (fontSizeSp * 1.5).sp,
                    fontFamily = if (isMusicianView) FontFamily.Monospace else FontFamily.Default,
                    color = ISYWhite
                )
            }
        }
    }
}
