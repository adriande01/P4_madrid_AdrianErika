package com.example.p4_madrid_adrianerika.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.p4_madrid_adrianerika.R
import com.example.p4_madrid_adrianerika.models.Place
import com.example.p4_madrid_adrianerika.models.SubType
import com.example.p4_madrid_adrianerika.ui.AppStrings
import com.example.p4_madrid_adrianerika.ui.tr
import com.example.p4_madrid_adrianerika.ui.viewmodels.ViewModel

// List screen — shows places grouped by subtype with filters and animated cards
@Composable
fun ListScreen(
    type: String,
    // Same instance for all screens
    myViewModel: ViewModel,
    onListClick: (Place) -> Unit,
    language: String = "EN"  // current app language from ViewModel
) {
    // Load places from Room/API when screen opens
    LaunchedEffect(type) {
        myViewModel.loadPlaces(type)
    }

    // Track screen load for staggered animations
    var screenLoaded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { screenLoaded = true }

    // Get the map of Places grouped by SubType through ViewModel
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

    // Fade-in animation for the whole screen
    val screenAlpha by animateFloatAsState(
        targetValue = if (screenLoaded) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "screenFade"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Decorative top gradient — same style as Home/Login screens
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(screenAlpha)
        ) {
            // FILTER SECTION
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
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                            .clickable { myViewModel.filterMenuExpanded = true }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = AppStrings.filter.tr(language),
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(R.string.FILTER_MENU),
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onBackground
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
                            .background(
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.shapes.medium
                            )
                    ) {
                        // Favorites filter item
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = myViewModel.showOnlyFavorites,
                                        onCheckedChange = null,
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                    Text(
                                        AppStrings.favorites.tr(language),
                                        modifier = Modifier.padding(start = 8.dp),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            },
                            onClick = { myViewModel.showOnlyFavorites = !myViewModel.showOnlyFavorites }
                        )

                        // Dynamic SubType filters
                        availableSubTypes.forEach { sub ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(
                                            checked = myViewModel.selectedSubTypes.contains(sub),
                                            onCheckedChange = null,
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = MaterialTheme.colorScheme.primary
                                            )
                                        )
                                        Text(
                                            stringResource(sub.subTitle),
                                            modifier = Modifier.padding(start = 8.dp),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                },
                                onClick = { myViewModel.toggleSubTypeFilter(sub) }
                            )
                        }
                    }
                }
            }

            // Loading indicator while places are being fetched
            if (myViewModel.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                // LazyColumn to display sections
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    var globalIndex = 0
                    // Iterate over each group (SubType)
                    groupedPlaces.forEach { (subType, places) ->

                        // Header for the SubType with decorative bar
                        item {
                            Column(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)) {
                                Text(
                                    text = stringResource(subType.subTitle),
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        fontStyle = FontStyle.Italic
                                    )
                                )
                                // Decorative underline bar matching HomeScreen style
                                Box(
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                        .width(40.dp)
                                        .height(3.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = RoundedCornerShape(2.dp)
                                        )
                                )
                            }
                        }

                        // Animated cards for each place in this SubType
                        itemsIndexed(places) { index, place ->
                            val cardIndex = globalIndex++
                            PlaceCard(
                                place = place,
                                isFav = myViewModel.favoriteIds.contains(place.id),
                                index = cardIndex,
                                screenLoaded = screenLoaded,
                                onClick = { onListClick(place) }
                            )
                        }

                        // Space between sections
                        item { Spacer(modifier = Modifier.height(8.dp)) }
                    }
                }
            }
        }
    }
}

// Individual place card with staggered entrance animation
@Composable
fun PlaceCard(
    place: Place,
    isFav: Boolean,
    index: Int,
    screenLoaded: Boolean,
    onClick: () -> Unit
) {
    // Staggered fade-in per card
    val alpha by animateFloatAsState(
        targetValue = if (screenLoaded) 1f else 0f,
        animationSpec = tween(
            durationMillis = 400,
            delayMillis = index * 60  // slight delay between each card
        ),
        label = "cardFade"
    )

    // Staggered slide-in from right
    val offsetX by animateDpAsState(
        targetValue = if (screenLoaded) 0.dp else 40.dp,
        animationSpec = tween(
            durationMillis = 400,
            delayMillis = index * 60,
            easing = EaseOutCubic
        ),
        label = "cardSlide"
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = offsetX)
            .alpha(alpha),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = place.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                // Show address as subtitle
                Text(
                    text = place.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            // Favorite star icon
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