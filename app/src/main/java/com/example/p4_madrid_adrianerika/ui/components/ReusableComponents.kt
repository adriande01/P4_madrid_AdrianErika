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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.p4_madrid_adrianerika.R
import androidx.core.net.toUri
import com.example.p4_madrid_adrianerika.ui.viewmodels.ViewModel

// Fun header app to show info about our app and hamburguer menu
@Composable
fun Header(isDarkMode: Boolean, onToggleDarkMode: () -> Unit, viewModel: ViewModel) {
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
                .padding(horizontal = 16.dp,),
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
                        // Call toggle settings menu expanded from ViewModel Sepiote
                        viewModel.settingsMenuExpanded = !viewModel.settingsMenuExpanded
                    },


                ) {
                    Icon(
                        imageVector = if (viewModel.settingsMenuExpanded) Icons.Filled.Settings else Icons.Outlined.Settings,
                        contentDescription = if (viewModel.settingsMenuExpanded) stringResource(R.string.T_IS_TOGGLED) else stringResource(
                            R.string.T_IS_NOT_TOGGLED
                        ),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }

                // Dropdown Settings
                DropdownMenu(
                    // Get expanded from ViewModel
                    expanded = viewModel.settingsMenuExpanded,
                    // Toggle expanded from ViewModel
                    onDismissRequest = { viewModel.settingsMenuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(if (isDarkMode) "Light Mode" else "Dark Mode") },
                        onClick = {
                            onToggleDarkMode()
                            viewModel.settingsMenuExpanded = false
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Red Theme") },
                        onClick = {
                            viewModel.setTheme("RED")
                            viewModel.settingsMenuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Blue Theme") },
                        onClick = {
                            viewModel.setTheme("BLUE")
                            viewModel.settingsMenuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Pink Theme") },
                        onClick = {
                            viewModel.setTheme("PINK")
                            viewModel.settingsMenuExpanded = false
                        }
                    )
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
                        DropdownMenuItem(
                            // Text from strings.xml
                            text = { Text(stringResource(R.string.ABOUT_US)) },
                            onClick = {
                                viewModel.hamburgerMenuExpanded = false
                                // URL GB form strings.xml
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