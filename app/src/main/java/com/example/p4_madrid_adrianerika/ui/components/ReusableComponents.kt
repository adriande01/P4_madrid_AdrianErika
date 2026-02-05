package com.example.p4_madrid_adrianerika.ui.components

import android.R.attr.contentDescription
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.p4_madrid_adrianerika.R

// Fun header app to show info about our app and hamburguer menu
@Composable
fun Header(){
    var isToggled by rememberSaveable { mutableStateOf(false) }

    Surface() {
        Row {
            // TITLE MADRID
            Text(
                text = stringResource(R.string.MADRID)
            )

            // SETTINGS ICON
            IconButton(
                onClick = { isToggled = !isToggled }
            ) {
                Icon(
                    imageVector = if (isToggled) Icons.Outlined.Settings else Icons.Filled.Settings,
                    contentDescription = if (isToggled) stringResource(R.string.T_IS_TOGGLED) else stringResource(R.string.T_IS_NOT_TOGGLED)
                )
            }

            // HAMBURGUER MENU
            IconButton(onClick = { /* Acción para abrir el menú */ }) {
                Icon(

                    painter = painterResource(id = R.drawable.ic_menu_colored),
                    contentDescription = "Menú",

                    tint = Color.Unspecified
                )
            }
        }
    }
}