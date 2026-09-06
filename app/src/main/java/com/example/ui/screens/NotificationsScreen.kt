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
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.NotificationItem
import com.example.model.Screen
import com.example.theme.*
import com.example.viewmodel.WorshipViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    viewModel: WorshipViewModel,
    modifier: Modifier = Modifier
) {
    val notifications by viewModel.notifications.collectAsState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("notifications_screen"),
        containerColor = ISYBlack,
        topBar = {
            TopAppBar(
                title = { Text("Notificări", color = ISYWhite, fontWeight = FontWeight.Bold) },
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
                    TextButton(onClick = { viewModel.markAllNotificationsAsRead() }) {
                        Icon(imageVector = Icons.Default.DoneAll, contentDescription = null, tint = ISYCoral, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Citite", color = ISYCoral)
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(notifications) { notif ->
                NotificationCard(
                    notification = notif,
                    onClick = {
                        viewModel.markNotificationAsRead(notif.id)
                        when (notif.action) {
                            "builder" -> viewModel.navigateTo(Screen.SetlistBuilder(null))
                            "practice" -> viewModel.navigateTo(Screen.PracticeMode)
                            "songs" -> viewModel.navigateTo(Screen.Songs)
                            "team" -> viewModel.navigateTo(Screen.Team)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun NotificationCard(
    notification: NotificationItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (!notification.isRead) ISYSurfaceElevated else ISYSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(notification.colorHex).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = notification.icon, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.SemiBold,
                        color = ISYWhite,
                        fontSize = 14.sp
                    )
                    Text(
                        text = notification.time,
                        color = ISYGray500,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notification.body,
                    color = if (!notification.isRead) ISYWhite else ISYGray300,
                    fontSize = 13.sp
                )
            }

            if (!notification.isRead) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(ISYCoral)
                )
            }
        }
    }
}
