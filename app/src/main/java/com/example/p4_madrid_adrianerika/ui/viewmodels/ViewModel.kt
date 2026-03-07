package com.example.p4_madrid_adrianerika.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.p4_madrid_adrianerika.data.PlaceRepository
import com.example.p4_madrid_adrianerika.db.AppDatabase
import com.example.p4_madrid_adrianerika.db.FavoriteEntity
import com.example.p4_madrid_adrianerika.models.Place
import com.example.p4_madrid_adrianerika.models.SubType
import com.example.p4_madrid_adrianerika.models.Type
import kotlinx.coroutines.launch

class ViewModel(application: Application) : AndroidViewModel(application) {

    // --- DATABASE + REPOSITORY ---
    private val dao = AppDatabase.getInstance(application).appDao()
    private val repository = PlaceRepository(dao)

    // --- CURRENT USER ---
    var currentUserId by mutableStateOf<String?>(null)

    // --- PLACES loaded from Room/API ---
    var places by mutableStateOf<List<Place>>(emptyList())
        private set

    // --- LOADING state ---
    var isLoading by mutableStateOf(false)
        private set

    // --- THEME ---
    var isDarkMode by mutableStateOf(false)
        private set
    var currentTheme by mutableStateOf("RED")
        private set

    // --- MENU STATES ---
    var settingsMenuExpanded by mutableStateOf(false)
    var hamburgerMenuExpanded by mutableStateOf(false)
    var filterMenuExpanded by mutableStateOf(false)

    // --- FILTERS ---
    var showOnlyFavorites by mutableStateOf(false)
    var selectedSubTypes by mutableStateOf(setOf<SubType>())

    // --- FAVORITES ---
    var favoriteIds by mutableStateOf(setOf<String>())
        private set

    // Load places from Repository (API first time, Room after)
    fun loadPlaces(type: String) {
        viewModelScope.launch {
            isLoading = true
            val entities = repository.loadPlaces(type)
            places = entities.map { entity ->
                Place(
                    id = entity.id,
                    name = entity.name,
                    address = entity.address,
                    gMapsUrl = entity.gMapsUrl,
                    imageUrl = entity.imageUrl,
                    image = entity.image,
                    lat = entity.lat,
                    lng = entity.lng,
                    type = Type.valueOf(entity.type),
                    subType = SubType.valueOf(entity.subType)
                )
            }
            isLoading = false
        }
    }

    // Load favorites for the logged in user from Room
    fun loadFavorites(userId: String) {
        currentUserId = userId
        viewModelScope.launch {
            val favs = dao.getFavsByUser(userId)
            favoriteIds = favs.toSet()
        }
    }

    // Toggle favorite — saves to Room + updates UI
    fun toggleFavorite(id: String) {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            if (favoriteIds.contains(id)) {
                dao.deleteFav(FavoriteEntity(userId, id))
                favoriteIds = favoriteIds - id
            } else {
                dao.insertFav(FavoriteEntity(userId, id))
                favoriteIds = favoriteIds + id
            }
        }
    }

    // Get filtered + grouped places for ListScreen
    fun getFilteredPlacesGrouped(typeName: String): Map<SubType, List<Place>> {
        return places
            .filter { place ->
                val matchesFav = if (showOnlyFavorites) favoriteIds.contains(place.id) else true
                val matchesSub = if (selectedSubTypes.isNotEmpty()) selectedSubTypes.contains(place.subType) else true
                matchesFav && matchesSub
            }
            .groupBy { it.subType }
    }

    // Get one place by id
    fun getPlaceById(id: String): Place? {
        return places.find { it.id == id }
    }

    fun toggleSubTypeFilter(subType: SubType) {
        selectedSubTypes = if (selectedSubTypes.contains(subType)) {
            selectedSubTypes - subType
        } else {
            selectedSubTypes + subType
        }
    }

    fun toggleDarkMode() {
        isDarkMode = !isDarkMode
    }

    fun setTheme(theme: String) {
        currentTheme = theme.uppercase()
    }
}