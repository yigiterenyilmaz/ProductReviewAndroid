package com.productreview.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room database for local data storage
 */
@Database(
    entities = [WishlistEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wishlistDao(): WishlistDao
}
