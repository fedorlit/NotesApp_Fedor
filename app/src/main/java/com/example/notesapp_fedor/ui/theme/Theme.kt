package com.example.notesapp_fedor.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = AccentColor,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = SurfaceDark,
    surface = SurfaceDark,
    onPrimary = Color.Black,
    onBackground = OnSurfaceDark,
    onSurface = OnSurfaceDark,
    primaryContainer = PrimaryDark,
    onPrimaryContainer = OnSurfaceDark
)

// We'll use the same dark theme for both to satisfy the request
private val LightColorScheme = DarkColorScheme

@Composable
fun NotesApp_FedorTheme(
    darkTheme: Boolean = true, // Default to dark as requested
    dynamicColor: Boolean = false, // Disable dynamic color to use our custom colors
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}