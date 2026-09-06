package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TeamMember
import com.example.theme.*
import com.example.ui.components.MemberAvatar
import com.example.viewmodel.WorshipViewModel

val PREDEFINED_ROLES = listOf(
    "Leader",
    "Co-Leader",
    "Vocalist",
    "Chitară",
    "Chitară Bas",
    "Chitară Electrică",
    "Tobe",
    "Vioară",
    "Pian"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamScreen(
    viewModel: WorshipViewModel,
    modifier: Modifier = Modifier
) {
    val members by viewModel.teamMembers.collectAsState()
    var selectedFilter by remember { mutableStateOf("Toți") }
    var showAddMemberDialog by remember { mutableStateOf(false) }
    var editingMember by remember { mutableStateOf<TeamMember?>(null) }
    var memberToDelete by remember { mutableStateOf<TeamMember?>(null) }

    val filterOptions = listOf("Toți", "Vocal", "Instrumental", "Tehnic")

    val filteredMembers = members.filter { member ->
        when (selectedFilter) {
            "Vocal" -> member.role.contains("Vocal", ignoreCase = true) ||
                    member.role.contains("Leader", ignoreCase = true) ||
                    member.role.contains("Voce", ignoreCase = true)
            "Instrumental" -> member.role.contains("Chitară", ignoreCase = true) ||
                    member.role.contains("Pian", ignoreCase = true) ||
                    member.role.contains("Tobe", ignoreCase = true) ||
                    member.role.contains("Vioară", ignoreCase = true) ||
                    member.role.contains("Bas", ignoreCase = true)
            "Tehnic" -> member.badge == "Tehnic" || member.role.contains("Tehnic", ignoreCase = true) || member.role.contains("Sunet", ignoreCase = true)
            else -> true
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("team_screen"),
        containerColor = ISYBlack,
        contentWindowInsets = WindowInsets.statusBars,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddMemberDialog = true },
                containerColor = ISYCoral,
                contentColor = ISYWhite,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(bottom = 16.dp, end = 4.dp)
                    .testTag("add_team_member_fab")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Adaugă membru")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                text = "Echipa ISYouth Worship",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = ISYWhite,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
            )

            // Filter chips
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterOptions) { filter ->
                    val isSelected = selectedFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ISYCoral,
                            selectedLabelColor = ISYWhite,
                            containerColor = ISYSurface,
                            labelColor = ISYGray300
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "${filteredMembers.size} membri în echipă",
                fontSize = 12.sp,
                color = ISYGray300,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredMembers) { member ->
                    MemberCard(
                        member = member,
                        onEdit = { editingMember = member },
                        onDelete = { memberToDelete = member }
                    )
                }
            }
        }
    }

    if (showAddMemberDialog) {
        MemberEditDialog(
            initialMember = null,
            onDismiss = { showAddMemberDialog = false },
            onSave = { newMember ->
                viewModel.addTeamMember(newMember)
                showAddMemberDialog = false
            }
        )
    }

    editingMember?.let { member ->
        MemberEditDialog(
            initialMember = member,
            onDismiss = { editingMember = null },
            onSave = { updated ->
                viewModel.updateTeamMember(updated)
                editingMember = null
            }
        )
    }

    memberToDelete?.let { member ->
        AlertDialog(
            onDismissRequest = { memberToDelete = null },
            containerColor = ISYSurface,
            title = { Text("Elimină membru", color = ISYWhite, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Sigur dorești să elimini pe ${member.name} din echipă?",
                    color = ISYGray300
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteTeamMember(member.id)
                        memberToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ISYError)
                ) {
                    Text("Elimină", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { memberToDelete = null }) {
                    Text("Anulează", color = ISYGray300)
                }
            }
        )
    }
}

