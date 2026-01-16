package com.productreview.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.productreview.data.repository.ProductRepository
import com.productreview.domain.model.Product
import com.productreview.domain.model.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Product Detail Screen
 * Manages product details, reviews, and review submission
 */
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    /**
     * Load product details and initial reviews
     */
    fun loadProduct(productId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Load product details
            val productResult = repository.getProduct(productId)
            productResult.fold(
                onSuccess = { apiProduct ->
                    val product = Product.fromApiModel(apiProduct)
                    _uiState.update { it.copy(product = product) }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to load product"
                        )
                    }
                    return@launch
                }
            )

            // Load reviews
            loadReviews(productId)
        }
    }

    /**
     * Load reviews for the product
     */
    private fun loadReviews(productId: Long, resetPage: Boolean = false) {
        viewModelScope.launch {
            if (resetPage) {
                _uiState.update { it.copy(currentReviewPage = 0, reviews = emptyList()) }
            }

            val currentState = _uiState.value
            val reviewsResult = repository.getReviews(
                productId = productId,
                page = currentState.currentReviewPage,
                size = 10,
                sort = "createdAt,desc",
                rating = currentState.selectedRating
            )

            reviewsResult.fold(
                onSuccess = { page ->
                    val domainReviews = page.content.map { Review.fromApiModel(it) }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            reviews = if (resetPage) domainReviews else it.reviews + domainReviews,
                            currentReviewPage = page.number,
                            totalReviewPages = page.totalPages,
                            hasMoreReviews = !page.last
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to load reviews"
                        )
                    }
                }
            )
        }
    }

    /**
     * Load next page of reviews
     */
    fun loadNextReviews() {
        val currentState = _uiState.value
        val productId = currentState.product?.id ?: return

        if (!currentState.isLoadingReviews && currentState.hasMoreReviews) {
            _uiState.update {
                it.copy(
                    currentReviewPage = it.currentReviewPage + 1,
                    isLoadingReviews = true
                )
            }
            loadReviews(productId)
        }
    }

    /**
     * Filter reviews by rating
     */
    fun filterByRating(rating: Int?) {
        val currentState = _uiState.value
        val productId = currentState.product?.id ?: return

        _uiState.update { it.copy(selectedRating = rating) }
        loadReviews(productId, resetPage = true)
    }

    /**
     * Submit a new review
     */
    fun submitReview(reviewerName: String, rating: Int, comment: String) {
        val productId = _uiState.value.product?.id ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmittingReview = true, submitError = null) }

            val result = repository.postReview(
                productId = productId,
                reviewerName = reviewerName,
                rating = rating,
                comment = comment
            )

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isSubmittingReview = false, reviewSubmitted = true) }
                    // Reload product and reviews to get updated data
                    loadProduct(productId)
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isSubmittingReview = false,
                            submitError = exception.message ?: "Failed to submit review"
                        )
                    }
                }
            )
        }
    }

    /**
     * Mark review as helpful (toggle)
     * Note: This is a local-only toggle. Backend doesn't support toggle,
     * so we only update local state for UI feedback.
     */
    fun markReviewAsHelpful(reviewId: Long) {
        _uiState.update { state ->
            state.copy(
                reviews = state.reviews.map { review ->
                    if (review.id == reviewId) {
                        val newIsMarkedHelpful = !review.isMarkedHelpful
                        review.copy(
                            isMarkedHelpful = newIsMarkedHelpful,
                            helpfulCount = if (newIsMarkedHelpful)
                                review.helpfulCount + 1
                            else
                                review.helpfulCount - 1
                        )
                    } else {
                        review
                    }
                }
            )
        }
    }

    /**
     * Reset review submitted state
     */
    fun resetReviewSubmitted() {
        _uiState.update { it.copy(reviewSubmitted = false, submitError = null) }
    }
}

/**
 * UI State for Product Detail Screen
 */
data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val product: Product? = null,
    val reviews: List<Review> = emptyList(),
    val error: String? = null,
    val currentReviewPage: Int = 0,
    val totalReviewPages: Int = 0,
    val hasMoreReviews: Boolean = true,
    val isLoadingReviews: Boolean = false,
    val selectedRating: Int? = null,
    val isSubmittingReview: Boolean = false,
    val reviewSubmitted: Boolean = false,
    val submitError: String? = null
)
