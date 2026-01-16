package com.productreview.data.api

import com.productreview.data.model.ApiProduct
import com.productreview.data.model.ApiReview
import com.productreview.data.model.CreateReviewRequest
import com.productreview.data.model.Page
import retrofit2.http.*

/**
 * Retrofit API interface for Product Review backend
 */
interface ProductApiService {

    /**
     * Get paginated list of products
     * @param page Page number (0-based)
     * @param size Items per page
     * @param sort Sort criteria (e.g., "name,asc")
     * @param category Filter by category (optional)
     */
    @GET("api/products")
    suspend fun getProducts(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sort") sort: String = "name,asc",
        @Query("category") category: String? = null
    ): Page<ApiProduct>

    /**
     * Get single product details by ID
     */
    @GET("api/products/{id}")
    suspend fun getProduct(
        @Path("id") id: Long
    ): ApiProduct

    /**
     * Get paginated reviews for a product
     * @param productId Product ID
     * @param page Page number (0-based)
     * @param size Items per page
     * @param sort Sort criteria (e.g., "createdAt,desc")
     * @param rating Filter by rating (optional)
     */
    @GET("api/products/{productId}/reviews")
    suspend fun getReviews(
        @Path("productId") productId: Long,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sort") sort: String = "createdAt,desc",
        @Query("rating") rating: Int? = null
    ): Page<ApiReview>

    /**
     * Submit a new review for a product
     */
    @POST("api/products/{productId}/reviews")
    suspend fun postReview(
        @Path("productId") productId: Long,
        @Body review: CreateReviewRequest
    ): ApiReview

    /**
     * Mark a review as helpful
     */
    @PUT("api/products/reviews/{reviewId}/helpful")
    suspend fun markReviewAsHelpful(
        @Path("reviewId") reviewId: Long
    ): ApiReview
}
