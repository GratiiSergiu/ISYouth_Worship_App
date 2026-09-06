package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Program
import com.example.model.Screen
import com.example.theme.*
import com.example.ui.components.AccentCard
import com.example.ui.components.QuickActionButton
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadge
import com.example.viewmodel.WorshipViewModel

@Composable
fun DashboardScreen(
    viewModel: WorshipViewModel,
    modifier: Modifier = Modifier
) {
    val programs by viewModel.programs.collectAsState()
    val songs by viewModel.songs.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val unreadNotifs = notifications.count { !it.isRead }

    val upcomingProgram = programs.firstOrNull { it.status == "ready" } ?: programs.firstOrNull()
    val otherPrograms = programs.filter { it.id != upcomingProgram?.id }
    val totalSongs = songs.size
    val inProgress = songs.count { it.status == "learning" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ISYBlack)
            .statusBarsPadding()
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(ISYCoral, ISYIndigo)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = "Logo",
                            tint = ISYWhite,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "ISYouth Worship",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = ISYWhite
                        )
                        Text(
                            text = "Planificare & Coordonare",
                            fontSize = 12.sp,
                            color = ISYGray300
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(ISYSurface)
                        .clickable { viewModel.navigateTo(Screen.Notifications) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notificări",
                        tint = ISYWhite,
                        modifier = Modifier.size(20.dp)
                    )
                    if (unreadNotifs > 0) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .align(Alignment.TopEnd)
                                .clip(CircleShape)
                                .background(ISYCoral)
                        )
                    }
                }
            }
        }

        // Quick Actions Grid / Row
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickActionButton(
                        icon = Icons.Default.AddCircleOutline,
                        label = "Setlist Nou",
                        color = ISYCoral,
                        onClick = { viewModel.navigateTo(Screen.SetlistBuilder(null)) }
                    )
                    QuickActionButton(
                        icon = Icons.Default.LibraryMusic,
                        label = "Cântări",
                        color = ISYIndigo,
                        onClick = { viewModel.navigateTo(Screen.Songs) }
                    )
                    QuickActionButton(
                        icon = Icons.Default.PlayCircleOutline,
                        label = "Live Mode",
                        color = ISYCoralLight,
                        onClick = { viewModel.navigateTo(Screen.LiveMode(upcomingProgram?.id)) }
                    )
                    QuickActionButton(
                        icon = Icons.Default.Schedule,
                        label = "Repetiții",
                        color = ISYSuccess,
                        onClick = { viewModel.navigateTo(Screen.PracticeMode) }
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickActionButton(
                        icon = Icons.Default.CalendarMonth,
                        label = "Calendar",
                        color = Color(0xFF3B82F6),
                        onClick = { viewModel.navigateTo(Screen.Calendar) }
                    )
                    QuickActionButton(
                        icon = Icons.Default.BarChart,
                        label = "Statistici",
                        color = ISYWarning,
                        onClick = { viewModel.navigateTo(Screen.Statistics) }
                    )
                    QuickActionButton(
                        icon = Icons.Default.Groups,
                        label = "Echipa",
                        color = Color(0xFF8B5CF6),
                        onClick = { viewModel.navigateTo(Screen.Team) }
                    )
                    QuickActionButton(
                        icon = Icons.Default.Person,
                        label = "Profil",
                        color = ISYGray300,
                        onClick = { viewModel.navigateTo(Screen.Profile) }
                    )
                }
            }
        }

        // Stats Summary Cards
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Repertoriu",
                    value = "$totalSongs",
                    subtitle = "Cântări totale",
                    color = ISYCoral,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "În Lucru",
                    value = "$inProgress",
                    subtitle = "Cântări noi",
                    color = ISYWarning,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Programe",
                    value = "${programs.size}",
                    subtitle = "Planificate",
                    color = ISYSuccess,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Upcoming Program Hero Card
        item {
            SectionHeader(
                title = "Program Următor",
                actionText = "Vezi toate",
                onAction = { viewModel.navigateTo(Screen.Calendar) }
            )

            if (upcomingProgram != null) {
                val totalSeconds = upcomingProgram.setlist.fold(0) { sum, item ->
                    val s = viewModel.getSongById(item.songId)
                    sum + (s?.durationSeconds ?: 0)
                }
                val durationMin = (totalSeconds / 60).coerceAtLeast(1)

                AccentCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .testTag("upcoming_program_card"),
                    accentBrush = Brush.verticalGradient(listOf(ISYCoral, ISYIndigo)),
                    onClick = { viewModel.navigateTo(Screen.SetlistBuilder(upcomingProgram.id)) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                            StatusBadge(status = upcomingProgram.status)
                            Text(
                                text = upcomingProgram.date,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ISYGray300
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = upcomingProgram.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = ISYWhite
                        )
                        if (upcomingProgram.theme.isNotEmpty()) {
                            Text(
                                text = upcomingProgram.theme,
                                fontSize = 14.sp,
                                color = ISYCoralLight
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MusicNote,
                                    contentDescription = null,
                                    tint = ISYGray300,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${upcomingProgram.setlist.size} cântări",
                                    fontSize = 13.sp,
                                    color = ISYGray300
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = ISYGray300,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$durationMin / ${upcomingProgram.targetDurationMinutes} min",
                                    fontSize = 13.sp,
                                    color = ISYGray300
                                )
                            }
                        }

                        if (upcomingProgram.tags.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(upcomingProgram.tags) { tag ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(ISYSurfaceElevated)
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(text = tag, fontSize = 11.sp, color = ISYGray300)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { viewModel.navigateTo(Screen.LiveMode(upcomingProgram.id)) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("start_live_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = ISYCoral),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Live Mode", fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { viewModel.navigateTo(Screen.SetlistBuilder(upcomingProgram.id)) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ISYWhite),
                                border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(ISYIndigo, ISYCoral))),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Editează", fontWeight = FontWeight.SemiBold)
                            }
                        }
                }
            }
        }

        // Recent / Other Programs
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(
                title = "Alte Programe",
                actionText = "+ Adaugă",
                onAction = { viewModel.navigateTo(Screen.SetlistBuilder(null)) }
            )
        }

        items(otherPrograms) { program ->
            ProgramListItem(
                program = program,
                viewModel = viewModel,
                onClick = { viewModel.navigateTo(Screen.SetlistBuilder(program.id)) },
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ISYSurface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = color)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = ISYWhite)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, fontSize = 10.sp, color = ISYGray300)
        }
    }
}

@Composable
private fun ProgramListItem(
    program: Program,
    viewModel: WorshipViewModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalSeconds = program.setlist.fold(0) { sum, item ->
        val s = viewModel.getSongById(item.songId)
        sum + (s?.durationSeconds ?: 0)
    }
    val durationMin = (totalSeconds / 60).coerceAtLeast(1)

    val accentGradient = when (program.status.lowercase()) {
        "completed", "completat" -> Brush.verticalGradient(listOf(ISYSuccess, ISYIndigo))
        "ready", "programat" -> Brush.verticalGradient(listOf(ISYCoral, ISYIndigo))
        else -> Brush.verticalGradient(listOf(ISYWarning, ISYIndigo))
    }

    AccentCard(
        modifier = modifier,
        accentBrush = accentGradient,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusBadge(status = program.status)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = program.date,
                        fontSize = 12.sp,
                        color = ISYGray300
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = program.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ISYWhite
                )
                if (program.theme.isNotEmpty()) {
                    Text(
                        text = program.theme,
                        fontSize = 13.sp,
                        color = ISYGray300
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${program.setlist.size} cântări • $durationMin min",
                    fontSize = 12.sp,
                    color = ISYCoralLight
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = ISYGray500
            )
        }
    }
}
