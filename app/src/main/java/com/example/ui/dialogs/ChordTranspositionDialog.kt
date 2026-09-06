package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.theme.*
import com.example.util.MusicTransposer

@Composable
fun ChordTranspositionDialog(
    originalKey: String,
    currentKey: String,
    onKeySelected: (newKey: String, semitonesDelta: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var notationMode by remember { mutableIntStateOf(0) } // 0 = Ambele, 1 = Română (Do), 2 = Internațional (C)

    val options = remember(originalKey, currentKey) {
        MusicTransposer.getTranspositionOptions(originalKey, currentKey)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.85f)
                .testTag("chord_transposition_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ISYSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(ISYCoral.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = null,
                                tint = ISYCoralLight,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Transpoziție Cântare",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = ISYWhite
                            )
                            Text(
                                text = "Gama originală a piesei: $originalKey",
                                fontSize = 12.sp,
                                color = ISYCoralLight,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Închide",
                            tint = ISYGray300
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Notation format selector
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SegmentedButton(
                        selected = notationMode == 0,
                        onClick = { notationMode = 0 },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = ISYCoral,
                            activeContentColor = ISYWhite,
                            inactiveContainerColor = ISYSurfaceElevated,
                            inactiveContentColor = ISYGray300
                        )
                    ) {
                        Text("Ambele", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                    SegmentedButton(
                        selected = notationMode == 1,
                        onClick = { notationMode = 1 },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = ISYCoral,
                            activeContentColor = ISYWhite,
                            inactiveContainerColor = ISYSurfaceElevated,
                            inactiveContentColor = ISYGray300
                        )
                    ) {
                        Text("Do - Re - Mi", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                    SegmentedButton(
                        selected = notationMode == 2,
                        onClick = { notationMode = 2 },
                        shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = ISYCoral,
                            activeContentColor = ISYWhite,
                            inactiveContainerColor = ISYSurfaceElevated,
                            inactiveContentColor = ISYGray300
                        )
                    ) {
                        Text("C - D - E", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Apasă pe acordul dorit pentru a transpune automat întreaga piesă:",
                    fontSize = 12.sp,
                    color = ISYGray300
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 12 Chromatic Keys Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(options) { option ->
                        val displayChord = when (notationMode) {
                            1 -> option.romanianKey
                            2 -> option.englishKey
                            else -> "${option.romanianKey} / ${option.englishKey}"
                        }
                        val chosenKeyToEmit = when (notationMode) {
                            1 -> option.romanianKey
                            2 -> option.englishKey
                            else -> if (originalKey.any { it in "DoReMiFaSolLaSi" }) option.romanianKey else option.englishKey
                        }

                        val isCurrent = option.isCurrent

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(68.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    width = if (isCurrent) 2.dp else 1.dp,
                                    color = if (isCurrent) ISYCoral else ISYSurfaceElevated,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    onKeySelected(chosenKeyToEmit, option.semitonesDelta)
                                    onDismiss()
                                }
                                .testTag("transposition_key_${option.englishKey}"),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isCurrent) ISYCoral.copy(alpha = 0.22f) else ISYSurfaceElevated
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 6.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = displayChord,
                                        fontSize = if (notationMode == 0) 12.sp else 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCurrent) ISYCoralLight else ISYWhite,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1
                                    )
                                    if (isCurrent) {
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selectat",
                                            tint = ISYCoralLight,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                // Badge showing delta
                                val badgeText = when {
                                    option.isOriginal -> "ORIGINAL"
                                    option.semitonesDelta > 0 -> "+${option.semitonesDelta} semit."
                                    else -> "${option.semitonesDelta} semit."
                                }
                                val badgeColor = when {
                                    option.isOriginal -> ISYSuccess
                                    isCurrent -> ISYCoralLight
                                    else -> ISYGray500
                                }

                                Text(
                                    text = badgeText,
                                    fontSize = 10.sp,
                                    fontWeight = if (option.isOriginal || isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    color = badgeColor
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val originalOption = options.firstOrNull { it.isOriginal }
                    OutlinedButton(
                        onClick = {
                            val resetKey = originalOption?.let {
                                if (notationMode == 1) it.romanianKey else if (notationMode == 2) it.englishKey else originalKey
                            } ?: originalKey
                            onKeySelected(resetKey, 0)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ISYWhite),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reset la Original", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = ISYSurfaceElevated),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Închide", color = ISYWhite, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
