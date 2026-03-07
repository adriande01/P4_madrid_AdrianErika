package com.example.p4_madrid_adrianerika.data

import retrofit2.http.GET
import retrofit2.http.Query

// Response data classes
data class OverpassResponse(val elements: List<OverpassElement>)
data class OverpassElement(
    val id: Long,
    val lat: Double?,
    val lon: Double?,
    val tags: Map<String, String>?
)

interface OverpassService {
    @GET("api/interpreter")
    suspend fun getPlaces(@Query("data") query: String): OverpassResponse
}