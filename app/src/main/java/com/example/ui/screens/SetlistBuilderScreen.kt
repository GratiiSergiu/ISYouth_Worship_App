package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Program
import com.example.model.Screen
import com.example.model.SetlistItem
import com.example.model.Song
import com.example.theme.*
import com.example.ui.components.SegmentBadge
import com.example.ui.components.StatusBadge
import com.example.viewmodel.WorshipViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetlistBuilderScreen(
    programId: String?,
    viewModel: WorshipViewModel,
    modifier: Modifier = Modifier
) {
    val programs by viewModel.programs.collectAsState()
    val allSongs by viewModel.songs.collectAsState()

    val initialProgram = remember(programId, programs) {
        programId?.let { id -> programs.firstOrNull { it.id == id } }
    }

    var title by remember(initialProgram) { mutableStateOf(initialProgram?.title ?: "Program Nou") }
    var theme by remember(initialProgram) { mutableStateOf(initialProgram?.theme ?: "") }
    var date by remember(initialProgram) { mutableStateOf(initialProgram?.date ?: "25 Mai 2025") }
    var targetDuration by remember(initialProgram) { mutableIntStateOf(initialProgram?.targetDurationMinutes ?: 60) }
    var selectedTags by remember(initialProgram) { mutableStateOf(initialProgram?.tags?.toSet() ?: setOf("#tineret", "#adorare")) }
    var status by remember(initialProgram) { mutableStateOf(initialProgram?.status ?: "draft") }
    var notes by remember(initialProgram) { mutableStateOf(initialProgram?.notes ?: "") }

    var setlistItems by remember(initialProgram) {
        mutableStateOf(initialProgram?.setlist ?: emptyList())
    }

    var showAddSongDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    val availableTags = listOf(
        "#tineret", "#adorare", "#predică", "#mărturie",
        "#botez", "#comuniune", "#rugăciune", "#laude"
    )

    val totalDurationSec = setlistItems.fold(0) { sum, item ->
        val song = allSongs.firstOrNull { it.id == item.songId }
        sum + (song?.durationSeconds ?: 0)
    }
    val totalDurationMin = (totalDurationSec / 60).coerceAtLeast(1)
    val isOverTarget = totalDurationMin > targetDuration

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("setlist_builder_screen"),
        containerColor = ISYBlack,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (initialProgram != null) "Editare Program" else "Setlist Nou",
                        color = ISYWhite,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Înapoi",
                            tint = ISYWhite
                        )
                    }
                },
                actions = {
                    if (initialProgram != null) {
                        IconButton(onClick = { showDeleteConfirmDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Șterge program",
                                tint = ISYError
                            )
                        }
                    }
                    Button(
                        onClick = {
                            val prog = Program(
                                id = initialProgram?.id ?: "prog_${System.currentTimeMillis()}",
                                title = title.ifBlank { "Program Nou" },
                                theme = theme,
                                date = date,
                                status = status,
                                setlist = setlistItems,
                                tags = selectedTags.toList(),
                                targetDurationMinutes = targetDuration,
                                notes = notes
                            )
                            if (initialProgram != null) {
                                viewModel.updateProgram(prog)
                            } else {
                                viewModel.addProgram(prog)
                            }
                            viewModel.navigateBack()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ISYCoral),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Salvează", fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ISYBlack)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Program Details Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ISYSurface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Titlu program") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = ISYWhite,
                                unfocusedTextColor = ISYWhite,
                                focusedBorderColor = ISYCoral
                            )
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = theme,
                                onValueChange = { theme = it },
                                label = { Text("Tematică") },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = ISYWhite,
                                    unfocusedTextColor = ISYWhite
                                )
                            )

                            OutlinedTextField(
                                value = date,
                                onValueChange = { date = it },
                                label = { Text("Data") },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = ISYWhite,
                                    unfocusedTextColor = ISYWhite
                                )
                            )
                        }

                        // Target duration & Status
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Durată țintă: $targetDuration min",
                                color = ISYWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(45, 60, 90).forEach { dur ->
                                    FilterChip(
                                        selected = targetDuration == dur,
                                        onClick = { targetDuration = dur },
                                        label = { Text("${dur}m") },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = ISYCoral,
                                            selectedLabelColor = ISYWhite,
                                            containerColor = ISYSurfaceElevated,
                                            labelColor = ISYGray300
                                        )
                                    )
                                }
                            }
                        }

                        // Status selection
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "Status:", color = ISYGray300, fontSize = 13.sp)
                            listOf("draft" to "Draft", "ready" to "Programat", "completed" to "Completat").forEach { (code, label) ->
                                FilterChip(
                                    selected = status == code,
                                    onClick = { status = code },
                                    label = { Text(label) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = when(code) {
                                            "ready" -> ISYCoral
                                            "completed" -> ISYSuccess
                                            else -> ISYWarning
                                        },
                                        selectedLabelColor = ISYWhite,
                                        containerColor = ISYSurfaceElevated,
                                        labelColor = ISYGray300
                                    )
                                )
                            }
                        }

                        // Tags
                        Column {
                            Text(text = "Etichete:", color = ISYGray300, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(availableTags) { tag ->
                                    val isSelected = selectedTags.contains(tag)
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = {
                                            selectedTags = if (isSelected) selectedTags - tag else selectedTags + tag
                                        },
                                        label = { Text(tag) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = ISYIndigo,
                                            selectedLabelColor = ISYWhite,
                                            containerColor = ISYSurfaceElevated,
                                            labelColor = ISYGray300
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Duration Progress Bar & Status Warning
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isOverTarget) ISYWarning.copy(alpha = 0.15f) else ISYSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Durată calculată: $totalDurationMin min",
                                fontWeight = FontWeight.Bold,
                                color = if (isOverTarget) ISYWarning else ISYWhite
                            )
                            Text(
                                text = "Țintă: $targetDuration min",
                                color = ISYGray300
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        val progress = (totalDurationMin.toFloat() / targetDuration).coerceIn(0f, 1f)
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (isOverTarget) ISYWarning else ISYCoral,
                            trackColor = ISYSurfaceElevated
                        )
                        if (isOverTarget) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "⚠️ Depășește durata estimată cu ${totalDurationMin - targetDuration} min",
                                fontSize = 12.sp,
                                color = ISYWarning
                            )
                        }
                    }
                }
            }

            // Setlist items header & Add Button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cântări în Setlist (${setlistItems.size})",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ISYWhite
                    )

                    Button(
                        onClick = { showAddSongDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = ISYCoral),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Adaugă")
                    }
                }
            }

            // Setlist Items List
            if (setlistItems.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nicio cântare adăugată încă în acest setlist.\nApasă \"Adaugă\" pentru a alege din repertoriu.",
                            color = ISYGray500,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                itemsIndexed(setlistItems) { index, item ->
                    val song = allSongs.firstOrNull { it.id == item.songId }
                    SetlistItemCard(
                        index = index,
                        totalCount = setlistItems.size,
                        item = item,
                        song = song,
                        onMoveUp = {
                            if (index > 0) {
                                val list = setlistItems.toMutableList()
                                val temp = list.removeAt(index)
                                list.add(index - 1, temp)
                                setlistItems = list
                            }
                        },
                        onMoveDown = {
                            if (index < setlistItems.size - 1) {
                                val list = setlistItems.toMutableList()
                                val temp = list.removeAt(index)
                                list.add(index + 1, temp)
                                setlistItems = list
                            }
                        },
                        onDelete = {
                            setlistItems = setlistItems.filterIndexed { i, _ -> i != index }
                        },
                        onSegmentChange = { newSegment ->
                            setlistItems = setlistItems.mapIndexed { i, si ->
                                if (i == index) si.copy(segment = newSegment) else si
                            }
                        },
                        onNotesChange = { newNotes ->
                            setlistItems = setlistItems.mapIndexed { i, si ->
                                if (i == index) si.copy(notes = newNotes) else si
                            }
                        }
                    )
                }
            }

            // General Notes
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ISYSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "Notițe Program", fontWeight = FontWeight.Bold, color = ISYWhite)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            placeholder = { Text("Instrucțiuni pentru echipă, tranziții, rugăciune...", color = ISYGray500) },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = ISYWhite,
                                unfocusedTextColor = ISYWhite,
                                focusedBorderColor = ISYCoral
                            )
                        )
                    }
                }
            }

            // Action: Start Live Mode
            item {
                Button(
                    onClick = {
                        val progId = initialProgram?.id ?: "prog_${System.currentTimeMillis()}"
                        val prog = Program(
                            id = progId,
                            title = title,
                            theme = theme,
                            date = date,
                            status = status,
                            setlist = setlistItems,
                            tags = selectedTags.toList(),
                            targetDurationMinutes = targetDuration,
                            notes = notes
                        )
                        viewModel.updateProgram(prog)
                        viewModel.navigateTo(Screen.LiveMode(progId))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ISYCoral),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Lansează în Live Mode", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Add Song Dialog

    if (showDeleteConfirmDialog && initialProgram != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            containerColor = ISYSurface,
            title = { Text("Șterge programul", color = ISYWhite, fontWeight = FontWeight.Bold) },
            text = { Text("Sigur dorești să ștergi programul '${initialProgram.title}'?", color = ISYGray300) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteProgram(initialProgram.id)
                        showDeleteConfirmDialog = false
                        viewModel.navigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ISYError)
                ) {
                    Text("Șterge", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Anulează", color = ISYGray300)
                }
            }
        )
    }

    if (showAddSongDialog) {
        AlertDialog(
            onDismissRequest = { showAddSongDialog = false },
            containerColor = ISYSurface,
            title = { Text("Alege Cântare din Repertoriu", color = ISYWhite) },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 320.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(allSongs) { song ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val newItem = SetlistItem(
                                        id = "si_${UUID.randomUUID()}",
                                        songId = song.id,
                                        segment = "worship"
                                    )
                                    setlistItems = setlistItems + newItem
                                    showAddSongDialog = false
                                },
                            colors = CardDefaults.cardColors(containerColor = ISYSurfaceElevated),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = song.title, fontWeight = FontWeight.Bold, color = ISYWhite, fontSize = 14.sp)
                                    Text(text = song.artist, color = ISYGray300, fontSize = 12.sp)
                                }
                                Text(text = song.key, color = ISYCoral, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showAddSongDialog = false }) {
                    Text("Închide", color = ISYGray300)
                }
            }
        )
    }
}

