package com.researchflow.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = ResearchBlueLight,
    onPrimary = ResearchDarkBackground,

    secondary = ResearchTealLight,
    onSecondary = ResearchDarkBackground,

    tertiary = ResearchBlueLight,
    onTertiary = ResearchDarkBackground,

    background = ResearchDarkBackground,
    onBackground = ResearchDarkTextPrimary,

    surface = ResearchDarkSurface,
    onSurface = ResearchDarkTextPrimary,

    surfaceVariant = ResearchDarkSurfaceVariant,
    onSurfaceVariant = ResearchDarkTextSecondary
)

private val LightColorScheme = lightColorScheme(
    primary = ResearchBlue,
    onPrimary = ResearchSurface,

    secondary = ResearchTeal,
    onSecondary = ResearchSurface,

    tertiary = ResearchBlueDark,

    background = ResearchBackground,
    onBackground = ResearchTextPrimary,

    surface = ResearchSurface,
    onSurface = ResearchTextPrimary,

    surfaceVariant = ResearchSurfaceVariant,
    onSurfaceVariant = ResearchTextSecondary
)

@Composable
fun ResearchFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}