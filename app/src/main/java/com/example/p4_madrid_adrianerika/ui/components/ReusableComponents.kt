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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.p4_madrid_adrianerika.R
import androidx.core.net.toUri
import com.example.p4_madrid_adrianerika.ui.AppStrings
import com.example.p4_madrid_adrianerika.ui.tr
import com.example.p4_madrid_adrianerika.ui.viewmodels.ViewModel

// Fun header app to show info about our app and hamburguer menu
@Composable
fun Header(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    viewModel: ViewModel,
    onLogout: () -> Unit,
    language: String = "EN"  // current app language from ViewModel
) {
    // Get the context
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(top = 20.dp),
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
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                // SETTINGS ICON
                IconButton(
                    onClick = {
                        // Call toggle settings menu expanded from ViewModel
                        viewModel.settingsMenuExpanded = !viewModel.settingsMenuExpanded
                    }
                ) {
                    Icon(
                        imageVector = if (viewModel.settingsMenuExpanded) Icons.Filled.Settings else Icons.Outlined.Settings,
                        contentDescription = if (viewModel.settingsMenuExpanded) stringResource(R.string.T_IS_TOGGLED) else stringResource(R.string.T_IS_NOT_TOGGLED),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }

                // Dropdown Settings
                DropdownMenu(
                    expanded = viewModel.settingsMenuExpanded,
                    onDismissRequest = { viewModel.settingsMenuExpanded = false }
                ) {
                    // Dark/Light mode toggle
                    DropdownMenuItem(
                        text = { Text(if (isDarkMode) AppStrings.lightMode.tr(language) else AppStrings.darkMode.tr(language)) },
                        onClick = {
                            onToggleDarkMode()
                            viewModel.settingsMenuExpanded = false
                        }
                    )
                    // Red theme option
                    DropdownMenuItem(
                        text = { Text(AppStrings.redTheme.tr(language)) },
                        onClick = {
                            viewModel.setTheme("RED")
                            viewModel.settingsMenuExpanded = false
                        }
                    )
                    // Blue theme option
                    DropdownMenuItem(
                        text = { Text(AppStrings.blueTheme.tr(language)) },
                        onClick = {
                            viewModel.setTheme("BLUE")
                            viewModel.settingsMenuExpanded = false
                        }
                    )
                    // Pink theme option
                    DropdownMenuItem(
                        text = { Text(AppStrings.pinkTheme.tr(language)) },
                        onClick = {
                            viewModel.setTheme("PINK")
                            viewModel.settingsMenuExpanded = false
                        }
                    )
                    // Logout option — only shown when a user is logged in
                    if (viewModel.currentUserId != null) {
                        DropdownMenuItem(
                            text = { Text(AppStrings.logout.tr(language)) },
                            onClick = {
                                viewModel.settingsMenuExpanded = false
                                onLogout()
                            }
                        )
                    }
                }

                // HAMBURGUER MENU
                IconButton(onClick = {
                    viewModel.hamburgerMenuExpanded = !viewModel.hamburgerMenuExpanded
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu_colored),
                        contentDescription = stringResource(R.string.MENU),
                        tint = Color.Unspecified
                    )
                    DropdownMenu(
                        expanded = viewModel.hamburgerMenuExpanded,
                        onDismissRequest = { viewModel.hamburgerMenuExpanded = false }
                    ) {
                        // Language toggle, switches between English and Spanish
                        DropdownMenuItem(
                            text = { Text(AppStrings.languageButton.tr(viewModel.language)) },
                            onClick = {
                                viewModel.toggleLanguage()
                                viewModel.hamburgerMenuExpanded = false
                            }
                        )
                        // About us, opens GitHub repo
                        DropdownMenuItem(
                            text = { Text(AppStrings.aboutUs.tr(language)) },
                            onClick = {
                                viewModel.hamburgerMenuExpanded = false
                                val url = context.getString(R.string.GB)
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