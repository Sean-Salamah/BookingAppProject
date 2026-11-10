package com.example.tusbookings.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val GoldBlackColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = BlackText,
    primaryContainer = GoldDark,
    onPrimaryContainer = WhiteText,
    secondary = GoldLight,
    onSecondary = BlackText,
    background = BlackBackground,
    onBackground = WhiteText,
    surface = BlackSurface,
    onSurface = WhiteText,
    surfaceVariant = BlackSurfaceVariant,
    onSurfaceVariant = GoldPrimary
)


@Composable
fun TUSBookingsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // themes dont get overridden
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = GoldBlackColorScheme,
        typography = Typography,
        content = content
    )
}