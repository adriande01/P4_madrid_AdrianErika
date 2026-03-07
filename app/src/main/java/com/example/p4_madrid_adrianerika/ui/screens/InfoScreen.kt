package com.example.p4_madrid_adrianerika.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.p4_madrid_adrianerika.R
import com.example.p4_madrid_adrianerika.ui.viewmodels.ViewModel

@Composable
fun InfoScreen(
    id: String,
    // Same instance for all screens
    myViewModel: ViewModel,
) {
    // 1. Get ViewModel (NOT NOW CAUSE WE USE THE SAME INSTANCE IN ALL SCREENS)
    //val myViewModel: ViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    // 2. Get the place by id to show his info, default: R1
    val placeF = myViewModel.getPlaceById(id) ?: myViewModel.getPlaceById("R1")

    // Context for Intents (Maps and Share)
    val context = LocalContext.current

    // State for Favorite (Star icon) from ViewModel
    val favoriteIds = myViewModel.favoriteIds
    val isFavorite = favoriteIds.contains(placeF?.id)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TOP SECTION: Favorite and Star Icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Favorite",
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "Favorite Toggle",
                tint = if (isFavorite) Color(0xFFFFD700) else MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(32.dp)
                    .padding(start = 4.dp)
                    .clickable {
                        // Change / toggle state calling ViewModel
                        myViewModel.toggleFavorite(id)
                    }
            )
        }
        // Pushes the footer to the very bottom
        Spacer(modifier = Modifier.weight(0.5f))

        // PLACE NAME (Main Title)
        Text(
            text = placeF?.name ?: "Fratelli Figurato",
            fontSize = 34.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 10.dp)
        )

        // BLACK DIVIDER LINE
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 2.5.dp,
            color = MaterialTheme.colorScheme.onPrimary
        )


        // CENTRAL IMAGE WITH BLACK BORDER
        Card(
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.onPrimary),
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ) {
            Image(
                painter = painterResource(placeF?.image ?: R.drawable.r1),
                contentDescription = "Place Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // ADDRESS (Located below the image on the left)
        Text(
            text = placeF?.address ?: "Calle Alonso Cano, 37",
            fontSize = 18.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 15.dp)
        )

        // Pushes the footer to the very bottom
        Spacer(modifier = Modifier.weight(1f))

        // FOOTER SECTION: Google Maps and Share
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // GOOGLE MAPS BUTTON
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    val url = placeF?.gMapsUrl ?: "https://maps.app.goo.gl/HQ573Zm7cCzRaotE7"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                }
            ) {
                Text(
                    text = "GOOGLE MAPS",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "Map Icon",
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(24.dp)
                )
            }

            // SHARE BUTTON
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Check out this place: ${placeF?.name ?: "Fratelli Figurato"}"
                        )
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }
            ) {
                Text(
                    text = "share",
                    fontStyle = FontStyle.Italic,
                    fontSize = 16.sp
                )
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share Icon",
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(24.dp)
                )
            }
        }
    }
}