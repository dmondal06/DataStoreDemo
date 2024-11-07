package edu.farmingdale.datastoredemo.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Define custom colors
private val LightPrimary = Color(0xFFDAD4EF) // Lavender background
private val LightCardColor = Color(0xFF9B7BD2) // Purple for emoji boxes in light mode

private val DarkPrimary = Color(0xFF210203) // Licorice background
private val DarkCardColor = Color(0xFFDAD4EF) // Lighter purple for emoji boxes in dark mode

// Define color schemes for dark and light themes
private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    background = DarkPrimary,
    surface = DarkCardColor, // Emoji box color in dark mode
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    background = LightPrimary,
    surface = LightCardColor, // Emoji box color in light mode
    onPrimary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.White
)

@Composable
fun DataStoreDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),

    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
