package com.example.p4_madrid_adrianerika.ui.screens

import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.p4_madrid_adrianerika.R
import com.example.p4_madrid_adrianerika.models.Type


// Function in home screen that shows every type card and manages clic events
@Composable
fun HomeScreen(
    onCategoryClick: (Type) -> Unit
) {
    // Iterate over enum Type and create a Card with name of each Type inside
    Type.entries.forEach { type ->
        Card(onClick = { onCategoryClick(type) }) {
            Text(text = type.name)
        }
    }
}
