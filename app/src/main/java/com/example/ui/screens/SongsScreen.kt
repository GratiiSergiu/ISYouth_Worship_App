package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Program
import com.example.model.Screen
import com.example.model.Song
import com.example.theme.*
import com.example.ui.components.StatusBadge
import com.example.ui.dialogs.AddOrEditSongDialog
import com.example.ui.dialogs.ChordTranspositionDialog
import com.example.ui.dialogs.ManageCategoriesDialog
import com.example.viewmodel.WorshipViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongsScreen(
    viewModel: WorshipViewModel,
    modifier: Modifier = Modifier
) {
    val songs by viewModel.songs.collectAsState()
    val programs by viewModel.programs.collectAsState()
    val categories by viewModel.categories.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Toate") }
    var selectedSong by remember { mutableStateOf<Song?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingSong by remember { mutableStateOf<Song?>(null) }
    var songToDelete by remember { mutableStateOf<Song?>(null) }
    var showAddToProgramDialog by remember { mutableStateOf<Song?>(null) }
    var showCategoryManager by remember { mutableStateOf(false) }

    val filteredSongs = songs.filter { song ->
        val matchesQuery = searchQuery.isEmpty() ||
                song.title.contains(searchQuery, ignoreCase = true) ||
                song.artist.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            "Toate" -> true
            "Favorite" -> song.isFavorite
            "Repertoire", "Repertoriu" -> song.category.equals("Repertoriu", ignoreCase = true) || song.status == "repertoire"
            "În lucru" -> song.category.equals("În lucru", ignoreCase = true) || song.status == "learning"
            "Nou" -> song.category.equals("Nou", ignoreCase = true) || song.status == "new"
            else -> song.category.equals(selectedFilter, ignoreCase = true)
        }
        matchesQuery && matchesFilter
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("songs_screen"),
        containerColor = ISYBlack,
        contentWindowInsets = WindowInsets.statusBars,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = ISYCoral,
                contentColor = ISYWhite,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(bottom = 16.dp, end = 4.dp)
                    .testTag("add_song_fab")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Adaugă cântare")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Screen Title
            Text(
                text = "Repertoriu Cântări",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = ISYWhite,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
            )

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Caută după titlu sau artist...", color = ISYGray500) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Căutare",
                        tint = ISYGray300
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Șterge",
                                tint = ISYGray300
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ISYSurface,
                    unfocusedContainerColor = ISYSurface,
                    focusedBorderColor = ISYCoral,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = ISYWhite,
                    unfocusedTextColor = ISYWhite
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .testTag("search_song_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Chips
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(categories) { filter ->
                    val isSelected = selectedFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = filter },
                        label = { Text(text = filter) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ISYCoral,
                            selectedLabelColor = ISYWhite,
                            containerColor = ISYSurface,
                            labelColor = ISYGray300
                        ),
                        border = null,
                        shape = RoundedCornerShape(20.dp)
                    )
                }

                item {
                    AssistChip(
                        onClick = { showCategoryManager = true },
                        label = { Text("Categorii...", fontSize = 12.sp, color = ISYCoralLight) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Gestionare categorii",
                                tint = ISYCoralLight,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = ISYSurfaceElevated
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Count summary
            Text(
                text = "${filteredSongs.size} cântări găsite",
                fontSize = 12.sp,
                color = ISYGray300,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )

            // Songs List
            if (filteredSongs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, start = 20.dp, end = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = ISYSurface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("🎵", fontSize = 36.sp)
                            Text(
                                text = if (searchQuery.isNotEmpty()) "Nicio cântare găsită" else "Repertoriul este gol",
                                color = ISYWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = if (searchQuery.isNotEmpty())
                                    "Încearcă alt termen de căutare sau altă categorie."
                                else
                                    "Apasă pe butonul '+' de mai jos pentru a adăuga cântările tale reale (prin link web sau copy-paste).",
                                color = ISYGray300,
                                fontSize = 13.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            if (searchQuery.isEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { showAddDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = ISYCoral),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Adaugă Cântare Nouă")
                                }
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredSongs, key = { it.id }) { song ->
                        SongCardItem(
                            song = song,
                            onSongClick = { selectedSong = song },
                            onFavoriteClick = { viewModel.toggleFavorite(song.id) },
                            onAddToProgramClick = { showAddToProgramDialog = song },
                            onEditClick = { editingSong = song },
                            onDeleteClick = { songToDelete = song }
                        )
                    }
                }
            }
        }
    }

    // Song Detail BottomSheet
    selectedSong?.let { song ->
        SongDetailBottomSheet(
            song = song,
            onDismiss = { selectedSong = null },
            onFavoriteToggle = { viewModel.toggleFavorite(song.id) },
            onAddToProgram = {
                showAddToProgramDialog = song
                selectedSong = null
            },
            onEdit = {
                editingSong = song
                selectedSong = null
            },
            onDelete = {
                songToDelete = song
                selectedSong = null
            }
        )
    }

    // Add / Edit Song Dialog
    if (showAddDialog) {
        AddOrEditSongDialog(
            initialSong = null,
            categories = categories,
            onDismiss = { showAddDialog = false },
            onSave = { newSong ->
                viewModel.addSong(newSong)
                showAddDialog = false
            }
        )
    }

    editingSong?.let { song ->
        AddOrEditSongDialog(
            initialSong = song,
            categories = categories,
            onDismiss = { editingSong = null },
            onSave = { updatedSong ->
                viewModel.updateSong(updatedSong)
                editingSong = null
            }
        )
    }

    if (showCategoryManager) {
        ManageCategoriesDialog(
            viewModel = viewModel,
            onDismiss = { showCategoryManager = false }
        )
    }

    songToDelete?.let { song ->
        AlertDialog(
            onDismissRequest = { songToDelete = null },
            containerColor = ISYSurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = null,
                        tint = ISYError,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Șterge cântarea", color = ISYWhite, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        "Sigur dorești să elimini această piesă din repertoriu?",
                        color = ISYWhite,
                        fontSize = 14.sp
                    )
                    Text(
                        "\"${song.title}\" — ${song.artist}",
                        color = ISYCoralLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        "Piesa va fi eliminată individual din repertoriu și din setlist-uri.",
                        color = ISYGray300,
                        fontSize = 12.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteSong(song.id)
                        songToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ISYError)
                ) {
                    Text("Șterge", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { songToDelete = null }) {
                    Text("Anulează", color = ISYGray300)
                }
            }
        )
    }

    // Add To Program Dialog
    showAddToProgramDialog?.let { song ->
        AddToProgramDialog(
            song = song,
            programs = programs,
            onDismiss = { showAddToProgramDialog = null },
            onSelectProgram = { programId, segment ->
                viewModel.addSongToProgram(programId, song.id, segment)
                showAddToProgramDialog = null
            }
        )
    }
}

