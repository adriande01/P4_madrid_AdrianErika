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
    onPrimary = Color.White,

    secondary = RedDarkSecondary,
    onSecondary = Color.White,

    tertiary = RedDarkTertiary,
    onTertiary = Color.White,

    background = RedDarkBackground,
    onBackground = Color.White
)

val LightColorScheme = lightColorScheme(
    primary = RedLightPrimary,
    onPrimary = Color.Black,

    secondary = RedLightSecondary,
    onSecondary = Color.Black,

    tertiary = RedLightTertiary,
    onTertiary = Color.Black,

    background = RedLightBackground,
    onBackground = Color.Black
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
                onPrimary = Color.White,

                secondary = BlueDarkSecondary,
                onSecondary = Color.White,

                tertiary = BlueDarkTertiary,
                onTertiary = Color.White,

                background = BlueDarkBackground,
                onBackground = Color.White
            )
        } else {
            lightColorScheme(
                primary = BlueLightPrimary,
                onPrimary = Color.Black,

                secondary = BlueLightSecondary,
                onSecondary = Color.Black,

                tertiary = BlueLightTertiary,
                onTertiary = Color.Black,

                background = BlueLightBackground,
                onBackground = Color.Black
            )
        }

        "PINK" -> if (darkTheme) {
            darkColorScheme(
                primary = PinkDarkPrimary,
                onPrimary = Color.White,

                secondary = PinkDarkSecondary,
                onSecondary = Color.White,

                tertiary = PinkDarkTertiary,
                onTertiary = Color.White,

                background = PinkDarkBackground,
                onBackground = Color.White
            )
        } else {
            lightColorScheme(
                primary = PinkLightPrimary,
                onPrimary = Color.Black,

                secondary = PinkLightSecondary,
                onSecondary = Color.Black,

                tertiary = PinkLightTertiary,
                onTertiary = Color.Black,

                background = PinkLightBackground,
                onBackground = Color.Black
            )
        }

        else -> if (darkTheme) { // Default red theme
            DarkColorScheme
        } else {
           LightColorScheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}