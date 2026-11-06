package com.example.study_hub

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

enum class StudyHubTab {
    HOME,
    PLANNER,
    LEARN,
    FOCUS,
    SETTINGS
}

@Composable
fun StudyHubBottomNavigation(
    selectedTab: StudyHubTab,
    onTabSelected: (StudyHubTab) -> Unit
) {

    NavigationBar {

        NavigationBarItem(
            selected = selectedTab == StudyHubTab.HOME,
            onClick = {
                onTabSelected(StudyHubTab.HOME)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            label = {
                Text("Home")
            }
        )

        NavigationBarItem(
            selected = selectedTab == StudyHubTab.PLANNER,
            onClick = {
                onTabSelected(StudyHubTab.PLANNER)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Planner"
                )
            },
            label = {
                Text("Planner")
            }
        )

        NavigationBarItem(
            selected = selectedTab == StudyHubTab.LEARN,
            onClick = {
                onTabSelected(StudyHubTab.LEARN)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = "Learn"
                )
            },
            label = {
                Text("Learn")
            }
        )

        NavigationBarItem(
            selected = selectedTab == StudyHubTab.FOCUS,
            onClick = {
                onTabSelected(StudyHubTab.FOCUS)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = "Focus"
                )
            },
            label = {
                Text("Focus")
            }
        )

        NavigationBarItem(
            selected = selectedTab == StudyHubTab.SETTINGS,
            onClick = {
                onTabSelected(StudyHubTab.SETTINGS)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            },
            label = {
                Text("Settings")
            }
        )
    }
}