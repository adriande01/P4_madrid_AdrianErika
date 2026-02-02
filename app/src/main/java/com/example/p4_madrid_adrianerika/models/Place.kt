package com.example.p4_madrid_adrianerika.models

import java.net.URL

data class Place(
    val id: Int,
    val type: Type,
    val subType: SubType,
    val name: Int,
    val image: Int,
    val address: Int,
    val gMapsUrl: Int,
    var favorite: Boolean = false
)
