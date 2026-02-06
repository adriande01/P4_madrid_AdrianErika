package com.example.p4_madrid_adrianerika.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.p4_madrid_adrianerika.models.Place
import com.example.p4_madrid_adrianerika.ui.viewmodels.ViewModel

// Function in list screen that shows places grouped by subtype and manages click events
@Composable
fun ListScreen(
    type: String,
    onListClick: (Place) -> Unit
) {
    // 1. Get ViewModel
    val myViewModel: ViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    // 2. Get the map of Places grouped by SubType
    val groupedPlaces = myViewModel.getPlacesGroupedBySubtype(type)

    // LazyColumn to display sections
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Iterate over each group (SubType)
        groupedPlaces.forEach { (subType, places) ->

            // Header for the SubType (Underlined)
            item {
                Text(
                    text = stringResource(subType.subTitle),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }

            // Cards for each place in this specific SubType
            items(places) { place ->
                Card(
                    onClick = { onListClick(place) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(place.name),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // Space between sections
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}