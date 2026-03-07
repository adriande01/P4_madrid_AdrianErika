package com.example.p4_madrid_adrianerika.data

import com.example.p4_madrid_adrianerika.R
import com.example.p4_madrid_adrianerika.models.Place
import com.example.p4_madrid_adrianerika.models.SubType
import com.example.p4_madrid_adrianerika.models.Type

class DataSource {
    // List of all places in the app with literal strings instead of R.string references
    private val allPlaces = mutableListOf(

        // RESTAURANTS
        Place("R1", Type.RESTAURANT, SubType.R_PIZZERIA, "Fratelli Figurato",
            R.drawable.r1, "Calle Alonso Cano, 37",
            "https://maps.app.goo.gl/HQ573Zm7cCzRaotE7"),
        Place("R2", Type.RESTAURANT, SubType.R_PIZZERIA, "Grosso Napoletano",
            R.drawable.r2, "Calle de Sta Engracia, 48",
            "https://maps.app.goo.gl/CPjhux7McNsJ5Cr66"),
        Place("R3", Type.RESTAURANT, SubType.R_HAMBURGER, "Goiko Grill",
            R.drawable.r3, "Calle de Prado, 15",
            "https://maps.app.goo.gl/55xNwPPunmLwwCEKA"),
        Place("R4", Type.RESTAURANT, SubType.R_HAMBURGER, "Burnout",
            R.drawable.r4, "Calle de Fuencarral, 148",
            "https://maps.app.goo.gl/eTJEjBN9L2zvRDZ98"),
        Place("R5", Type.RESTAURANT, SubType.R_BUFFET, "Running Sushi in Osaka",
            R.drawable.r5, "Calle de Hermosilla, 103",
            "https://maps.app.goo.gl/mnS5z27b1G1Wu5YZ7"),
        Place("R6", Type.RESTAURANT, SubType.R_BUFFET, "La Cabaña Argentina",
            R.drawable.r6, "C. de Ventura de la Vega, 9, y 10",
            "https://maps.app.goo.gl/2t5C3AQkmAzEaegs9"),

        // CINEMAS
        Place("C1", Type.CINEMA, SubType.C_MANY_S, "Cine Yelmo Ideal",
            R.drawable.c1, "Calle del Dr Cortezo, 6",
            "https://maps.app.goo.gl/9oZeRJXS86aKwKKH8"),
        Place("C2", Type.CINEMA, SubType.C_MANY_S, "Cinesa Proyecciones",
            R.drawable.c2, "Calle de Fuencarral, 136",
            "https://maps.app.goo.gl/bgKaYLqmPcDD3vrA7"),
        Place("C3", Type.CINEMA, SubType.C_FEW_S, "Cine Doré",
            R.drawable.c3, "Calle de Santa Isabel, 3",
            "https://maps.app.goo.gl/zv9A3i5FESDDN9YQA"),
        Place("C4", Type.CINEMA, SubType.C_FEW_S, "Sala Equis",
            R.drawable.c4, "Calle del Duque de Alba, 4",
            "https://maps.app.goo.gl/kgQVG8ebJYKt7nqe9"),

        // PARKS
        Place("P1", Type.PARK, SubType.P_BIG, "El Retiro",
            R.drawable.p1, "Retiro, 28009 Madrid",
            "https://maps.app.goo.gl/bJU8dMtTaQJzcQFP6"),
        Place("P2", Type.PARK, SubType.P_BIG, "Casa de Campo",
            R.drawable.p2, "Paseo de la Puerta del Ángel, 1",
            "https://maps.app.goo.gl/EVJfBtq6f2aNfiwu8"),
        Place("P3", Type.PARK, SubType.P_LIT, "Real Jardín Botánico",
            R.drawable.p3, "Plaza de Murillo, 2",
            "https://maps.app.goo.gl/cEhKmTHTvthT2MLq8"),
        Place("P4", Type.PARK, SubType.P_LIT, "Templo de Debod",
            R.drawable.p4, "Calle de Ferraz, 1",
            "https://maps.app.goo.gl/GQK9Fj3Mfy9YGM546")
    )

    // Toggle favorite state of a place by its String id
    fun toggleFavorite(placeId: String) {
        val index = allPlaces.indexOfFirst { it.id == placeId }
        if (index != -1) {
            allPlaces[index].favorite = !allPlaces[index].favorite
        }
    }

    // Get places filtered by type, subtype and/or favorite status
    fun getPlaces(type: Type? = null, subType: SubType? = null, favorite: Boolean? = null): List<Place> {
        return allPlaces.filter { place ->
            (type == null || place.type == type) &&
                    (subType == null || place.subType == subType) &&
                    (favorite == null || place.favorite == favorite)
        }
    }

    // Get a single place by its String id
    fun getPlaceById(id: String): Place? {
        return allPlaces.firstOrNull { it.id == id }
    }
}