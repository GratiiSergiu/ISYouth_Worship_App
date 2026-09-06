package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.Song
import com.example.theme.*
import com.example.util.MusicTransposer
import com.example.util.WebSongImporter
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrEditSongDialog(
    initialSong: Song?,
    categories: List<String>,
    onDismiss: () -> Unit,
    onSave: (Song) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current

    // Selected tab: 0 = Link Web, 1 = Copy-Paste, 2 = Detalii
    var selectedTab by remember { mutableIntStateOf(if (initialSong == null) 0 else 2) }

    // Core song fields
    var title by remember { mutableStateOf(initialSong?.title ?: "") }
    var artist by remember { mutableStateOf(initialSong?.artist ?: "") }
    var key by remember { mutableStateOf(initialSong?.key ?: "Sol") }
    var tempo by remember { mutableStateOf(initialSong?.tempo?.toString() ?: "70") }
    var duration by remember { mutableStateOf(initialSong?.durationSeconds?.toString() ?: "240") }
    var status by remember { mutableStateOf(initialSong?.status ?: "repertoire") }
    var selectedCategory by remember { mutableStateOf(initialSong?.category ?: "Repertoriu") }
    var lyrics by remember { mutableStateOf(initialSong?.lyrics ?: "") }
    var chords by remember { mutableStateOf(initialSong?.chords ?: "") }

    // Web Import fields
    var webUrl by remember { mutableStateOf("") }
    var isLoadingUrl by remember { mutableStateOf(false) }
    var urlErrorMessage by remember { mutableStateOf<String?>(null) }
    var webRawContent by remember { mutableStateOf("") }

    // Copy-Paste raw text
    var pastedRawText by remember { mutableStateOf("") }

    // Transposition State
    var semitonesOffset by remember { mutableIntStateOf(0) }
    var preferRomanianNotation by remember { mutableStateOf(true) }
    var showTranspositionKeyPicker by remember { mutableStateOf(false) }

    // Current transposed key
    val effectiveKey = remember(key, semitonesOffset, preferRomanianNotation) {
        if (key.isBlank()) "Sol"
        else MusicTransposer.transposeKey(key, semitonesOffset, preferRomanianNotation)
    }

    if (showTranspositionKeyPicker) {
        ChordTranspositionDialog(
            originalKey = key.ifBlank { "Do" },
            currentKey = effectiveKey,
            onKeySelected = { newKey, delta ->
                semitonesOffset = delta
            },
            onDismiss = { showTranspositionKeyPicker = false }
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(20.dp)),
            color = ISYSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (initialSong == null) "Adăugare Cântare" else "Editare Cântare",
                        color = ISYWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Închide", tint = ISYGray300)
                    }
                }

                // Mode Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = ISYSurfaceElevated,
                    contentColor = ISYCoral,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = ISYCoral
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("1. Link Web", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        icon = { Icon(Icons.Default.Link, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("2. Copy-Paste", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        icon = { Icon(Icons.Default.ContentPaste, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("3. Detalii", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        icon = { Icon(Icons.Default.EditNote, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tab Content
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    when (selectedTab) {
                        // TAB 0: IMPORT FROM WEB LINK
                        0 -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "Introduceți adresa web completă a piesei (ex: melodia.ro, resursecrestine.ro, ultimate-guitar.com)",
                                    color = ISYGray300,
                                    fontSize = 12.sp
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = webUrl,
                                        onValueChange = {
                                            webUrl = it
                                            urlErrorMessage = null
                                        },
                                        placeholder = { Text("https://melodia.ro/cantari/...", color = ISYGray500, fontSize = 13.sp) },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true,
                                        leadingIcon = {
                                            Icon(Icons.Default.Language, contentDescription = null, tint = ISYCoral)
                                        },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = ISYWhite,
                                            unfocusedTextColor = ISYWhite,
                                            focusedBorderColor = ISYCoral
                                        )
                                    )

                                    IconButton(
                                        onClick = {
                                            clipboardManager.getText()?.text?.let { clipText ->
                                                webUrl = clipText
                                            }
                                        }
                                    ) {
                                        Icon(Icons.Default.ContentPaste, contentDescription = "Lipește link", tint = ISYWhite)
                                    }
                                }

                                if (urlErrorMessage != null) {
                                    Text(
                                        text = urlErrorMessage ?: "",
                                        color = ISYError,
                                        fontSize = 12.sp
                                    )
                                }

                                Button(
                                    onClick = {
                                        if (webUrl.isBlank()) {
                                            urlErrorMessage = "Vă rugăm introduceți o adresă URL validă."
                                            return@Button
                                        }
                                        isLoadingUrl = true
                                        urlErrorMessage = null
                                        coroutineScope.launch {
                                            val result = WebSongImporter.fetchSongFromUrl(webUrl)
                                            isLoadingUrl = false
                                            result.onSuccess { imported ->
                                                title = imported.title
                                                artist = imported.artist
                                                key = imported.key
                                                webRawContent = imported.content
                                                chords = imported.content
                                                lyrics = imported.lyricsOnly.ifBlank { imported.content }
                                                semitonesOffset = 0
                                            }.onFailure { err ->
                                                urlErrorMessage = "Nu s-a putut descărca automat: ${err.message}. Puteți folosi tab-ul 'Copy-Paste'."
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = !isLoadingUrl,
                                    colors = ButtonDefaults.buttonColors(containerColor = ISYCoral),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    if (isLoadingUrl) {
                                        CircularProgressIndicator(color = ISYWhite, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Se preia piesa de pe site...")
                                    } else {
                                        Icon(Icons.Default.CloudDownload, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Preia piesa și acordurile")
                                    }
                                }

                                // If song is imported or ready, show details banner and transposition controls
                                if (webRawContent.isNotBlank() || chords.isNotBlank()) {
                                    HorizontalDivider(color = ISYSurfaceElevated)

                                    // Status card with imported song information
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = ISYSurfaceElevated)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(14.dp),
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ISYSuccess, modifier = Modifier.size(16.dp))
                                                    Text(
                                                        text = "Preluat cu succes din link",
                                                        color = ISYSuccess,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 13.sp
                                                    )
                                                }
                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = ISYCoral.copy(alpha = 0.2f)
                                                ) {
                                                    Text(
                                                        text = "Tonalitate: $effectiveKey",
                                                        color = ISYCoralLight,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }

                                            Text(
                                                text = title.ifBlank { "Cântare Nouă" },
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = ISYWhite
                                            )

                                            Text(
                                                text = "Autor: ${artist.ifBlank { "Necunoscut" }}",
                                                fontSize = 12.sp,
                                                color = ISYGray300
                                            )

                                            Text(
                                                text = "✓ Versuri, acorduri și repartizare strofe/refrene preluate fidel",
                                                fontSize = 11.sp,
                                                color = ISYCoralLight
                                            )
                                        }
                                    }

                                    Text(
                                        text = "TRANSPOZIȚIE LIVE ACORDURI",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = ISYCoralLight,
                                        letterSpacing = 1.sp
                                    )

                                    // Transpose bar: click on chord to select directly from list
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { showTranspositionKeyPicker = true }
                                            .testTag("tab0_chord_transposition_picker"),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = ISYSurfaceElevated)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 14.dp, vertical = 10.dp),
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
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(ISYCoral.copy(alpha = 0.2f)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = effectiveKey,
                                                        color = ISYCoralLight,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 16.sp
                                                    )
                                                }
                                                Column {
                                                    Text("Tonalitate Cântare (Apasă pentru listă)", fontSize = 11.sp, color = ISYGray300)
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(
                                                            text = effectiveKey,
                                                            fontSize = 16.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = ISYWhite
                                                        )
                                                        if (semitonesOffset != 0) {
                                                            Spacer(modifier = Modifier.width(6.dp))
                                                            Text(
                                                                text = "(Original: $key, ${if (semitonesOffset > 0) "+$semitonesOffset" else "$semitonesOffset"} semit.)",
                                                                fontSize = 11.sp,
                                                                color = ISYCoralLight
                                                            )
                                                        }
                                                    }
                                                }
                                            }

                                            Surface(
                                                color = ISYCoral,
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text("Alege Acord", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ISYWhite)
                                                    Spacer(modifier = Modifier.width(3.dp))
                                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = ISYWhite, modifier = Modifier.size(16.dp))
                                                }
                                            }
                                        }
                                    }

                                    // Notation toggle (Română vs Internațional)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Notație acorduri:", color = ISYGray300, fontSize = 12.sp)
                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            FilterChip(
                                                selected = preferRomanianNotation,
                                                onClick = { preferRomanianNotation = true },
                                                label = { Text("Do-Re-Mi", fontSize = 11.sp) },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = ISYCoral,
                                                    selectedLabelColor = ISYWhite,
                                                    containerColor = ISYSurfaceElevated,
                                                    labelColor = ISYGray300
                                                )
                                            )
                                            FilterChip(
                                                selected = !preferRomanianNotation,
                                                onClick = { preferRomanianNotation = false },
                                                label = { Text("C-D-E", fontSize = 11.sp) },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = ISYCoral,
                                                    selectedLabelColor = ISYWhite,
                                                    containerColor = ISYSurfaceElevated,
                                                    labelColor = ISYGray300
                                                )
                                            )
                                        }
                                    }

                                    // Preview of transposed chords
                                    val currentPreview = remember(webRawContent, chords, semitonesOffset, preferRomanianNotation) {
                                        val base = if (webRawContent.isNotBlank()) webRawContent else chords
                                        MusicTransposer.transposeTextContent(base, semitonesOffset, preferRomanianNotation)
                                    }

                                    Text("Previzualizare Acorduri & Versuri:", fontSize = 12.sp, color = ISYGray300)
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(min = 100.dp, max = 220.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(ISYSurfaceElevated)
                                            .padding(10.dp)
                                            .verticalScroll(rememberScrollState())
                                    ) {
                                        Text(
                                            text = currentPreview,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 12.sp,
                                            color = ISYWhite
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            chords = currentPreview
                                            lyrics = WebSongImporter.extractLyricsOnly(currentPreview).ifBlank { currentPreview }
                                            key = effectiveKey
                                            selectedTab = 2 // Move to details
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = ISYSuccess),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Continuă spre detalii și salvare")
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                                    }
                                }
                            }
                        }

                        // TAB 1: COPY-PASTE TEXT
                        1 -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "Lipiți direct versurile cu acorduri. Puteți transpune acordurile în timp real.",
                                    color = ISYGray300,
                                    fontSize = 12.sp
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(
                                        onClick = {
                                            clipboardManager.getText()?.text?.let { clip ->
                                                pastedRawText = clip
                                                chords = clip
                                                lyrics = WebSongImporter.extractLyricsOnly(clip).ifBlank { clip }
                                                val detected = WebSongImporter.detectSongKey(clip)
                                                if (detected.isNotBlank()) {
                                                    key = detected
                                                }
                                            }
                                        }
                                    ) {
                                        Icon(Icons.Default.ContentPaste, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Lipește din Clipboard", fontSize = 12.sp, color = ISYCoralLight)
                                    }
                                }

                                OutlinedTextField(
                                    value = pastedRawText,
                                    onValueChange = {
                                        pastedRawText = it
                                        chords = it
                                        lyrics = it
                                    },
                                    placeholder = {
                                        Text(
                                            "[Sol] Mare ești Tu, Doamne\n[Do] Și vrednic de laudă...",
                                            color = ISYGray500,
                                            fontSize = 12.sp
                                        )
                                    },
                                    minLines = 6,
                                    maxLines = 10,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = ISYWhite,
                                        unfocusedTextColor = ISYWhite,
                                        focusedBorderColor = ISYCoral
                                    )
                                )

                                // Live Transposition Controls for Copy-Paste
                                if (pastedRawText.isNotBlank()) {
                                    Text(
                                        text = "TRANSPOZIȚIE LIVE ACORDURI",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = ISYCoralLight
                                    )

                                    // Transpose bar: click on chord to select directly from list
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { showTranspositionKeyPicker = true }
                                            .testTag("tab1_chord_transposition_picker"),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = ISYSurfaceElevated)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 14.dp, vertical = 10.dp),
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
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(ISYCoral.copy(alpha = 0.2f)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = effectiveKey,
                                                        color = ISYCoralLight,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 16.sp
                                                    )
                                                }
                                                Column {
                                                    Text("Tonalitate Cântare (Apasă pentru listă)", fontSize = 11.sp, color = ISYGray300)
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(
                                                            text = effectiveKey,
                                                            fontSize = 16.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = ISYWhite
                                                        )
                                                        if (semitonesOffset != 0) {
                                                            Spacer(modifier = Modifier.width(6.dp))
                                                            Text(
                                                                text = "(Original: $key, ${if (semitonesOffset > 0) "+$semitonesOffset" else "$semitonesOffset"} semit.)",
                                                                fontSize = 11.sp,
                                                                color = ISYCoralLight
                                                            )
                                                        }
                                                    }
                                                }
                                            }

                                            Surface(
                                                color = ISYCoral,
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text("Alege Acord", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ISYWhite)
                                                    Spacer(modifier = Modifier.width(3.dp))
                                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = ISYWhite, modifier = Modifier.size(16.dp))
                                                }
                                            }
                                        }
                                    }

                                    // Notation toggle
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Notație acorduri:", color = ISYGray300, fontSize = 12.sp)
                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            FilterChip(
                                                selected = preferRomanianNotation,
                                                onClick = { preferRomanianNotation = true },
                                                label = { Text("Do-Re-Mi", fontSize = 11.sp) },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = ISYCoral,
                                                    selectedLabelColor = ISYWhite,
                                                    containerColor = ISYSurfaceElevated,
                                                    labelColor = ISYGray300
                                                )
                                            )
                                            FilterChip(
                                                selected = !preferRomanianNotation,
                                                onClick = { preferRomanianNotation = false },
                                                label = { Text("C-D-E", fontSize = 11.sp) },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = ISYCoral,
                                                    selectedLabelColor = ISYWhite,
                                                    containerColor = ISYSurfaceElevated,
                                                    labelColor = ISYGray300
                                                )
                                            )
                                        }
                                    }

                                    Button(
                                        onClick = {
                                            val transposed = MusicTransposer.transposeTextContent(pastedRawText, semitonesOffset, preferRomanianNotation)
                                            chords = transposed
                                            lyrics = WebSongImporter.extractLyricsOnly(transposed).ifBlank { transposed }
                                            key = effectiveKey
                                            selectedTab = 2 // Move to details
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = ISYSuccess),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Continuă spre detalii și salvare")
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                                    }
                                }
                            }
                        }

                        // TAB 2: DETALII & SALVARE
                        2 -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = title,
                                    onValueChange = { title = it },
                                    label = { Text("Titlu cântare *") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = ISYWhite,
                                        unfocusedTextColor = ISYWhite,
                                        focusedBorderColor = ISYCoral
                                    )
                                )

                                OutlinedTextField(
                                    value = artist,
                                    onValueChange = { artist = it },
                                    label = { Text("Artist / Autor") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = ISYWhite,
                                        unfocusedTextColor = ISYWhite,
                                        focusedBorderColor = ISYCoral
                                    )
                                )

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = key,
                                        onValueChange = { key = it },
                                        label = { Text("Gamă (Key)") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = ISYWhite,
                                            unfocusedTextColor = ISYWhite,
                                            focusedBorderColor = ISYCoral
                                        )
                                    )
                                    OutlinedTextField(
                                        value = tempo,
                                        onValueChange = { tempo = it },
                                        label = { Text("BPM") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = ISYWhite,
                                            unfocusedTextColor = ISYWhite,
                                            focusedBorderColor = ISYCoral
                                        )
                                    )
                                }

                                // Category Selector
                                Text("Categorie Cântare:", fontSize = 12.sp, color = ISYGray300)
                                val availableCategories = categories.filter { it != "Toate" && it != "Favorite" }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    availableCategories.take(4).forEach { cat ->
                                        FilterChip(
                                            selected = selectedCategory == cat,
                                            onClick = { selectedCategory = cat },
                                            label = { Text(cat, fontSize = 11.sp) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = ISYCoral,
                                                selectedLabelColor = ISYWhite,
                                                containerColor = ISYSurfaceElevated,
                                                labelColor = ISYGray300
                                            )
                                        )
                                    }
                                }

                                // Status chips
                                Text("Stare în repertoriu:", fontSize = 12.sp, color = ISYGray300)
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    listOf("repertoire" to "Repertoriu", "learning" to "În lucru", "new" to "Nou").forEach { (sKey, sLabel) ->
                                        FilterChip(
                                            selected = status == sKey,
                                            onClick = { status = sKey },
                                            label = { Text(sLabel, fontSize = 12.sp) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = ISYCoral,
                                                selectedLabelColor = ISYWhite,
                                                containerColor = ISYSurfaceElevated,
                                                labelColor = ISYGray300
                                            )
                                        )
                                    }
                                }

                                OutlinedTextField(
                                    value = chords,
                                    onValueChange = { chords = it },
                                    label = { Text("Acorduri și structură") },
                                    minLines = 4,
                                    maxLines = 8,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = ISYWhite,
                                        unfocusedTextColor = ISYWhite
                                    )
                                )

                                OutlinedTextField(
                                    value = lyrics,
                                    onValueChange = { lyrics = it },
                                    label = { Text("Versuri") },
                                    minLines = 3,
                                    maxLines = 8,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = ISYWhite,
                                        unfocusedTextColor = ISYWhite
                                    )
                                )
                            }
                        }
                    }
                }

                // Bottom Action Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Anulează", color = ISYGray300)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (title.isBlank()) return@Button
                            val cleanTitle = title.replace("\r", "").replace("\n", " ").replace(Regex("\\s+"), " ").trim()
                            val cleanArtist = if (artist.isBlank()) "Necunoscut" else artist.replace("\r", "").replace("\n", " ").replace(Regex("\\s+"), " ").trim()
                            val cleanKey = effectiveKey.ifBlank { key }.replace("\r", "").replace("\n", "").trim()

                            val savedSong = Song(
                                id = initialSong?.id ?: UUID.randomUUID().toString(),
                                title = cleanTitle,
                                artist = cleanArtist,
                                key = cleanKey,
                                tempo = tempo.toIntOrNull() ?: 70,
                                durationSeconds = duration.toIntOrNull() ?: 240,
                                status = status,
                                category = selectedCategory,
                                chords = chords.trim(),
                                lyrics = lyrics.trim(),
                                isFavorite = initialSong?.isFavorite ?: false,
                                usageCount = initialSong?.usageCount ?: 0
                            )
                            onSave(savedSong)
                        },
                        enabled = title.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = ISYCoral),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Salvează Cântarea")
                    }
                }
            }
        }
    }
}
