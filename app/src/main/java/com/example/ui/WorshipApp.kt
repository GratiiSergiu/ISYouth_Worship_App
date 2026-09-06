package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Screen
import com.example.theme.*
import com.example.ui.screens.*
import com.example.viewmodel.WorshipViewModel

data class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val screen: Screen
)

@Composable
fun WorshipApp(
    viewModel: WorshipViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    // Handle system back button
    BackHandler(enabled = currentScreen != Screen.Dashboard) {
        viewModel.navigateBack()
    }

    val bottomNavItems = listOf(
        BottomNavItem(
            title = "Acasă",
            selectedIcon = Icons.Default.Home,
            unselectedIcon = Icons.Outlined.Home,
            screen = Screen.Dashboard
        ),
        BottomNavItem(
            title = "Cântări",
            selectedIcon = Icons.Default.LibraryMusic,
            unselectedIcon = Icons.Outlined.LibraryMusic,
            screen = Screen.Songs
        ),
        BottomNavItem(
            title = "Setlist",
            selectedIcon = Icons.Default.PlaylistAddCheck,
            unselectedIcon = Icons.Outlined.PlaylistAddCheck,
            screen = Screen.SetlistBuilder(null)
        ),
        BottomNavItem(
            title = "Echipa",
            selectedIcon = Icons.Default.Groups,
            unselectedIcon = Icons.Outlined.Groups,
            screen = Screen.Team
        ),
        BottomNavItem(
            title = "Profil",
            selectedIcon = Icons.Default.Person,
            unselectedIcon = Icons.Outlined.Person,
            screen = Screen.Profile
        )
    )

    // Hide bottom bar in LiveMode for distraction-free full stage view
    val showBottomBar = currentScreen !is Screen.LiveMode

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("worship_app_root"),
        containerColor = ISYBlack,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = ISYSurface,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .testTag("bottom_nav_bar")
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = when (currentScreen) {
                            Screen.Dashboard -> item.screen is Screen.Dashboard
                            Screen.Songs -> item.screen is Screen.Songs
                            is Screen.SetlistBuilder -> item.screen is Screen.SetlistBuilder
                            Screen.Team -> item.screen is Screen.Team
                            Screen.Profile -> item.screen is Screen.Profile
                            else -> false
                        }

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.navigateTo(item.screen) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontSize = 11.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ISYWhite,
                                unselectedIconColor = ISYGray500,
                                selectedTextColor = ISYCoral,
                                unselectedTextColor = ISYGray500,
                                indicatorColor = ISYCoral
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
        ) {
            when (val screen = currentScreen) {
                Screen.Dashboard -> DashboardScreen(viewModel = viewModel)
                Screen.Songs -> SongsScreen(viewModel = viewModel)
                is Screen.SetlistBuilder -> SetlistBuilderScreen(programId = screen.programId, viewModel = viewModel)
                is Screen.LiveMode -> LiveModeScreen(programId = screen.programId, viewModel = viewModel)
                Screen.PracticeMode -> PracticeModeScreen(viewModel = viewModel)
                Screen.Calendar -> CalendarScreen(viewModel = viewModel)
                Screen.Statistics -> StatisticsScreen(viewModel = viewModel)
                Screen.Team -> TeamScreen(viewModel = viewModel)
                Screen.Notifications -> NotificationsScreen(viewModel = viewModel)
                Screen.Profile -> ProfileScreen(viewModel = viewModel)
            }
        }
    }
}
