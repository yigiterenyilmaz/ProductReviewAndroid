package com.productreview.data.model

import com.google.gson.annotations.SerializedName

/**
 * Paginated API response wrapper
 */
data class Page<T>(
    @SerializedName("content")
    val content: List<T>,
    @SerializedName("totalElements")
    val totalElements: Int,
    @SerializedName("totalPages")
    val totalPages: Int,
    @SerializedName("number")
    val number: Int, // Current page index (0-based)
    @SerializedName("size")
    val size: Int,
    @SerializedName("last")
    val last: Boolean
)

/**
 * Product DTO from API
 */
data class ApiProduct(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("price")
    val price: Double,
    @SerializedName("averageRating")
    val averageRating: Double? = null,
    @SerializedName("reviewCount")
    val reviewCount: Int? = null,
    @SerializedName("ratingBreakdown")
    val ratingBreakdown: Map<Int, Int>? = null,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    @SerializedName("aiSummary")
    val aiSummary: String? = null
)

/**
 * Review DTO from API
 */
data class ApiReview(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("reviewerName")
    val reviewerName: String? = null,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("comment")
    val comment: String,
    @SerializedName("helpfulCount")
    val helpfulCount: Int? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null
)

/**
 * Request body for creating a new review
 */
data class CreateReviewRequest(
    @SerializedName("reviewerName")
    val reviewerName: String,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("comment")
    val comment: String
)