@Composable
private fun MemberCard(
    member: TeamMember,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ISYSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MemberAvatar(
                initial = member.initial,
                primaryColor = member.primaryColor,
                secondaryColor = member.secondaryColor,
                size = 46.dp
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = member.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = ISYWhite
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    val badgeBg = when (member.badge) {
                        "Admin" -> ISYCoral.copy(alpha = 0.2f)
                        "Tehnic" -> ISYIndigo.copy(alpha = 0.4f)
                        else -> ISYSuccess.copy(alpha = 0.2f)
                    }
                    val badgeColor = when (member.badge) {
                        "Admin" -> ISYCoral
                        "Tehnic" -> ISYCoralLight
                        else -> ISYSuccess
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(badgeBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = member.badge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = member.role,
                    fontSize = 13.sp,
                    color = ISYCoralLight,
                    fontWeight = FontWeight.Medium
                )
            }

            // Edit and delete actions
            IconButton(
                onClick = onEdit,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editează membru",
                    tint = ISYGray300,
                    modifier = Modifier.size(18.dp)
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Elimină membru",
                    tint = ISYError.copy(alpha = 0.8f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun MemberEditDialog(
    initialMember: TeamMember?,
    onDismiss: () -> Unit,
    onSave: (TeamMember) -> Unit
) {
    var name by remember { mutableStateOf(initialMember?.name ?: "") }
    var badge by remember { mutableStateOf(initialMember?.badge ?: "Membru") }

    // Parse existing roles into selected list
    val selectedRoles = remember {
        mutableStateListOf<String>().apply {
            if (initialMember != null && initialMember.role.isNotBlank()) {
                val parts = initialMember.role.split("•").map { it.trim() }.filter { it.isNotEmpty() }
                addAll(parts)
            }
        }
    }

    var customRoleInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ISYSurface,
        title = {
            Text(
                text = if (initialMember == null) "Adaugă Membru în Echipă" else "Editează Membru",
                color = ISYWhite,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        errorMessage = null
                    },
                    label = { Text("Nume complet") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ISYWhite,
                        unfocusedTextColor = ISYWhite,
                        focusedBorderColor = ISYCoral
                    )
                )

                Text(
                    text = "Alege Roluri (poți selecta 1-3 roluri):",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ISYGray300
                )

                // Predefined roles selection
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    val rows = PREDEFINED_ROLES.chunked(3)
                    rows.forEach { rowRoles ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            rowRoles.forEach { r ->
                                val isSelected = selectedRoles.contains(r)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        if (isSelected) {
                                            selectedRoles.remove(r)
                                        } else {
                                            if (selectedRoles.size < 4) {
                                                selectedRoles.add(r)
                                            }
                                        }
                                    },
                                    label = { Text(r, fontSize = 12.sp) },
                                    leadingIcon = if (isSelected) {
                                        {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    } else null,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = ISYCoral,
                                        selectedLabelColor = ISYWhite,
                                        containerColor = ISYSurfaceElevated,
                                        labelColor = ISYGray300
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                // Add custom role
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = customRoleInput,
                        onValueChange = { customRoleInput = it },
                        label = { Text("Alt rol manual") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = ISYWhite,
                            unfocusedTextColor = ISYWhite,
                            focusedBorderColor = ISYIndigo
                        )
                    )
                    Button(
                        onClick = {
                            val trimmed = customRoleInput.trim()
                            if (trimmed.isNotEmpty() && !selectedRoles.contains(trimmed)) {
                                selectedRoles.add(trimmed)
                                customRoleInput = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ISYIndigo),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("+")
                    }
                }

                // Display currently selected roles tags
                if (selectedRoles.isNotEmpty()) {
                    Text(
                        text = "Roluri selectate: " + selectedRoles.joinToString(" • "),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ISYCoralLight
                    )
                }

                Text(
                    text = "Tip / Nivel acces:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ISYGray300
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Membru", "Admin", "Tehnic").forEach { b ->
                        FilterChip(
                            selected = badge == b,
                            onClick = { badge = b },
                            label = { Text(b) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ISYCoral,
                                selectedLabelColor = ISYWhite,
                                containerColor = ISYSurfaceElevated,
                                labelColor = ISYGray300
                            )
                        )
                    }
                }

                errorMessage?.let { err ->
                    Text(text = err, color = ISYError, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        errorMessage = "Introdu numele complet."
                        return@Button
                    }
                    val finalRole = if (selectedRoles.isNotEmpty()) {
                        selectedRoles.joinToString(" • ")
                    } else {
                        "Membru Echipă"
                    }
                    val initial = name.first().uppercase()
                    val primaryColor = initialMember?.primaryColor ?: 0xFF4A3B6B
                    val secondaryColor = initialMember?.secondaryColor ?: 0xFFE85D5D
                    val id = initialMember?.id ?: "tm_${System.currentTimeMillis()}"

                    onSave(
                        TeamMember(
                            id = id,
                            name = name.trim(),
                            role = finalRole,
                            initial = initial,
                            badge = badge,
                            primaryColor = primaryColor,
                            secondaryColor = secondaryColor
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = ISYCoral)
            ) {
                Text(if (initialMember == null) "Adaugă" else "Salvează", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Anulează", color = ISYGray300)
            }
        }
    )
}
