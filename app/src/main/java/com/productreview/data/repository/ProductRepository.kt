package com.productreview.data.repository

import com.productreview.data.api.ProductApiService
import com.productreview.data.model.ApiProduct
import com.productreview.data.model.ApiReview
import com.productreview.data.model.CreateReviewRequest
import com.productreview.data.model.Page
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for product-related data operations
 * Follows Clean Architecture - acts as single source of truth
 */
@Singleton
class ProductRepository @Inject constructor(
    private val apiService: ProductApiService
) {
    /**
     * Fetch paginated products with optional filtering and sorting
     */
    suspend fun getProducts(
        page: Int = 0,
        size: Int = 10,
        sort: String = "name,asc",
        category: String? = null
    ): Result<Page<ApiProduct>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getProducts(
                page = page,
                size = size,
                sort = sort,
                category = if (category == "All") null else category
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Fetch single product by ID
     */
    suspend fun getProduct(productId: Long): Result<ApiProduct> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getProduct(productId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Fetch paginated reviews for a product
     */
    suspend fun getReviews(
        productId: Long,
        page: Int = 0,
        size: Int = 10,
        sort: String = "createdAt,desc",
        rating: Int? = null
    ): Result<Page<ApiReview>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getReviews(
                productId = productId,
                page = page,
                size = size,
                sort = sort,
                rating = rating
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Submit a new review for a product
     */
    suspend fun postReview(
        productId: Long,
        reviewerName: String,
        rating: Int,
        comment: String
    ): Result<ApiReview> = withContext(Dispatchers.IO) {
        try {
            val request = CreateReviewRequest(
                reviewerName = reviewerName,
                rating = rating,
                comment = comment
            )
            val response = apiService.postReview(productId, request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Mark a review as helpful
     */
    suspend fun markReviewAsHelpful(reviewId: Long): Result<ApiReview> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.markReviewAsHelpful(reviewId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
