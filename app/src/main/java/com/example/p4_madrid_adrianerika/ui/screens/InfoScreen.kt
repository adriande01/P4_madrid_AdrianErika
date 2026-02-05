package com.example.p4_madrid_adrianerika.ui.screens

import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.p4_madrid_adrianerika.R
import com.example.p4_madrid_adrianerika.models.Place
import com.example.p4_madrid_adrianerika.models.Type
import com.example.p4_madrid_adrianerika.ui.viewmodels.ViewModel

// Function in home screen that shows every type card and manages clic events
@Composable
fun InfoScreen(
    id: String
) {
    // 1. Get ViewModel
    val myViewModel: ViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    // 2. Get the place by id to show his info, default: R1
    val placeF =
        myViewModel.getPlaceById(id) ?: myViewModel.getPlaceById(stringResource(R.string.R1_ID))


    Card() {
        Text(text = stringResource(placeF?.address ?: R.string.R1_ADDRESS))
    }

}
