package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AgendaItem
import com.example.model.Attendee
import com.example.model.Rehearsal
import com.example.theme.*
import com.example.ui.components.StatusBadge
import com.example.viewmodel.WorshipViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeModeScreen(
    viewModel: WorshipViewModel,
    modifier: Modifier = Modifier
) {
    val rehearsals by viewModel.rehearsals.collectAsState()
    var showScheduleDialog by remember { mutableStateOf(false) }
    var editingRehearsal by remember { mutableStateOf<Rehearsal?>(null) }
    var rehearsalToDelete by remember { mutableStateOf<Rehearsal?>(null) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("practice_mode_screen"),
        containerColor = ISYBlack,
        topBar = {
            TopAppBar(
                title = { Text(text = "Practice Mode • Repetiții", color = ISYWhite, fontWeight = FontWeight.Bold) },
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
                    IconButton(onClick = { showScheduleDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Programează repetiție",
                            tint = ISYCoral
                        )
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
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "REPETIȚII & COORDONARE ECHIPĂ",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ISYGray300,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                )
            }

            items(rehearsals) { rehearsal ->
                RehearsalCard(
                    rehearsal = rehearsal,
                    onStatusChange = { attendeeName, newStatus ->
                        viewModel.updateAttendeeStatus(rehearsal.id, attendeeName, newStatus)
                    },
                    onEdit = { editingRehearsal = rehearsal },
                    onDelete = { rehearsalToDelete = rehearsal }
                )
            }
        }
    }

    if (showScheduleDialog) {
        AddOrEditRehearsalDialog(
            initialRehearsal = null,
            onDismiss = { showScheduleDialog = false },
            onSave = { newReh ->
                viewModel.addRehearsal(newReh)
                showScheduleDialog = false
            }
        )
    }

    editingRehearsal?.let { reh ->
        AddOrEditRehearsalDialog(
            initialRehearsal = reh,
            onDismiss = { editingRehearsal = null },
            onSave = { updatedReh ->
                viewModel.updateRehearsal(updatedReh)
                editingRehearsal = null
            }
        )
    }

    rehearsalToDelete?.let { reh ->
        AlertDialog(
            onDismissRequest = { rehearsalToDelete = null },
            containerColor = ISYSurface,
            title = { Text("Șterge repetiția", color = ISYWhite, fontWeight = FontWeight.Bold) },
            text = { Text("Sigur dorești să elimini repetiția pentru \"${reh.programName}\" (${reh.date})?", color = ISYGray300) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteRehearsal(reh.id)
                        rehearsalToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ISYError)
                ) {
                    Text("Șterge", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { rehearsalToDelete = null }) {
                    Text("Anulează", color = ISYGray300)
                }
            }
        )
    }
}

