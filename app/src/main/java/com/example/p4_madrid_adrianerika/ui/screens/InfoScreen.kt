package com.example.p4_madrid_adrianerika.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.p4_madrid_adrianerika.R
import com.example.p4_madrid_adrianerika.models.Type
import com.example.p4_madrid_adrianerika.ui.AppStrings
import com.example.p4_madrid_adrianerika.ui.tr
import com.example.p4_madrid_adrianerika.ui.viewmodels.ViewModel
import com.google.android.gms.location.LocationServices

// Info screen — shows detailed info about a single place
@Composable
fun InfoScreen(
    id: String,
    myViewModel: ViewModel,
    language: String = "EN"  // current app language from ViewModel
) {
    val placeF = myViewModel.getPlaceById(id) ?: myViewModel.getPlaceById("R1")
    val context = LocalContext.current
    val favoriteIds = myViewModel.favoriteIds
    val isFavorite = favoriteIds.contains(placeF?.id)

    // Distance state — shows calculating message while waiting for GPS
    var distanceText by remember { mutableStateOf(AppStrings.distanceCalculating.tr(language)) }

    // FusedLocationClient for getting device location
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Fade-in animation on screen load
    var screenLoaded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { screenLoaded = true }

    val screenAlpha by animateFloatAsState(
        targetValue = if (screenLoaded) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "screenFade"
    )

    val offsetY by animateDpAsState(
        targetValue = if (screenLoaded) 0.dp else 30.dp,
        animationSpec = tween(durationMillis = 500, easing = EaseOutCubic),
        label = "screenSlide"
    )

    // Helper to calculate distance between user and place
    fun calculateDistance(location: Location, place: com.example.p4_madrid_adrianerika.models.Place?): String {
        return if (place != null && (place.lat != 0.0 || place.lng != 0.0)) {
            val results = FloatArray(1)
            Location.distanceBetween(location.latitude, location.longitude, place.lat, place.lng, results)
            AppStrings.distanceFrom.tr(language).format(results[0] / 1000)
        } else AppStrings.distanceNotAvailable.tr(language)
    }

    // Permission launcher for location access
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            fusedLocationClient.getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null
            ).addOnSuccessListener { location: Location? ->
                distanceText = if (location != null) calculateDistance(location, placeF)
                else AppStrings.distanceNotAvailable.tr(language)
            }
        } else {
            distanceText = AppStrings.distanceDenied.tr(language)
        }
    }

    // Request location on screen load
    LaunchedEffect(id) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null
            ).addOnSuccessListener { location: Location? ->
                distanceText = if (location != null) calculateDistance(location, placeF)
                else AppStrings.distanceNotAvailable.tr(language)
            }
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Decorative top gradient — consistent with rest of app
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
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
                .padding(20.dp)
                .offset(y = offsetY)
                .alpha(screenAlpha),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // TOP: Favorite toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    AppStrings.favorite.tr(language),
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "Favorite Toggle",
                    tint = if (isFavorite) Color(0xFFFFD700) else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    modifier = Modifier
                        .size(32.dp)
                        .padding(start = 4.dp)
                        .clickable { myViewModel.toggleFavorite(id) }
                )
            }

            Spacer(modifier = Modifier.weight(0.3f))

            // PLACE NAME
            Text(
                text = placeF?.name ?: "Fratelli Figurato",
                fontSize = 32.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 10.dp)
            )

            // Decorative bar under title — same style as HomeScreen
            Box(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 6.dp, bottom = 10.dp)
                    .width(60.dp)
                    .height(3.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(2.dp)
                    )
            )

            // IMAGE with rounded corners
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            ) {
                if (placeF?.imageUrl != null) {
                    AsyncImage(
                        model = placeF.imageUrl,
                        contentDescription = "Place Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Fallback to local drawable based on place type
                    val fallback = when (placeF?.type) {
                        Type.RESTAURANT -> R.drawable.r
                        Type.CINEMA -> R.drawable.c
                        Type.PARK -> R.drawable.p
                        else -> R.drawable.r
                    }
                    Image(
                        painter = painterResource(fallback),
                        contentDescription = "Place Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ADDRESS
            Text(
                text = placeF?.address ?: "Calle Alonso Cano, 37",
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Start)
            )

            // DISTANCE with pill background
            Box(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 6.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = distanceText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // FOOTER: Maps + Share buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // GOOGLE MAPS button
                Button(
                    onClick = {
                        val url = if (placeF != null && (placeF.lat != 0.0 || placeF.lng != 0.0)) {
                            "https://maps.google.com/?q=${placeF.lat},${placeF.lng}"
                        } else {
                            placeF?.gMapsUrl ?: "https://maps.google.com"
                        }
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        Icons.Default.Place,
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 4.dp)
                    )
                    Text(
                        AppStrings.mapsButton.tr(language),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                // SHARE button
                OutlinedButton(
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            // Share text includes place name in current language
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "${AppStrings.shareText.tr(language)}${placeF?.name ?: "Fratelli Figurato"}"
                            )
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, null))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        2.dp,
                        MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 4.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        AppStrings.shareButton.tr(language),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}