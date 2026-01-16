package com.productreview.domain.model

import com.productreview.data.model.ApiProduct

/**
 * Domain model for Product
 * Clean separation from API DTOs
 */
data class Product(
    val id: Long,
    val name: String,
    val description: String,
    val category: String,
    val price: Double,
    val averageRating: Double,
    val reviewCount: Int,
    val ratingBreakdown: Map<Int, Int>,
    val imageUrl: String?,
    val aiSummary: String?
) {
    companion object {
        /**
         * Map API DTO to domain model
         */
        fun fromApiModel(apiProduct: ApiProduct): Product {
            return Product(
                id = apiProduct.id,
                name = apiProduct.name,
                description = apiProduct.description,
                category = apiProduct.category ?: "Other",
                price = apiProduct.price,
                averageRating = apiProduct.averageRating ?: 0.0,
                reviewCount = apiProduct.reviewCount ?: 0,
                ratingBreakdown = apiProduct.ratingBreakdown ?: emptyMap(),
                imageUrl = apiProduct.imageUrl,
                aiSummary = apiProduct.aiSummary
            )
        }
    }

    /**
     * Formatted price string
     */
    val formattedPrice: String
        get() = "$${"%.2f".format(price)}"

    /**
     * Check if product has reviews
     */
    val hasReviews: Boolean
        get() = reviewCount > 0
}