@Composable
private fun SongCardItem(
    song: Song,
    onSongClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onAddToProgramClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val durationMin = song.durationSeconds / 60
    val durationSec = song.durationSeconds % 60
    val durationFormatted = "%d:%02d".format(durationMin, durationSec)

    // Sanitize title, artist, key to ensure no newlines or unwanted spacing break card height
    val cleanTitle = song.title.replace("\r", "").replace("\n", " ").replace(Regex("\\s+"), " ").trim()
    val cleanArtist = song.artist.replace("\r", "").replace("\n", " ").replace(Regex("\\s+"), " ").trim()
    val cleanKey = song.key.replace("\r", "").replace("\n", "").trim()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSongClick)
            .testTag("song_item_${song.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ISYSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Key circle
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(ISYSurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = cleanKey.ifBlank { "Sol" },
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = ISYCoral,
                    maxLines = 1,
                    softWrap = false
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = cleanTitle.ifBlank { "Fără Titlu" },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = ISYWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
                Text(
                    text = cleanArtist.ifBlank { "Autor necunoscut" },
                    fontSize = 12.sp,
                    color = ISYGray300,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    StatusBadge(status = song.status)
                    Text(
                        text = "$durationFormatted • ${song.tempo} BPM",
                        fontSize = 10.sp,
                        color = ISYGray300,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = false
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Action buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (song.isFavorite) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Favorit",
                        tint = if (song.isFavorite) ISYWarning else ISYGray500,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onAddToProgramClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlaylistAdd,
                        contentDescription = "Adaugă în program",
                        tint = ISYCoralLight,
                        modifier = Modifier.size(19.dp)
                    )
                }

                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editează cântarea",
                        tint = ISYGray300,
                        modifier = Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("delete_song_${song.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Șterge cântarea",
                        tint = ISYError.copy(alpha = 0.85f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SongDetailBottomSheet(
    song: Song,
    onDismiss: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onAddToProgram: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var semitones by remember { mutableIntStateOf(0) }
    var isMusicianView by remember { mutableStateOf(false) }
    var showChordTranspositionDialog by remember { mutableStateOf(false) }

    val currentKey = remember(song.key, semitones) {
        WorshipViewModel.transposeChord(song.key, semitones)
    }

    val displayedContent = remember(song, semitones, isMusicianView) {
        if (isMusicianView && song.chords.isNotEmpty()) {
            WorshipViewModel.transposeText(song.chords, semitones)
        } else {
            song.lyrics
        }
    }

    if (showChordTranspositionDialog) {
        ChordTranspositionDialog(
            originalKey = song.key,
            currentKey = currentKey,
            onKeySelected = { _, delta ->
                semitones = delta
            },
            onDismiss = { showChordTranspositionDialog = false }
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = ISYSurface,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = ISYWhite
                    )
                    Text(
                        text = song.artist,
                        style = MaterialTheme.typography.bodyLarge,
                        color = ISYGray300
                    )
                }
                StatusBadge(status = song.status)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Metadata Grid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ISYSurfaceElevated)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showChordTranspositionDialog = true }
                        .padding(4.dp)
                ) {
                    MetaItem(label = "Tonalitate ▾", value = currentKey, color = ISYCoral)
                }
                MetaItem(label = "Tempo", value = "${song.tempo} BPM", color = ISYWhite)
                MetaItem(label = "Măsură", value = song.timeSignature, color = ISYWhite)
                MetaItem(label = "Folosit", value = "${song.usageCount} ori", color = ISYSuccess)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Band line-up
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ISYSurfaceElevated)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                MetaItem(label = "Lead Vocals", value = song.lead, color = ISYCoralLight)
                MetaItem(label = "Chitară", value = song.guitar, color = ISYWhite)
                MetaItem(label = "Tobe", value = song.drums, color = ISYWhite)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Transposition: Click on the chord to select directly from the list of all keys
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { showChordTranspositionDialog = true }
                    .testTag("chord_transposition_selector"),
                colors = CardDefaults.cardColors(containerColor = ISYBlack),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(ISYCoral.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentKey,
                                color = ISYCoral,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Column {
                            Text(
                                text = "Acord / Tonalitate Cântare (Apasă pentru listă)",
                                fontSize = 11.sp,
                                color = ISYGray300
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = currentKey,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ISYWhite
                                )
                                if (semitones != 0) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "(Original: ${song.key})",
                                        fontSize = 12.sp,
                                        color = ISYCoralLight
                                    )
                                }
                            }
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ISYCoral
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Alege Acord", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ISYWhite)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = ISYWhite, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // View Mode Toggle (Versuri vs Acorduri)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isMusicianView) "Vedere Muzician (Acorduri)" else "Vedere Versuri",
                    fontWeight = FontWeight.Bold,
                    color = ISYWhite
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Acorduri", fontSize = 12.sp, color = ISYGray300)
                    Spacer(modifier = Modifier.width(6.dp))
                    Switch(
                        checked = isMusicianView,
                        onCheckedChange = { isMusicianView = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ISYWhite,
                            checkedTrackColor = ISYCoral
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Lyrics / Chords Display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ISYBlack)
                    .padding(16.dp)
            ) {
                Text(
                    text = displayedContent.ifEmpty { "Nu există conținut disponibil pentru această cântare." },
                    fontFamily = if (isMusicianView) FontFamily.Monospace else FontFamily.Default,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = ISYWhite
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onAddToProgram,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = ISYCoral),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Adaugă în program")
                }

                OutlinedButton(
                    onClick = onFavoriteToggle,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ISYWhite),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = if (song.isFavorite) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = null,
                        tint = if (song.isFavorite) ISYWarning else ISYWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Edit and Delete Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ISYWhite),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Editează")
                }

                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ISYError),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = ISYError, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Șterge", color = ISYError)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun MetaItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 11.sp, color = ISYGray300)
        Spacer(modifier = Modifier.height(3.dp))
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun AddToProgramDialog(
    song: Song,
    programs: List<Program>,
    onDismiss: () -> Unit,
    onSelectProgram: (String, String) -> Unit
) {
    var selectedSegment by remember { mutableStateOf("worship") }
    val segments = listOf("intro", "worship", "special", "sermon", "sending")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ISYSurface,
        title = {
            Text(
                text = "Adaugă \"${song.title}\"",
                color = ISYWhite,
                fontSize = 18.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = "Alege segmentul:", color = ISYGray300, fontSize = 13.sp)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(segments) { seg ->
                        FilterChip(
                            selected = selectedSegment == seg,
                            onClick = { selectedSegment = seg },
                            label = { Text(seg.replaceFirstChar { it.uppercase() }) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ISYCoral,
                                selectedLabelColor = ISYWhite,
                                containerColor = ISYSurfaceElevated,
                                labelColor = ISYGray300
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Alege programul:", color = ISYGray300, fontSize = 13.sp)

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 240.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(programs) { prog ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectProgram(prog.id, selectedSegment) },
                            colors = CardDefaults.cardColors(containerColor = ISYSurfaceElevated),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = prog.title, fontWeight = FontWeight.Bold, color = ISYWhite)
                                    Text(text = prog.date, fontSize = 12.sp, color = ISYGray300)
                                }
                                StatusBadge(status = prog.status)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Închide", color = ISYGray300)
            }
        }
    )
}
