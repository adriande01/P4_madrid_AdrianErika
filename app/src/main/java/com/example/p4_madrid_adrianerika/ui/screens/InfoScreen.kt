package com.example.p4_madrid_adrianerika.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.p4_madrid_adrianerika.R
import com.example.p4_madrid_adrianerika.ui.viewmodels.ViewModel
import com.google.android.gms.location.LocationServices

@Composable
fun InfoScreen(
    id: String,
    myViewModel: ViewModel,
) {
    val placeF = myViewModel.getPlaceById(id) ?: myViewModel.getPlaceById("R1")
    val context = LocalContext.current
    val favoriteIds = myViewModel.favoriteIds
    val isFavorite = favoriteIds.contains(placeF?.id)

    // Distance state
    var distanceText by remember { mutableStateOf("Calculando distancia...") }

    // FusedLocationClient
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Helper to calculate and set distance
    fun calculateDistance(location: Location, placeF: com.example.p4_madrid_adrianerika.models.Place?): String {
        return if (placeF != null && (placeF.lat != 0.0 || placeF.lng != 0.0)) {
            val results = FloatArray(1)
            Location.distanceBetween(location.latitude, location.longitude, placeF.lat, placeF.lng, results)
            "%.1f km de ti".format(results[0] / 1000)
        } else "Distancia no disponible"
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            fusedLocationClient.getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null
            ).addOnSuccessListener { location: Location? ->
                distanceText = if (location != null) calculateDistance(location, placeF)
                else "Distancia no disponible"
            }
        } else {
            distanceText = "Permiso denegado"
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
                else "Distancia no disponible"
            }
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TOP: Favorite
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Favorite", fontSize = 16.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "Favorite Toggle",
                tint = if (isFavorite) Color(0xFFFFD700) else MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(32.dp).padding(start = 4.dp).clickable {
                    myViewModel.toggleFavorite(id)
                }
            )
        }

        Spacer(modifier = Modifier.weight(0.5f))

        // NAME
        Text(
            text = placeF?.name ?: "Fratelli Figurato",
            fontSize = 34.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 10.dp)
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 2.5.dp, color = MaterialTheme.colorScheme.onPrimary)

        // IMAGE
        Card(
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.onPrimary),
            shape = RectangleShape,
            modifier = Modifier.fillMaxWidth().height(350.dp)
        ) {
            if (placeF?.image != null) {
                Image(
                    painter = painterResource(placeF.image),
                    contentDescription = "Place Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Image by type
                val fallback = when (placeF?.type) {
                    com.example.p4_madrid_adrianerika.models.Type.RESTAURANT -> R.drawable.r
                    com.example.p4_madrid_adrianerika.models.Type.CINEMA -> R.drawable.c
                    com.example.p4_madrid_adrianerika.models.Type.PARK -> R.drawable.p
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

        // ADDRESS
        Text(
            text = placeF?.address ?: "Calle Alonso Cano, 37",
            fontSize = 18.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.Start).padding(top = 15.dp)
        )

        // DISTANCE
        Text(
            text = distanceText,
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.Start).padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // FOOTER: Maps + Share
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    val url = if (placeF != null && (placeF.lat != 0.0 || placeF.lng != 0.0)) {
                        "https://maps.google.com/?q=${placeF.lat},${placeF.lng}"
                    } else {
                        placeF?.gMapsUrl ?: "https://maps.google.com"
                    }
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            ) {
                Text("GOOGLE MAPS", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Icon(Icons.Default.Place, contentDescription = "Map Icon", modifier = Modifier.padding(start = 5.dp).size(24.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Check out this place: ${placeF?.name ?: "Fratelli Figurato"}")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, null))
                }
            ) {
                Text("share", fontStyle = FontStyle.Italic, fontSize = 16.sp)
                Icon(Icons.Default.Share, contentDescription = "Share Icon", modifier = Modifier.padding(start = 5.dp).size(24.dp))
            }
        }
    }
}