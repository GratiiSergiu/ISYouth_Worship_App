package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingFlat
import androidx.compose.material.icons.automirrored.filled.TrendingUp
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
import com.example.model.SongStat
import com.example.theme.*
import com.example.viewmodel.WorshipViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: WorshipViewModel,
    modifier: Modifier = Modifier
) {
    val stats by viewModel.songStats.collectAsState()
    var selectedPeriod by remember { mutableStateOf("Tot timpul") }
    var sortByTop by remember { mutableStateOf(true) }

    val periods = listOf("3 luni", "6 luni", "1 an", "Tot timpul")

    val sortedStats = remember(stats, sortByTop) {
        if (sortByTop) stats.sortedByDescending { it.playCount }
        else stats.sortedBy { it.playCount }
    }

    val maxPlayCount = stats.maxOfOrNull { it.playCount }?.coerceAtLeast(1) ?: 1

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("statistics_screen"),
        containerColor = ISYBlack,
        topBar = {
            TopAppBar(
                title = { Text("Statistici Repertoriu", color = ISYWhite, fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Period selector
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    periods.forEach { period ->
                        val isSelected = selectedPeriod == period
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedPeriod = period },
                            label = { Text(period) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ISYCoral,
                                selectedLabelColor = ISYWhite,
                                containerColor = ISYSurface,
                                labelColor = ISYGray300
                            )
                        )
                    }
                }
            }

            // Overview Metric Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = ISYSurface)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Total Cântate", fontSize = 11.sp, color = ISYCoralLight, fontWeight = FontWeight.Bold)
                            Text("${stats.sumOf { it.playCount }}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = ISYWhite)
                            Text("în toate programele", fontSize = 10.sp, color = ISYGray300)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = ISYSurface)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Cântări Unice", fontSize = 11.sp, color = ISYSuccess, fontWeight = FontWeight.Bold)
                            Text("${stats.size}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = ISYWhite)
                            Text("în repertoriu activ", fontSize = 10.sp, color = ISYGray300)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = ISYSurface)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Top 1 Cântare", fontSize = 11.sp, color = ISYWarning, fontWeight = FontWeight.Bold)
                            Text(stats.firstOrNull()?.title?.take(8) ?: "-", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ISYWhite)
                            Text("${stats.firstOrNull()?.playCount ?: 0} interpretări", fontSize = 10.sp, color = ISYGray300)
                        }
                    }
                }
            }

            // Sorting toggle
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (sortByTop) "CELE MAI FRECVENTE" else "CELE MAI RAR CÂNTATE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ISYGray300,
                        letterSpacing = 1.sp
                    )

                    TextButton(onClick = { sortByTop = !sortByTop }) {
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = null,
                            tint = ISYCoral,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (sortByTop) "Vezi Rar Cântate" else "Vezi Top Cântate",
                            color = ISYCoral,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Stats list
            if (sortedStats.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = ISYSurface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("📊", fontSize = 32.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Nicio cântare în repertoriu",
                                color = ISYWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Adaugă cântări din tab-ul Cântări și asociază-le în programe pentru a vizualiza statistici live.",
                                color = ISYGray300,
                                fontSize = 12.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                itemsIndexed(sortedStats) { index, stat ->
                    StatItemCard(
                        rank = index + 1,
                        stat = stat,
                        maxCount = maxPlayCount
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItemCard(
    rank: Int,
    stat: SongStat,
    maxCount: Int
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ISYSurface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rank Circle
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (rank <= 3) ISYCoral else ISYSurfaceElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$rank",
                        color = ISYWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stat.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = ISYWhite
                    )
                    Text(
                        text = "${stat.artist} • Gama: ${stat.key}",
                        fontSize = 12.sp,
                        color = ISYGray300
                    )
                }

                // Trend icon
                val (trendIcon, trendColor) = when(stat.trend) {
                    "up" -> Pair(Icons.AutoMirrored.Filled.TrendingUp, ISYSuccess)
                    "down" -> Pair(Icons.AutoMirrored.Filled.TrendingDown, ISYCoral)
                    else -> Pair(Icons.AutoMirrored.Filled.TrendingFlat, ISYWarning)
                }
                Icon(
                    imageVector = trendIcon,
                    contentDescription = null,
                    tint = trendColor,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "${stat.playCount}x",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = ISYWhite
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress Bar of usage
            val progress = stat.playCount.toFloat() / maxCount
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = ISYCoral,
                trackColor = ISYSurfaceElevated
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Ultima dată: ${stat.lastPlayed}",
                    fontSize = 11.sp,
                    color = ISYGray500
                )
                Text(
                    text = if (expanded) "Ascunde istoric" else "Vezi istoric",
                    fontSize = 11.sp,
                    color = ISYCoralLight,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Expanded program history
            if (expanded && stat.programs.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(ISYBlack)
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "Istoric programe:", fontSize = 12.sp, color = ISYGray300, fontWeight = FontWeight.Bold)
                    stat.programs.forEach { p ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "${p.date} - ${p.program}", fontSize = 12.sp, color = ISYWhite)
                            Text(text = "Poziția #${p.position}", fontSize = 11.sp, color = ISYCoral)
                        }
                    }
                }
            }
        }
    }
}
