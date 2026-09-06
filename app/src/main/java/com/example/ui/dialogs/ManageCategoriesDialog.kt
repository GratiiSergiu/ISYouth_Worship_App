package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theme.*
import com.example.viewmodel.WorshipViewModel

@Composable
fun ManageCategoriesDialog(
    viewModel: WorshipViewModel,
    onDismiss: () -> Unit
) {
    val categories by viewModel.categories.collectAsState()
    var newCategoryText by remember { mutableStateOf("") }
    var editingCategory by remember { mutableStateOf<String?>(null) }
    var editCategoryText by remember { mutableStateOf("") }

    val protectedCategories = setOf("Toate", "Favorite", "Repertoriu")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ISYSurface,
        title = {
            Text(
                text = "Gestionare Categorii",
                color = ISYWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Adaugă, redenumește sau elimină categorii pentru organizarea repertoriului.",
                    color = ISYGray300,
                    fontSize = 12.sp
                )

                // Add new category row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = newCategoryText,
                        onValueChange = { newCategoryText = it },
                        placeholder = { Text("Ex: Închinare, Lente, Crăciun...", color = ISYGray500, fontSize = 13.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = ISYWhite,
                            unfocusedTextColor = ISYWhite,
                            focusedBorderColor = ISYCoral
                        )
                    )
                    Button(
                        onClick = {
                            if (newCategoryText.isNotBlank()) {
                                viewModel.addCategory(newCategoryText.trim())
                                newCategoryText = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ISYCoral),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Adaugă", tint = ISYWhite)
                    }
                }

                HorizontalDivider(color = ISYSurfaceElevated)

                // List of existing categories
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories.filter { it != "Toate" && it != "Favorite" }) { category ->
                        val isProtected = protectedCategories.contains(category)
                        val isEditing = editingCategory == category

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ISYSurfaceElevated)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            if (isEditing) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = editCategoryText,
                                        onValueChange = { editCategoryText = it },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = ISYWhite,
                                            unfocusedTextColor = ISYWhite,
                                            focusedBorderColor = ISYCoral
                                        )
                                    )
                                    IconButton(
                                        onClick = {
                                            if (editCategoryText.isNotBlank()) {
                                                viewModel.updateCategory(category, editCategoryText.trim())
                                            }
                                            editingCategory = null
                                        }
                                    ) {
                                        Icon(imageVector = Icons.Default.Check, contentDescription = "Salvează", tint = ISYSuccess)
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = category,
                                            color = ISYWhite,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 14.sp
                                        )
                                        if (isProtected) {
                                            Text(
                                                text = "Categorie implicită",
                                                color = ISYGray500,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }

                                    if (!isProtected) {
                                        Row {
                                            IconButton(
                                                onClick = {
                                                    editingCategory = category
                                                    editCategoryText = category
                                                },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = "Editează",
                                                    tint = ISYGray300,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            IconButton(
                                                onClick = { viewModel.deleteCategory(category) },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Șterge",
                                                    tint = ISYError,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = ISYCoral)
            ) {
                Text("Gata", color = ISYWhite)
            }
        }
    )
}
