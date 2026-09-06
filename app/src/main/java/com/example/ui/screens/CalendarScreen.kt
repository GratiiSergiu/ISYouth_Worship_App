package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.theme.*
import com.example.ui.components.StatusBadge
import com.example.viewmodel.WorshipViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: WorshipViewModel,
    modifier: Modifier = Modifier
) {
    val programs by viewModel.programs.collectAsState()
    var selectedDay by remember { mutableIntStateOf(25) }
    var programToDelete by remember { mutableStateOf<Program?>(null) }

    val daysInMonth = 31
    val startDayOffset = 3 // Thursday (Mai 2025 started on Thursday)
    val eventDays = mapOf(
        4 to "Speranță",
        11 to "Harul",
        18 to "Credință",
        25 to "Identitate"
    )

    val selectedProgram = remember(selectedDay, programs) {
        programs.firstOrNull { it.date.startsWith("$selectedDay Mai") }
            ?: if (selectedDay == 25) programs.firstOrNull { it.id == "prog_1" } else null
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("calendar_screen"),
        containerColor = ISYBlack,
        topBar = {
            TopAppBar(
                title = { Text(text = "Calendar Programe", color = ISYWhite, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Înapoi",
                            tint = ISYWhite
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Month Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Luna anterioară", tint = ISYWhite)
                    }
                    Text(
                        text = "Mai 2025",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ISYWhite
                    )
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Luna următoare", tint = ISYWhite)
                    }
                }
            }

            // Days of week header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    listOf("L", "M", "M", "J", "V", "S", "D").forEach { d ->
                        Text(
                            text = d,
                            color = if (d == "D") ISYCoral else ISYGray300,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Calendar Grid
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ISYSurface)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        var dayCounter = 1
                        val totalSlots = startDayOffset + daysInMonth
                        val rows = (totalSlots + 6) / 7

                        for (r in 0 until rows) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                for (c in 0..6) {
                                    val slot = r * 7 + c
                                    if (slot < startDayOffset || dayCounter > daysInMonth) {
                                        Box(modifier = Modifier.size(38.dp))
                                    } else {
                                        val day = dayCounter
                                        val isSelected = day == selectedDay
                                        val hasEvent = eventDays.containsKey(day)

                                        Box(
                                            modifier = Modifier
                                                .size(38.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (isSelected) ISYCoral
                                                    else if (hasEvent) ISYSurfaceElevated
                                                    else Color.Transparent
                                                )
                                                .clickable { selectedDay = day },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(
                                                    text = "$day",
                                                    fontSize = 14.sp,
                                                    fontWeight = if (isSelected || hasEvent) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isSelected) ISYWhite else if (hasEvent) ISYCoralLight else ISYGray300
                                                )
                                                if (hasEvent && !isSelected) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(4.dp)
                                                            .clip(CircleShape)
                                                            .background(ISYCoral)
                                                    )
                                                }
                                            }
                                        }
                                        dayCounter++
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Selected Day Details Card
            item {
                Text(
                    text = "EVENIMENT PENTRU $selectedDay MAI 2025",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ISYGray300,
                    letterSpacing = 1.sp
                )
            }

            item {
                if (selectedProgram != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = ISYSurface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedProgram.title,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ISYWhite
                                )
                                StatusBadge(status = selectedProgram.status)
                            }
                            if (selectedProgram.theme.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = selectedProgram.theme,
                                    fontSize = 13.sp,
                                    color = ISYCoralLight
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "${selectedProgram.setlist.size} cântări planificate • ${selectedProgram.targetDurationMinutes} min",
                                fontSize = 13.sp,
                                color = ISYGray300
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(
                                    onClick = { viewModel.navigateTo(Screen.SetlistBuilder(selectedProgram.id)) },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = ISYCoral),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Deschide Setlist")
                                }
                                Button(
                                    onClick = { viewModel.navigateTo(Screen.LiveMode(selectedProgram.id)) },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = ISYIndigo),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Live Mode")
                                }
                                IconButton(
                                    onClick = { programToDelete = selectedProgram }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Șterge program",
                                        tint = ISYError
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = ISYSurface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Niciun program programat pe $selectedDay Mai.",
                                color = ISYGray300,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    viewModel.navigateTo(Screen.SetlistBuilder(null))
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ISYCoral),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Creează program pentru această zi")
                            }
                        }
                    }
                }
            }

            // All month programs overview
            item {
                Text(
                    text = "TOATE PROGRAMELE LUNII",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ISYGray300,
                    letterSpacing = 1.sp
                )
            }

            items(programs) { prog ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.navigateTo(Screen.SetlistBuilder(prog.id)) },
                    shape = RoundedCornerShape(10.dp),
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
                            Text(text = prog.title, fontWeight = FontWeight.Bold, color = ISYWhite)
                            Text(text = "${prog.date} • ${prog.setlist.size} cântări", fontSize = 12.sp, color = ISYGray300)
                        }
                        StatusBadge(status = prog.status)
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = { programToDelete = prog },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Șterge",
                                tint = ISYError.copy(alpha = 0.8f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    programToDelete?.let { prog ->
        AlertDialog(
            onDismissRequest = { programToDelete = null },
            containerColor = ISYSurface,
            title = { Text("Șterge programul", color = ISYWhite, fontWeight = FontWeight.Bold) },
            text = { Text("Sigur dorești să elimini programul '${prog.title}'?", color = ISYGray300) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteProgram(prog.id)
                        programToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ISYError)
                ) {
                    Text("Șterge", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { programToDelete = null }) {
                    Text("Anulează", color = ISYGray300)
                }
            }
        )
    }

}
