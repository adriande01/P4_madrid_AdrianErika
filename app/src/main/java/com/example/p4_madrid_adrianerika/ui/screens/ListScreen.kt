package com.example.p4_madrid_adrianerika.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.p4_madrid_adrianerika.R
import com.example.p4_madrid_adrianerika.models.Place
import com.example.p4_madrid_adrianerika.models.SubType
import com.example.p4_madrid_adrianerika.ui.viewmodels.ViewModel

// Function in list screen that shows places grouped by subtype and manages click events
@Composable
fun ListScreen(
    type: String,
    // Same instance for all screens
    myViewModel: ViewModel,
    onListClick: (Place) -> Unit
) {
    // 1. Get ViewModel (NOT NOW CAUSE WE USE THE SAME INSTANCE IN ALL SCREENS)
    // val myViewModel: ViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    // 2. Get the map of Places grouped by SubType throw ViewModel
    val groupedPlaces = myViewModel.getFilteredPlacesGrouped(type).mapValues { entry ->
        entry.value.filter { place ->
            val matchesFav =
                if (myViewModel.showOnlyFavorites) myViewModel.favoriteIds.contains(place.id) else true
            val matchesSub =
                if (myViewModel.selectedSubTypes.isNotEmpty()) myViewModel.selectedSubTypes.contains(
                    place.subType
                ) else true
            matchesFav && matchesSub
        }
        // If there is nothing show nothing
    }.filter { it.value.isNotEmpty() }

    // Get available subtypes for the current category to show in the filter
    val availableSubTypes = SubType.values().filter { it.parentType.name == type }

    Column(modifier = Modifier.fillMaxSize()) {

        // FILTER SECTION: Styled to match the sketch (FILTER text + Icon)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, end = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                // Row made clickable to trigger the menu from both text and icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { myViewModel.filterMenuExpanded = true }
                ) {
                    Text(
                        text = "FILTER", // Updated to "FILTER" as requested
                        fontSize = 18.sp, // Slightly bigger
                        fontStyle = FontStyle.Italic, // Italic style as shown in image
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = stringResource(R.string.FILTER_MENU),
                        modifier = Modifier.size(28.dp) // Icon size adjusted
                    )
                }

                // Dropdown menu for filters
                DropdownMenu(
                    expanded = myViewModel.filterMenuExpanded,
                    onDismissRequest = { myViewModel.filterMenuExpanded = false },
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.medium
                        )
                        .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium)

                ) {
                    // Favorites filter item
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = myViewModel.showOnlyFavorites,
                                    onCheckedChange = null
                                )
                                Text(
                                    stringResource(R.string.FAVORITES),
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        },
                        onClick = { myViewModel.showOnlyFavorites = !myViewModel.showOnlyFavorites },


                    )

                    // Dynamic SubType filters
                    availableSubTypes.forEach { sub ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = myViewModel.selectedSubTypes.contains(sub),
                                        onCheckedChange = null
                                    )
                                    Text(
                                        stringResource(sub.subTitle),
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            },
                            onClick = { myViewModel.toggleSubTypeFilter(sub) }
                        )
                    }
                }
            }
        }

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
                    // Get if the card is toggled fav throw viewModel instance
                    val isFav = myViewModel.favoriteIds.contains(place.id)
                    Card(
                        onClick = { onListClick(place) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        border = BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )

                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(place.name),
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSecondary

                            )
                            if (isFav) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }

                // Space between sections
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}