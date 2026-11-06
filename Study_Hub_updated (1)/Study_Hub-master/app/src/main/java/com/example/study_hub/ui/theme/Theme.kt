
package com.example.study_hub.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val StudyHubLightColors = lightColorScheme(
    primary = TurquoiseBlue,
    onPrimary = LightBlueText,

    secondary = DeepBlue,
    onSecondary = LightBlueText,

    tertiary = LightBlue,
    onTertiary = BlueText,

    background = BlueBackground,
    onBackground = BlueText,

    surface = BlueSurface,
    onSurface = BlueText,

    surfaceVariant = LightBlue,
    onSurfaceVariant = MutedBlueText
)

private val StudyHubDarkColors = darkColorScheme(
    primary = SoftBlue,
    onPrimary = DeepBlue,

    secondary = TurquoiseBlue,
    onSecondary = LightBlueText,

    tertiary = LightBlue,
    onTertiary = BlueText,

    background = DarkBlueBackground,
    onBackground = LightBlueText,

    surface = DarkBlueSurface,
    onSurface = LightBlueText,

    surfaceVariant = Color(0xFF31546A),
    onSurfaceVariant = Color(0xFFD5E6F0)
)

@Composable
fun Study_HubTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (darkTheme) {
            StudyHubDarkColors
        } else {
            StudyHubLightColors
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}