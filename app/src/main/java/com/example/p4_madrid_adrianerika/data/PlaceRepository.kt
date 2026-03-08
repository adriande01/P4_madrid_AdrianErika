package com.example.p4_madrid_adrianerika.data

import com.example.p4_madrid_adrianerika.db.AppDao
import com.example.p4_madrid_adrianerika.db.PlaceEntity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlaceRepository(private val dao: AppDao) {

    private val UNSPLASH_KEY = "46uqNHiaHSUIpOzsUbJjwdhS3ier6nimyRc6L3UuEzc" // ACCESS KEY

    private val service: OverpassService = Retrofit.Builder()
        .baseUrl("https://overpass-api.de/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OverpassService::class.java)

    private fun buildQuery(type: String): String {
        val tag = when (type) {
            "RESTAURANT" -> """node["amenity"="restaurant"](40.38,-3.72,40.47,-3.67);out 30;"""
            "CINEMA"     -> """node["amenity"="cinema"](40.38,-3.72,40.47,-3.67);out 30;"""
            "PARK"       -> """node["leisure"="park"](40.38,-3.72,40.47,-3.67);out 30;"""
            else -> ""
        }
        return "[out:json];$tag"
    }

    private fun mapSubType(type: String, el: OverpassElement): String {
        return when (type) {
            "RESTAURANT" -> {
                val cuisine = el.tags?.get("cuisine")?.lowercase() ?: ""
                when {
                    cuisine.contains("pizza") -> "R_PIZZERIA"
                    cuisine.contains("burger") || cuisine.contains("american") -> "R_HAMBURGER"
                    cuisine.contains("buffet") || cuisine.contains("asian") || cuisine.contains("chinese") -> "R_BUFFET"
                    else -> when (el.id % 3) {
                        0L -> "R_PIZZERIA"
                        1L -> "R_HAMBURGER"
                        else -> "R_BUFFET"
                    }
                }
            }
            "CINEMA" -> if (el.id % 2 == 0L) "C_MANY_S" else "C_FEW_S"
            "PARK" -> {
                val leisure = el.tags?.get("leisure")?.lowercase() ?: ""
                when {
                    leisure.contains("garden") -> "P_LIT"
                    else -> if (el.id % 2 == 0L) "P_BIG" else "P_LIT"
                }
            }
            else -> "R_PIZZERIA"
        }
    }

    // Gets a relevant image from Unsplash based on place type
    private fun getImageUrl(type: String): String {
        val query = when (type) {
            "RESTAURANT" -> "restaurant+madrid"
            "CINEMA"     -> "cinema+theater"
            "PARK"       -> "park+madrid"
            else         -> "madrid"
        }
        return "https://api.unsplash.com/photos/random?query=$query&client_id=$UNSPLASH_KEY"
    }

    suspend fun loadPlaces(type: String): List<PlaceEntity> {
        if (dao.countPlaces(type) > 0) return dao.getPlacesByType(type)

        return try {
            val response = service.getPlaces(buildQuery(type))
            val entities = response.elements.mapNotNull { el ->
                val name = el.tags?.get("name") ?: return@mapNotNull null
                val address = listOfNotNull(
                    el.tags["addr:street"],
                    el.tags["addr:housenumber"]
                ).joinToString(" ").ifEmpty { "Madrid" }
                val lat = el.lat ?: 0.0
                val lng = el.lon ?: 0.0
                PlaceEntity(
                    id = el.id.toString(),
                    name = name,
                    address = address,
                    gMapsUrl = "https://maps.google.com/?q=$lat,$lng",
                    imageUrl = when (type) {
                        "RESTAURANT" -> "https://loremflickr.com/400/300/restaurant,food?lock=${el.id}"
                        "CINEMA"     -> "https://loremflickr.com/400/300/film,theater?lock=${el.id}"
                        "PARK"       -> "https://loremflickr.com/400/300/garden,trees?lock=${el.id}"
                        else         -> "https://loremflickr.com/400/300/city,madrid?lock=${el.id}"
                    },
                    image = null,
                    lat = lat,
                    lng = lng,
                    type = type,
                    subType = mapSubType(type, el)
                )
            }
            dao.insertPlaces(entities)
            entities
        } catch (e: Exception) {
            emptyList()
        }
    }
}