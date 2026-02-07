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

    var currentTheme by mutableStateOf("RED")
        private set



    // Menus state to have the state when mobile turns
    var settingsMenuExpanded by mutableStateOf(false)
    var hamburgerMenuExpanded by mutableStateOf(false)

    // DataSource
    private val dataSource: DataSource = DataSource()

    // Check menu expanded
    var filterMenuExpanded by mutableStateOf(false)

    // Control only favorite checkbox places
    var showOnlyFavorites by mutableStateOf(false)

    // Save selected only selected sybtypes in filter (checkboxes)
    var selectedSubTypes by mutableStateOf(setOf<SubType>())

    // Mutable list to save all fav ids better than change actual object (redraw)
    var favoriteIds by mutableStateOf(setOf<Int>())
        private set

    fun toggleSubTypeFilter(subType: SubType) {
        selectedSubTypes = if (selectedSubTypes.contains(subType)) {
            selectedSubTypes - subType
        } else {
            selectedSubTypes + subType
        }
    }

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

    fun setTheme(theme: String) {
        currentTheme = theme.uppercase()
    }

    fun getAllPlacesFiltered(type: String): List<Place> {
        return try {
            val enum = Type.valueOf(type)
            dataSource.getPlaces(enum)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Filter places depending on fav or subtype or both
    fun getFilteredPlacesGrouped(typeName: String): Map<SubType, List<Place>> {
        return try {
            val typeEnum = Type.valueOf(typeName)
            val allPlaces = dataSource.getPlaces(typeEnum)

            allPlaces.filter { place ->
                // If switch fav is on ==> only fav
                val matchesFav = if (showOnlyFavorites) favoriteIds.contains(place.id) else true
                // If there is not selected subtype includes all, if not ==> includes only selected subtypes
                val matchesSub = if (selectedSubTypes.isNotEmpty()) selectedSubTypes.contains(place.subType) else true

                matchesFav && matchesSub
            }.groupBy { it.subType }
        } catch (e: Exception) {
            emptyMap()
        }
    }

    fun getPlaceById(id: String): Place? {
        val idInt = id.toIntOrNull() ?: return null
        return dataSource.getPlaceById(idInt)
    }
}
