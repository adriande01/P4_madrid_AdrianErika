package com.example.p4_madrid_adrianerika.ui.screens

import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.p4_madrid_adrianerika.models.Place
import com.example.p4_madrid_adrianerika.models.Type
import com.example.p4_madrid_adrianerika.ui.viewmodels.ViewModel

// Function in home screen that shows every type card and manages clic events
@Composable
fun ListScreen(
    type: String,
    onListClick: (Place) -> Unit
) {
    // 1. Get get the list of Places filtered (restaurants, cinemas, parks)

    val listFiltered = ViewModel().getAllPlacesFiltered(type)

    // Iterate over listFiltered to generate the cards
    listFiltered.forEach { place ->
        Card(onClick = { onListClick(place) }) {
            Text(text = stringResource(place.name))
        }
    }
}
