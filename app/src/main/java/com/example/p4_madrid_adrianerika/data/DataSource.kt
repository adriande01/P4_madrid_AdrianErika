package com.example.p4_madrid_adrianerika.data

import com.example.p4_madrid_adrianerika.R

import com.example.p4_madrid_adrianerika.models.Place
import com.example.p4_madrid_adrianerika.models.SubType
import com.example.p4_madrid_adrianerika.models.Type

class DataSource {
    // List of all sites in our app, can be mod
    private val allPlaces = mutableListOf(
        // RESTAURANTS
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
            R.string.R2_ID,
            Type.RESTAURANT,
            SubType.R_PIZZERIA,
            R.string.R2_NAME,
            R.drawable.r2,
            R.string.R2_ADDRESS,
            R.string.R2_URL,
            false
        ),
        Place(
            R.string.R3_ID,
            Type.RESTAURANT,
            SubType.R_HAMBURGER,
            R.string.R3_NAME,
            R.drawable.r3,
            R.string.R3_ADDRESS,
            R.string.R3_URL,
            false
        ),
        Place(
            R.string.R4_ID,
            Type.RESTAURANT,
            SubType.R_HAMBURGER,
            R.string.R4_NAME,
            R.drawable.r4,
            R.string.R4_ADDRESS,
            R.string.R4_URL,
            false
        ),
        Place(
            R.string.R5_ID,
            Type.RESTAURANT,
            SubType.R_BUFFET,
            R.string.R5_NAME,
            R.drawable.r5,
            R.string.R5_ADDRESS,
            R.string.R5_URL,
            false
        ),
        Place(
            R.string.R6_ID,
            Type.RESTAURANT,
            SubType.R_BUFFET,
            R.string.R6_NAME,
            R.drawable.r6,
            R.string.R6_ADDRESS,
            R.string.R6_URL,
            false
        ),

        // CINEMAS
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
            R.string.C2_ID,
            Type.CINEMA,
            SubType.C_MANY_S,
            R.string.C2_NAME,
            R.drawable.c2,
            R.string.C2_ADDRESS,
            R.string.C2_URL,
            false
        ),
        Place(
            R.string.C3_ID,
            Type.CINEMA,
            SubType.C_FEW_S,
            R.string.C3_NAME,
            R.drawable.c3,
            R.string.C3_ADDRESS,
            R.string.C3_URL,
            false
        ),
        Place(
            R.string.C4_ID,
            Type.CINEMA,
            SubType.C_FEW_S,
            R.string.C4_NAME,
            R.drawable.c4,
            R.string.C4_ADDRESS,
            R.string.C4_URL,
            false
        ),

        // PARKS
        Place(
            R.string.P1_ID,
            Type.PARK,
            SubType.P_BIG,
            R.string.P1_NAME,
            R.drawable.p1,
            R.string.P1_ADDRESS,
            R.string.P1_URL,
            false
        ),
        Place(
            R.string.P2_ID,
            Type.PARK,
            SubType.P_BIG,
            R.string.P2_NAME,
            R.drawable.p2,
            R.string.P2_ADDRESS,
            R.string.P2_URL,
            false
        ),
        Place(
            R.string.P3_ID,
            Type.PARK,
            SubType.P_LIT,
            R.string.P3_NAME,
            R.drawable.p3,
            R.string.P3_ADDRESS,
            R.string.P3_URL,
            false
        ),
        Place(
            R.string.P4_ID,
            Type.PARK,
            SubType.P_LIT,
            R.string.P4_NAME,
            R.drawable.p4,
            R.string.P4_ADDRESS,
            R.string.P4_URL,
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


    // Func to get all places
    fun getPlaces(type: Type? = null, subType: SubType? = null, favorite: Boolean? = null): List<Place> {
        return allPlaces.filter { place ->
            (type == null || place.type == type) &&
                    (subType == null || place.subType == subType) &&
                    (favorite == null || place.favorite == favorite)
        }
    }

    // Func to get one Place by id
    fun getPlaceById(id: Int): Place? {
        allPlaces.forEach { place ->
            if (place.id == id){
                return place
            }
        }
        return null
    }

}