package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theme.*
import com.example.viewmodel.WorshipViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: WorshipViewModel,
    modifier: Modifier = Modifier
) {
    var pushNotifications by remember { mutableStateOf(true) }
    var keepScreenOnInLiveMode by remember { mutableStateOf(true) }
    var preferRomanianNotes by remember { mutableStateOf(true) }
    var defaultKey by remember { mutableStateOf("Do") }
    var showClearDialog by remember { mutableStateOf(false) }
    var showRestoreDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        containerColor = ISYBlack,
        topBar = {
            TopAppBar(
                title = { Text("Profil & Setări", color = ISYWhite, fontWeight = FontWeight.Bold) },
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
            // Profile Info Header Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ISYSurface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(ISYCoral, ISYIndigo)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "AP",
                                color = ISYWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Andrei Popescu",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = ISYWhite
                        )
                        Text(
                            text = "Lider Worship • Admin",
                            fontSize = 13.sp,
                            color = ISYCoralLight,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Biserica ISYouth • Chișinău",
                            fontSize = 12.sp,
                            color = ISYGray300
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Stats Summary
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(ISYSurfaceElevated)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            ProfileStat(value = "14", label = "Programe")
                            ProfileStat(value = "38", label = "Cântări")
                            ProfileStat(value = "24", label = "Repetiții")
                        }
                    }
                }
            }

            // Preferences
            item {
                Text(
                    text = "PREFERINȚE APLICAȚIE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ISYGray300,
                    letterSpacing = 1.sp
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ISYSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Notificări Push", fontWeight = FontWeight.Bold, color = ISYWhite)
                                Text("Alerte pentru repetiții și modificări de setlist", fontSize = 12.sp, color = ISYGray300)
                            }
                            Switch(
                                checked = pushNotifications,
                                onCheckedChange = { pushNotifications = it },
                                colors = SwitchDefaults.colors(checkedTrackColor = ISYCoral)
                            )
                        }

                        HorizontalDivider(color = ISYSurfaceElevated)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Ecran activ permanent în Live Mode", fontWeight = FontWeight.Bold, color = ISYWhite)
                                Text("Împiedică blocarea ecranului pe scenă", fontSize = 12.sp, color = ISYGray300)
                            }
                            Switch(
                                checked = keepScreenOnInLiveMode,
                                onCheckedChange = { keepScreenOnInLiveMode = it },
                                colors = SwitchDefaults.colors(checkedTrackColor = ISYCoral)
                            )
                        }

                        HorizontalDivider(color = ISYSurfaceElevated)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Nomenclatură acorduri", fontWeight = FontWeight.Bold, color = ISYWhite)
                                Text("Do, Re, Mi (Română) vs C, D, E", fontSize = 12.sp, color = ISYGray300)
                            }
                            Switch(
                                checked = preferRomanianNotes,
                                onCheckedChange = { preferRomanianNotes = it },
                                colors = SwitchDefaults.colors(checkedTrackColor = ISYCoral)
                            )
                        }
                    }
                }
            }

            // Data Management Section
            item {
                Text(
                    text = "GESTIUNE DATE & REPERTORIU",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ISYGray300,
                    letterSpacing = 1.sp
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ISYSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Control asupra datelor aplicației",
                            color = ISYWhite,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Dacă dorești să introduci doar cântările și membrii tăi proprii, poți curăța datele demonstrative sau le poți restaura oricând.",
                            color = ISYGray300,
                            fontSize = 12.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showClearDialog = true },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ISYError),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.DeleteSweep, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Curăță Demo", fontSize = 12.sp)
                            }

                            Button(
                                onClick = { showRestoreDialog = true },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = ISYSurfaceElevated),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Restore, contentDescription = null, modifier = Modifier.size(16.dp), tint = ISYCoralLight)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Reîncarcă Demo", fontSize = 12.sp, color = ISYCoralLight)
                            }
                        }
                    }
                }
            }

            // About / App details
            item {
                Text(
                    text = "DESPRE APLICAȚIE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ISYGray300,
                    letterSpacing = 1.sp
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ISYSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Versiune", color = ISYGray300, fontSize = 13.sp)
                            Text("ISYouth Worship 1.0.0", color = ISYWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Platformă", color = ISYGray300, fontSize = 13.sp)
                            Text("Android Native • Jetpack Compose", color = ISYCoralLight, fontSize = 13.sp)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Dezvoltator", color = ISYGray300, fontSize = 13.sp)
                            Text("Echipa Media ISYouth", color = ISYWhite, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        if (showClearDialog) {
            AlertDialog(
                onDismissRequest = { showClearDialog = false },
                containerColor = ISYSurface,
                title = { Text("Golește datele demonstrative", color = ISYWhite, fontWeight = FontWeight.Bold) },
                text = {
                    Text(
                        "Această acțiune va șterge piesele, programele și membrii demonstrativi, lăsând aplicația curată pentru datele introduse de tine. Dorești să continui?",
                        color = ISYGray300
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.clearAllDemoData()
                            showClearDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ISYError)
                    ) {
                        Text("Da, curăță tot")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearDialog = false }) {
                        Text("Anulează", color = ISYGray300)
                    }
                }
            )
        }

        if (showRestoreDialog) {
            AlertDialog(
                onDismissRequest = { showRestoreDialog = false },
                containerColor = ISYSurface,
                title = { Text("Reîncarcă datele demo", color = ISYWhite, fontWeight = FontWeight.Bold) },
                text = {
                    Text(
                        "Dorești să reîncarci setul demonstrativ de cântări, programe și membri?",
                        color = ISYGray300
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.resetToSampleData()
                            showRestoreDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ISYCoral)
                    ) {
                        Text("Reîncarcă")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showRestoreDialog = false }) {
                        Text("Anulează", color = ISYGray300)
                    }
                }
            )
        }
    }
}

@Composable
private fun ProfileStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = ISYCoral)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, fontSize = 12.sp, color = ISYGray300)
    }
}
