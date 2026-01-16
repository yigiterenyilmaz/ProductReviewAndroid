package com.productreview.data.repository

import com.productreview.data.local.WishlistDao
import com.productreview.data.local.WishlistEntity
import com.productreview.domain.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for wishlist operations
 */
@Singleton
class WishlistRepository @Inject constructor(
    private val wishlistDao: WishlistDao
) {
    /**
     * Get all wishlist items as Flow
     */
    fun getAllWishlistItems(): Flow<List<WishlistEntity>> {
        return wishlistDao.getAllWishlistItems()
    }

    /**
     * Check if product is in wishlist
     */
    fun isInWishlist(productId: Long): Flow<Boolean> {
        return wishlistDao.isInWishlist(productId)
    }

    /**
     * Add product to wishlist
     */
    suspend fun addToWishlist(product: Product) {
        val entity = WishlistEntity(
            productId = product.id,
            name = product.name,
            price = product.price,
            category = product.category,
            imageUrl = product.imageUrl,
            averageRating = product.averageRating
        )
        wishlistDao.insertWishlistItem(entity)
    }

    /**
     * Remove product from wishlist
     */
    suspend fun removeFromWishlist(productId: Long) {
        wishlistDao.deleteWishlistItemById(productId)
    }

    /**
     * Toggle wishlist status for a product
     */
    suspend fun toggleWishlist(product: Product, isInWishlist: Boolean) {
        if (isInWishlist) {
            removeFromWishlist(product.id)
        } else {
            addToWishlist(product)
        }
    }
}
