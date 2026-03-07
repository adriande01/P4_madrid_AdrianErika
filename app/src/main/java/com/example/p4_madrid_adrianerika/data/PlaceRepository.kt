package com.example.p4_madrid_adrianerika.data

import com.example.p4_madrid_adrianerika.db.AppDao
import com.example.p4_madrid_adrianerika.db.PlaceEntity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlaceRepository(private val dao: AppDao) {

    private val service: OverpassService = Retrofit.Builder()
        .baseUrl("https://overpass-api.de/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OverpassService::class.java)

    // Overpass query per type
    private fun buildQuery(type: String): String {
        val tag = when (type) {
            "RESTAURANT" -> """node["amenity"="restaurant"](40.38,-3.72,40.47,-3.67);out 20;"""
            "CINEMA"     -> """node["amenity"="cinema"](40.38,-3.72,40.47,-3.67);out 20;"""
            "PARK"       -> """node["leisure"="park"](40.38,-3.72,40.47,-3.67);out 20;"""
            else -> ""
        }
        return "[out:json];$tag"
    }

    // Default subtype per type (Overpass doesn't know our subtypes)
    private fun defaultSubType(type: String): String = when (type) {
        "RESTAURANT" -> "R_PIZZERIA"
        "CINEMA"     -> "C_MANY_S"
        "PARK"       -> "P_BIG"
        else         -> "R_PIZZERIA"
    }

    suspend fun loadPlaces(type: String): List<PlaceEntity> {
        // If Room already has data → return it directly
        if (dao.countPlaces(type) > 0) {
            return dao.getPlacesByType(type)
        }

        // Otherwise → fetch from API → save → return
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
                    imageUrl = null,
                    image = null,
                    lat = lat,
                    lng = lng,
                    type = type,
                    subType = defaultSubType(type)
                )
            }
            dao.insertPlaces(entities)
            entities
        } catch (e: Exception) {
            emptyList()
        }
    }
}