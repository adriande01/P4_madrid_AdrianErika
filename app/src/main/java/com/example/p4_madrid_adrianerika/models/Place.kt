package com.example.p4_madrid_adrianerika.models

data class Place(
    val id: String,
    val type: Type,
    val subType: SubType,
    val name: String,
    val image: Int? = null,
    val address: String,
    val gMapsUrl: String,
    val imageUrl: String? = null,
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    var favorite: Boolean = false
)
