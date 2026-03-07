package com.example.p4_madrid_adrianerika.db

import androidx.room.*


// ENTITIES — tables in the Room database

// Table for registered users
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val username: String,
    val passwordHash: String
)

// Table for places mirror of the Place model
@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val address: String,
    val gMapsUrl: String,
    val imageUrl: String?,
    val image: Int?,
    val lat: Double,
    val lng: Double,
    val type: String,
    val subType: String
)

// Table for favorites — links a user to a place
@Entity(
    tableName = "favorites",
    primaryKeys = ["userId", "placeId"]
)
data class FavoriteEntity(
    val userId: String,
    val placeId: String
)

// DAO — database operations
@Dao
interface AppDao {

    // --- USERS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUser(username: String): UserEntity?

    // --- PLACES ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaces(places: List<PlaceEntity>)

    @Query("SELECT * FROM places WHERE type = :type")
    suspend fun getPlacesByType(type: String): List<PlaceEntity>

    @Query("SELECT COUNT(*) FROM places WHERE type = :type")
    suspend fun countPlaces(type: String): Int

    // --- FAVORITES ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFav(fav: FavoriteEntity)

    @Delete
    suspend fun deleteFav(fav: FavoriteEntity)

    @Query("SELECT placeId FROM favorites WHERE userId = :userId")
    suspend fun getFavsByUser(userId: String): List<String>
}


// DATABASE — main Room database class
@Database(
    entities = [UserEntity::class, PlaceEntity::class, FavoriteEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "madrid_db"
                )
                    .fallbackToDestructiveMigration() // wipes DB on version change
                    .build().also { INSTANCE = it }
            }
        }
    }
}