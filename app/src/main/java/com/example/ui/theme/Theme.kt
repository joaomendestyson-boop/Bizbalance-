package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BizGreenAccent,
    onPrimary = Color.White,
    primaryContainer = BizGreenSecondary,
    onPrimaryContainer = Color.White,
    secondary = BizGreenPrimary,
    onSecondary = Color.White,
    background = BizSlateDarkBg,
    surface = BizSlateCardDark,
    onBackground = BizSlateTextDark,
    onSurface = BizSlateTextDark,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF475569)
)

private val LightColorScheme = lightColorScheme(
    primary = BizGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = BizGreenSecondary,
    onPrimaryContainer = Color.White,
    secondary = BizGreenSecondary,
    onSecondary = Color.White,
    background = BizLightBg,
    surface = BizLightSurface,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextSecondaryMuted,
    outline = BorderLight
)

@Composable
fun BizBalanceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
