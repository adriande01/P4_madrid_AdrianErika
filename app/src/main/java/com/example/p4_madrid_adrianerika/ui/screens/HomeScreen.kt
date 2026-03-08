package com.example.p4_madrid_adrianerika.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.p4_madrid_adrianerika.models.Type
import com.example.p4_madrid_adrianerika.ui.AppStrings
import com.example.p4_madrid_adrianerika.ui.tr

// Home screen — shows each place category as a full-width animated card
@Composable
fun HomeScreen(
    onCategoryClick: (Type) -> Unit,
    language: String = "EN"  // current app language from ViewModel
) {
    // Track if the screen has loaded to trigger staggered animations
    var screenLoaded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { screenLoaded = true }

    // Root box to layer the gradient behind the cards
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Decorative top gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 80.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Use itemsIndexed to get the index for staggered animation delay
            itemsIndexed(Type.entries) { index, type ->
                CategoryCard(
                    type = type,
                    index = index,
                    screenLoaded = screenLoaded,
                    language = language,
                    onCategoryClick = onCategoryClick
                )
            }
        }
    }
}

// Individual category card with entrance animation and press scale effect
@Composable
fun CategoryCard(
    type: Type,
    index: Int,
    screenLoaded: Boolean,
    language: String,
    onCategoryClick: (Type) -> Unit
) {
    // Staggered fade-in: each card waits longer based on its index
    val alpha by animateFloatAsState(
        targetValue = if (screenLoaded) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            delayMillis = index * 150  // 0ms, 150ms, 300ms for each card
        ),
        label = "cardFadeIn"
    )

    // Staggered slide-up: same delay as fade
    val offsetY by animateDpAsState(
        targetValue = if (screenLoaded) 0.dp else 60.dp,
        animationSpec = tween(
            durationMillis = 600,
            delayMillis = index * 150,
            easing = EaseOutCubic
        ),
        label = "cardSlideUp"
    )

    // Interaction source to detect press state on the card
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Track if card has been tapped — keeps bar expanded after single tap
    var isTapped by remember { mutableStateOf(false) }

    // Scale down slightly on press for tactile feedback
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "cardScale"
    )

    // Decorative bar expands when card is pressed or tapped
    val barWidth by animateDpAsState(
        targetValue = if (isPressed || isTapped) 400.dp else 120.dp,
        animationSpec = tween(durationMillis = 300),
        label = "barWidth"
    )

    Card(
        onClick = {
            isTapped = true  // Keep bar expanded after tap
            onCategoryClick(type)
        },
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(horizontal = 16.dp)
            .offset(y = offsetY)
            .alpha(alpha)
            .scale(scale),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // 1. Background image
            Image(
                painter = painterResource(id = type.img),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.85f
            )

            // 2. Dramatic gradient overlay — darker at bottom for text legibility
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.3f),
                                Color.Black.copy(alpha = 0.75f)
                            )
                        )
                    )
            )

            // 3. Category title at bottom left — translated based on current language
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                Text(
                    // Translate category name based on current language
                    text = when (type) {
                        Type.RESTAURANT -> AppStrings.restaurants.tr(language)
                        Type.CINEMA -> AppStrings.cinemas.tr(language)
                        Type.PARK -> AppStrings.parks.tr(language)
                    },
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = 1.sp
                )
                // Decorative bar — animates wider when card is pressed or tapped
                Box(
                    modifier = Modifier
                        .width(barWidth)
                        .height(3.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}