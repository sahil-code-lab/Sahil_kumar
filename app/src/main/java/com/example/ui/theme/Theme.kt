package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val FitForgeDarkColorScheme = darkColorScheme(
    primary = NeonGreenPrimary,
    onPrimary = PureBlack,
    primaryContainer = NeonGreenDark,
    onPrimaryContainer = TextWhite,
    secondary = ElectricCyan,
    onSecondary = PureBlack,
    secondaryContainer = DarkSurfaceElevated,
    onSecondaryContainer = ElectricCyan,
    tertiary = HighProteinOrange,
    onTertiary = PureBlack,
    background = DarkBackground,
    onBackground = TextWhite,
    surface = DarkSurface,
    onSurface = TextWhite,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = DarkSurfaceBorder,
    error = FlameRed,
    onError = TextWhite
)

@Composable
fun FitForgeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FitForgeDarkColorScheme,
        typography = Typography,
        content = content
    )
}
