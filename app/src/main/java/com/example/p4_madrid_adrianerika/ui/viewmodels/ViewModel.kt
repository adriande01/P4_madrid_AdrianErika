package com.example.p4_madrid_adrianerika.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.p4_madrid_adrianerika.data.DataSource
import com.example.p4_madrid_adrianerika.models.Place
import com.example.p4_madrid_adrianerika.models.SubType
import com.example.p4_madrid_adrianerika.models.Type


class ViewModel : ViewModel() {

    // Auxiliar var to make UI redraw
    var favoriteUpdateTick by mutableStateOf(0)
        private set

    // Theme state
    var isDarkMode by mutableStateOf(false)
        private set

    // Menus state to have the state when mobile turns
    var settingsMenuExpanded by mutableStateOf(false)
    var hamburgerMenuExpanded by mutableStateOf(false)

    // DataSource
    private val dataSource: DataSource = DataSource()

    // Mutable list to save all fav ids better than change actual object (redraw)
    var favoriteIds by mutableStateOf(setOf<Int>())
        private set

    fun toggleFavorite(id: String) {
        val idInt = id.toIntOrNull() ?: return

        // Update Set (call composables so it changes)
        favoriteIds = if (favoriteIds.contains(idInt)) {
            favoriteIds - idInt
        } else {
            favoriteIds + idInt
        }

        // Toggle in Datasource not neccesary but I want to make sure
        dataSource.toggleFavorite(idInt)
    }

    fun toggleDarkMode() {
        isDarkMode = !isDarkMode
    }



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
