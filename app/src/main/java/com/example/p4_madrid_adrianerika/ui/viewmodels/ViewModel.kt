package com.example.p4_madrid_adrianerika.ui.viewmodels

import com.example.p4_madrid_adrianerika.data.DataSource
import com.example.p4_madrid_adrianerika.models.Place
import com.example.p4_madrid_adrianerika.models.Type

class ViewModel {
    val dataSource: DataSource = DataSource()
    fun getAllPlacesFiltered(type: String): List<Place> {
        val newEnum = Type.valueOf(type)
        return dataSource.getPlaces(newEnum)
    }
}
