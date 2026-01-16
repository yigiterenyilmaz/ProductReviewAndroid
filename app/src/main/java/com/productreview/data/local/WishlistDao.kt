package com.productreview.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO for wishlist operations
 */
@Dao
interface WishlistDao {

    @Query("SELECT * FROM wishlist ORDER BY addedAt DESC")
    fun getAllWishlistItems(): Flow<List<WishlistEntity>>

    @Query("SELECT * FROM wishlist WHERE productId = :productId")
    suspend fun getWishlistItem(productId: Long): WishlistEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlistItem(item: WishlistEntity)

    @Delete
    suspend fun deleteWishlistItem(item: WishlistEntity)

    @Query("DELETE FROM wishlist WHERE productId = :productId")
    suspend fun deleteWishlistItemById(productId: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE productId = :productId)")
    fun isInWishlist(productId: Long): Flow<Boolean>
}