@Composable
private fun SetlistItemCard(
    index: Int,
    totalCount: Int,
    item: SetlistItem,
    song: Song?,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onDelete: () -> Unit,
    onSegmentChange: (String) -> Unit,
    onNotesChange: (String) -> Unit
) {
    var expandedSegmentMenu by remember { mutableStateOf(false) }
    var editingNotes by remember { mutableStateOf(false) }
    var notesText by remember(item.notes) { mutableStateOf(item.notes) }

    val segments = listOf("intro", "worship", "special", "sermon", "sending")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ISYSurface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(ISYSurfaceElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = ISYWhite
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Box {
                        Box(
                            modifier = Modifier
                                .clickable { expandedSegmentMenu = true }
                        ) {
                            SegmentBadge(segment = item.segment)
                        }

                        DropdownMenu(
                            expanded = expandedSegmentMenu,
                            onDismissRequest = { expandedSegmentMenu = false },
                            modifier = Modifier.background(ISYSurfaceElevated)
                        ) {
                            segments.forEach { seg ->
                                DropdownMenuItem(
                                    text = { Text(seg.replaceFirstChar { it.uppercase() }, color = ISYWhite) },
                                    onClick = {
                                        onSegmentChange(seg)
                                        expandedSegmentMenu = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Up / Down / Delete actions
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onMoveUp,
                        enabled = index > 0,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = "Sus",
                            tint = if (index > 0) ISYWhite else ISYGray500,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onMoveDown,
                        enabled = index < totalCount - 1,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = "Jos",
                            tint = if (index < totalCount - 1) ISYWhite else ISYGray500,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Șterge",
                            tint = ISYCoral,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = song?.title ?: "Cântare necunoscută",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = ISYWhite
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = song?.artist ?: "",
                    fontSize = 12.sp,
                    color = ISYGray300
                )
                Text(
                    text = "${song?.key ?: "-"} • ${((song?.durationSeconds ?: 0) / 60)} min",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ISYCoralLight
                )
            }

            // Notes row
            Spacer(modifier = Modifier.height(6.dp))
            if (editingNotes) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = notesText,
                        onValueChange = { notesText = it },
                        placeholder = { Text("Notițe (ex: intro pian)", fontSize = 12.sp) },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = ISYWhite,
                            unfocusedTextColor = ISYWhite
                        )
                    )
                    IconButton(onClick = {
                        onNotesChange(notesText)
                        editingNotes = false
                    }) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Salvează", tint = ISYSuccess)
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { editingNotes = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.EditNote,
                        contentDescription = null,
                        tint = ISYGray500,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (item.notes.isNotBlank()) item.notes else "+ Adaugă notiță specifică...",
                        fontSize = 11.sp,
                        color = if (item.notes.isNotBlank()) ISYCoralLight else ISYGray500
                    )
                }
            }
        }
    }
}
