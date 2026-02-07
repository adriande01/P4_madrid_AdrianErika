package com.example.p4_madrid_adrianerika.ui.theme

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


val DarkColorScheme = darkColorScheme(
    primary = RedDarkPrimary,
    secondary = RedDarkSecondary,
    tertiary = RedDarkTertiary,
    background = RedDarkBackground
)

val LightColorScheme = lightColorScheme(
    primary = RedLightPrimary,
    secondary = RedLightSecondary,
    tertiary = RedLightTertiary,
    background = RedLightBackground
)


@Composable
fun P4_madrid_AdrianErikaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeName: String = "RED", // Default theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val colorScheme = when (themeName.uppercase()) {
        "BLUE" -> if (darkTheme) {
            darkColorScheme(
                primary = BlueDarkPrimary,
                secondary = BlueDarkSecondary,
                tertiary = BlueDarkTertiary,
                background = BlueDarkBackground
            )
        } else {
            lightColorScheme(
                primary = BlueLightPrimary,
                secondary = BlueLightSecondary,
                tertiary = BlueLightTertiary,
                background = BlueLightBackground
            )
        }

        "PINK" -> if (darkTheme) {
            darkColorScheme(
                primary = PinkDarkPrimary,
                secondary = PinkDarkSecondary,
                tertiary = PinkDarkTertiary,
                background = PinkDarkBackground
            )
        } else {
            lightColorScheme(
                primary = PinkLightPrimary,
                secondary = PinkLightSecondary,
                tertiary = PinkLightTertiary,
                background = PinkLightBackground
            )
        }

        else -> if (darkTheme) { // Default red theme
            darkColorScheme(
                primary = RedDarkPrimary,
                secondary = RedDarkSecondary,
                tertiary = RedDarkTertiary,
                background = RedDarkBackground
            )
        } else {
            lightColorScheme(
                primary = RedLightPrimary,
                secondary = RedLightSecondary,
                tertiary = RedLightTertiary,
                background = RedLightBackground
            )
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}