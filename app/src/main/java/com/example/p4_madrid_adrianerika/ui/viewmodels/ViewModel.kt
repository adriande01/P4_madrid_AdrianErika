package com.example.p4_madrid_adrianerika.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.p4_madrid_adrianerika.data.DataSource
import com.example.p4_madrid_adrianerika.models.Place
import com.example.p4_madrid_adrianerika.models.SubType
import com.example.p4_madrid_adrianerika.models.Type


class ViewModel : ViewModel() {
    private val dataSource: DataSource = DataSource()
    fun getAllPlacesFiltered(type: String): List<Place> {
        return try {
            val enum = Type.valueOf(type)
            dataSource.getPlaces(enum)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getPlacesGroupedBySubtype(typeName: String): Map<SubType, List<Place>> {
        return try {
            val typeEnum = Type.valueOf(typeName)
            val allPlacesOfType = dataSource.getPlaces(typeEnum)
            // Group list by subtype
            allPlacesOfType.groupBy { it.subType }
        } catch (e: Exception) {
            emptyMap()
        }
    }

    fun getPlaceById(id: String): Place? {
        val idInt = id.toIntOrNull() ?: return null
        return dataSource.getPlaceById(idInt)
    }
}
