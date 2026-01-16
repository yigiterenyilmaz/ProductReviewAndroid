package com.productreview.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for wishlist items
 */
@Entity(tableName = "wishlist")
data class WishlistEntity(
    @PrimaryKey
    val productId: Long,
    val name: String,
    val price: Double,
    val category: String,
    val imageUrl: String?,
    val averageRating: Double,
    val addedAt: Long = System.currentTimeMillis()
)
