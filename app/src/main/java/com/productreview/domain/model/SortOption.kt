package com.productreview.domain.model

/**
 * Sort options for product list
 */
data class SortOption(
    val label: String,
    val value: String
)

object SortOptions {
    val NAME_ASC = SortOption("Name (A-Z)", "name,asc")
    val NAME_DESC = SortOption("Name (Z-A)", "name,desc")
    val RATING_DESC = SortOption("Top Rated", "averageRating,desc")
    val RATING_ASC = SortOption("Low Rated", "averageRating,asc")
    val PRICE_ASC = SortOption("Price: Low-High", "price,asc")
    val PRICE_DESC = SortOption("Price: High-Low", "price,desc")
    val REVIEWS_DESC = SortOption("Most Reviewed", "reviewCount,desc")

    val options = listOf(
        NAME_ASC,
        NAME_DESC,
        RATING_DESC,
        RATING_ASC,
        PRICE_ASC,
        PRICE_DESC,
        REVIEWS_DESC
    )
}
