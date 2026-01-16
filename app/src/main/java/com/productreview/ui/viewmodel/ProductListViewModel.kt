package com.productreview.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.productreview.data.repository.ProductRepository
import com.productreview.domain.model.Category
import com.productreview.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Product List Screen
 * Manages product fetching, pagination, filtering, and sorting
 */
@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    private var allProducts: List<Product> = emptyList()

    init {
        loadProducts()
    }

    /**
     * Load products with current filters
     */
    fun loadProducts(resetPage: Boolean = false) {
        viewModelScope.launch {
            if (resetPage) {
                _uiState.update { it.copy(currentPage = 0, products = emptyList()) }
            }

            _uiState.update { it.copy(isLoading = true, error = null) }

            val currentState = _uiState.value
            val result = repository.getProducts(
                page = currentState.currentPage,
                size = 10,
                sort = currentState.sortBy,
                category = if (currentState.selectedCategory == Category.ALL) null else currentState.selectedCategory
            )

            result.fold(
                onSuccess = { page ->
                    val domainProducts = page.content.map { Product.fromApiModel(it) }
                    if (resetPage) {
                        allProducts = domainProducts
                    } else {
                        allProducts = allProducts + domainProducts
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            products = filterProductsBySearch(allProducts, it.searchQuery),
                            currentPage = page.number,
                            totalPages = page.totalPages,
                            hasMore = !page.last
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to load products"
                        )
                    }
                }
            )
        }
    }

    /**
     * Load next page of products
     */
    fun loadNextPage() {
        val currentState = _uiState.value
        if (!currentState.isLoading && currentState.hasMore) {
            _uiState.update { it.copy(currentPage = it.currentPage + 1) }
            loadProducts()
        }
    }

    /**
     * Change category filter
     */
    fun selectCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadProducts(resetPage = true)
    }

    /**
     * Change sort order
     */
    fun setSortBy(sortBy: String) {
        _uiState.update { it.copy(sortBy = sortBy) }
        loadProducts(resetPage = true)
    }

    /**
     * Refresh products
     */
    fun refresh() {
        loadProducts(resetPage = true)
    }

    /**
     * Update search query
     */
    fun setSearchQuery(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                products = filterProductsBySearch(allProducts, query)
            )
        }
    }

    /**
     * Filter products by search query
     */
    private fun filterProductsBySearch(products: List<Product>, query: String): List<Product> {
        if (query.isBlank()) return products

        val lowerQuery = query.lowercase()
        return products.filter { product ->
            product.name.lowercase().contains(lowerQuery) ||
            product.description.lowercase().contains(lowerQuery) ||
            product.category.lowercase().contains(lowerQuery)
        }
    }
}

/**
 * UI State for Product List Screen
 */
data class ProductListUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null,
    val currentPage: Int = 0,
    val totalPages: Int = 0,
    val hasMore: Boolean = true,
    val selectedCategory: String = Category.ALL,
    val sortBy: String = "name,asc",
    val searchQuery: String = ""
)