@Composable
private fun RehearsalCard(
    rehearsal: Rehearsal,
    onStatusChange: (String, String) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedAgenda by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ISYSurface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = rehearsal.programName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ISYWhite
                    )
                    Text(
                        text = "${rehearsal.date} • ${rehearsal.time}",
                        fontSize = 13.sp,
                        color = ISYCoralLight
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusBadge(status = rehearsal.status)
                    IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editează repetiție",
                            tint = ISYGray300,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Elimină repetiție",
                            tint = ISYError.copy(alpha = 0.8f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Location & Duration
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = ISYGray300, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = rehearsal.location, fontSize = 12.sp, color = ISYGray300)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.AccessTime, contentDescription = null, tint = ISYGray300, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${rehearsal.duration} minute", fontSize = 12.sp, color = ISYGray300)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Attendees Section
            Text(
                text = "Prezență (${rehearsal.attendees.count { it.status == "confirmed" }}/${rehearsal.attendees.size} Confirmați):",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = ISYWhite
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                rehearsal.attendees.forEach { att ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(ISYIndigo),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = att.avatar, fontSize = 11.sp, color = ISYWhite, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = att.name, fontSize = 13.sp, color = ISYWhite)
                        }

                        // Status chip clickable to toggle
                        val (statusText, statusColor) = when(att.status) {
                            "confirmed" -> Pair("Confirmat", ISYSuccess)
                            "maybe" -> Pair("Poate", ISYWarning)
                            "declined" -> Pair("Refuzat", ISYCoral)
                            else -> Pair("Fără răspuns", ISYGray500)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(statusColor.copy(alpha = 0.2f))
                                .clickable {
                                    val nextStatus = when(att.status) {
                                        "confirmed" -> "maybe"
                                        "maybe" -> "declined"
                                        "declined" -> "no-response"
                                        else -> "confirmed"
                                    }
                                    onStatusChange(att.name, nextStatus)
                                }
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(text = statusText, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = statusColor)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Agenda Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedAgenda = !expandedAgenda },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Agendă Repetiție (${rehearsal.agenda.size} puncte)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = ISYCoralLight
                )
                Icon(
                    imageVector = if (expandedAgenda) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = ISYCoralLight
                )
            }

            if (expandedAgenda) {
                Spacer(modifier = Modifier.height(8.dp))
                rehearsal.agenda.forEach { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = ISYSurfaceElevated)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = item.song, fontWeight = FontWeight.Bold, color = ISYWhite, fontSize = 13.sp)
                                Text(text = "Focus: ${item.focus}", fontSize = 12.sp, color = ISYGray300)
                            }
                            Text(text = "${item.duration}m", color = ISYWarning, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Recording item if available
            if (rehearsal.hasRecording) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(ISYSuccess.copy(alpha = 0.15f))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.GraphicEq, contentDescription = null, tint = ISYSuccess)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Înregistrare audio disponibilă", color = ISYSuccess, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun AddOrEditRehearsalDialog(
    initialRehearsal: Rehearsal?,
    onDismiss: () -> Unit,
    onSave: (Rehearsal) -> Unit
) {
    var programName by remember { mutableStateOf(initialRehearsal?.programName ?: "") }
    var date by remember { mutableStateOf(initialRehearsal?.date ?: "30 Mai 2025") }
    var time by remember { mutableStateOf(initialRehearsal?.time ?: "19:00") }
    var location by remember { mutableStateOf(initialRehearsal?.location ?: "Sala Tineret") }
    var duration by remember { mutableStateOf(initialRehearsal?.duration?.toString() ?: "90") }
    var status by remember { mutableStateOf(initialRehearsal?.status ?: "scheduled") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ISYSurface,
        title = {
            Text(
                text = if (initialRehearsal == null) "Programează Repetiție" else "Editează Repetiția",
                color = ISYWhite,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = programName,
                    onValueChange = { programName = it },
                    label = { Text("Nume Program") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = ISYWhite, unfocusedTextColor = ISYWhite, focusedBorderColor = ISYCoral)
                )
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Data") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = ISYWhite, unfocusedTextColor = ISYWhite)
                )
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Ora (ex: 19:00)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = ISYWhite, unfocusedTextColor = ISYWhite)
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Locație") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = ISYWhite, unfocusedTextColor = ISYWhite)
                )
                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Durată (minute)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = ISYWhite, unfocusedTextColor = ISYWhite)
                )

                // Status chip
                Text("Status:", fontSize = 12.sp, color = ISYGray300)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("scheduled" to "Programat", "completed" to "Finalizat").forEach { (sKey, sLabel) ->
                        FilterChip(
                            selected = status == sKey,
                            onClick = { status = sKey },
                            label = { Text(sLabel) },
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
        },
        confirmButton = {
            Button(
                onClick = {
                    if (programName.isNotBlank()) {
                        val rehId = initialRehearsal?.id ?: "reh_${System.currentTimeMillis()}"
                        val attendees = initialRehearsal?.attendees ?: listOf(
                            Attendee("Andrei Popescu", "confirmed", "A"),
                            Attendee("Maria Ionescu", "confirmed", "M"),
                            Attendee("Alex Dumitru", "maybe", "A"),
                            Attendee("Cristina Marin", "confirmed", "C")
                        )
                        val agenda = initialRehearsal?.agenda ?: listOf(
                            AgendaItem("Cântare 1", "Dinamica", 20),
                            AgendaItem("Cântare 2", "Armonii", 20)
                        )
                        onSave(
                            Rehearsal(
                                id = rehId,
                                programName = programName.trim(),
                                date = date.trim(),
                                time = time.trim(),
                                location = location.trim(),
                                duration = duration.toIntOrNull() ?: 90,
                                status = status,
                                attendees = attendees,
                                agenda = agenda,
                                hasRecording = initialRehearsal?.hasRecording ?: false,
                                recordingUrl = initialRehearsal?.recordingUrl ?: ""
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ISYCoral)
            ) {
                Text(if (initialRehearsal == null) "Salvează" else "Actualizează", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Anulează", color = ISYGray300)
            }
        }
    )
}
