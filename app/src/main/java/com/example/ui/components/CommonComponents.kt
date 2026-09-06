package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theme.*

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (label, bg, textColor) = when (status.lowercase()) {
        "ready", "programat" -> Triple("PROGRAMAT", ISYCoral.copy(alpha = 0.2f), ISYCoral)
        "completed", "completat" -> Triple("COMPLETAT", ISYSuccess.copy(alpha = 0.2f), ISYSuccess)
        "repertoire" -> Triple("REPERTORIU", ISYSuccess.copy(alpha = 0.2f), ISYSuccess)
        "learning", "în lucru" -> Triple("ÎN LUCRU", ISYWarning.copy(alpha = 0.2f), ISYWarning)
        "new", "nou" -> Triple("NOU", ISYCoralLight.copy(alpha = 0.2f), ISYCoralLight)
        else -> Triple(status.uppercase(), ISYWarning.copy(alpha = 0.2f), ISYWarning)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun SegmentBadge(
    segment: String,
    modifier: Modifier = Modifier
) {
    val (label, color) = when (segment.lowercase()) {
        "intro" -> Pair("Intro", ISYGray500)
        "worship" -> Pair("Worship", ISYCoral)
        "special" -> Pair("Moment Special", ISYIndigo)
        "sermon" -> Pair("Predică", ISYWarning)
        "sending" -> Pair("Trimitere", ISYSuccess)
        else -> Pair(segment.replaceFirstChar { it.uppercase() }, ISYIndigo)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.25f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (color == ISYIndigo) ISYCoralLight else color
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = ISYWhite
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ISYGray300
                )
            }
        }
        if (actionText != null && onAction != null) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.labelLarge,
                color = ISYCoral,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(onClick = onAction)
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = ISYGray300
        )
    }
}

@Composable
fun MemberAvatar(
    initial: String,
    primaryColor: Long,
    secondaryColor: Long,
    size: Dp = 40.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(primaryColor), Color(secondaryColor))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            color = ISYWhite,
            fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.42f).sp
        )
    }
}

@Composable
fun AccentCard(
    modifier: Modifier = Modifier,
    accentBrush: Brush = Brush.verticalGradient(listOf(ISYCoral, ISYIndigo)),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ISYSurface)
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick)
                else Modifier
            )
    ) {
        // Left accent bar
        Box(
            modifier = Modifier
                .width(4.dp)
                .matchParentSize()
                .background(accentBrush)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp, top = 16.dp, bottom = 16.dp),
            content = content
        )
    }
}

