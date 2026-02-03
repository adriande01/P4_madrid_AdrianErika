package com.example.p4_madrid_adrianerika.data

import com.example.p4_madrid_adrianerika.R

import com.example.p4_madrid_adrianerika.models.Place
import com.example.p4_madrid_adrianerika.models.SubType
import com.example.p4_madrid_adrianerika.models.Type

class DataSource {
    // List of all sites in our app, can be mod
    private val allPlaces = mutableListOf(
        Place(
            R.string.R1_ID,
            Type.RESTAURANT,
            SubType.R_PIZZERIA,
            R.string.R1_NAME,
            R.drawable.r1,
            R.string.R1_ADDRESS,
            R.string.R1_URL,
            false
        ),
        Place(
            R.string.C1_ID,
            Type.CINEMA,
            SubType.C_MANY_S,
            R.string.C1_NAME,
            R.drawable.c1,
            R.string.C1_ADDRESS,
            R.string.C1_URL,
            false
        ),
        Place(
            R.string.P1_ID,
            Type.PARK,
            SubType.P_BIG,
            R.string.P1_NAME,
            R.drawable.p1,
            R.string.P1_ADDRESS,
            R.string.P1_URL,
            false
        )
    )

    // Func to toggle the fav value
    fun toggleFavorite(placeId: Int) {
        val index = allPlaces.indexOfFirst { it.id == placeId }
        if (index != -1) {
            // Invertimos el valor actual
            allPlaces[index].favorite = !allPlaces[index].favorite
        }
    }


    fun getPlaces(type: Type? = null, subType: SubType? = null, favorite: Boolean? = null): List<Place> {
        return allPlaces.filter { place ->
            (type == null || place.type == type) &&
                    (subType == null || place.subType == subType) &&
                    (favorite == null || place.favorite == favorite)
        }
    }
}