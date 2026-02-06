package com.example.p4_madrid_adrianerika.ui.components

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.p4_madrid_adrianerika.R
import androidx.core.net.toUri

// Fun header app to show info about our app and hamburguer menu
@Composable
fun Header(isDarkMode: Boolean, onToggleDarkMode: () -> Unit) {
    val context = LocalContext.current
    var settingsMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var hamburgerMenuExpanded by rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        color = MaterialTheme.colorScheme.primary

    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TITLE MADRID
            Text(
                text = stringResource(R.string.MADRID),
                style = MaterialTheme.typography.headlineMedium
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                // SETTINGS ICON
                IconButton(
                    onClick = {
                        settingsMenuExpanded = !settingsMenuExpanded
                    }

                ) {
                    Icon(
                        imageVector = if (settingsMenuExpanded) Icons.Filled.Settings else Icons.Outlined.Settings,
                        contentDescription = if (settingsMenuExpanded) stringResource(R.string.T_IS_TOGGLED) else stringResource(
                            R.string.T_IS_NOT_TOGGLED
                        )
                    )
                }

                // Dropdown Settings
                DropdownMenu(
                    expanded = settingsMenuExpanded,
                    onDismissRequest = { settingsMenuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(if (isDarkMode) "Modo Claro" else "Modo Oscuro")
                        },
                        onClick = {
                        onToggleDarkMode()
                        settingsMenuExpanded = false
                    })
                }

                // HAMBURGUER MENU
                IconButton(onClick = {
                    hamburgerMenuExpanded = !hamburgerMenuExpanded
                }) {
                    Icon(

                        painter = painterResource(id = R.drawable.ic_menu_colored),
                        contentDescription = "Menú",

                        tint = Color.Unspecified
                    )
                    DropdownMenu(
                        expanded = hamburgerMenuExpanded,
                        onDismissRequest = { hamburgerMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("About Us") },
                            onClick = {
                                hamburgerMenuExpanded = false
                                // Abre el link que quieras
                                val url = "https://github.com/adriande01/P4_madrid_AdrianErika.git"
                                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}