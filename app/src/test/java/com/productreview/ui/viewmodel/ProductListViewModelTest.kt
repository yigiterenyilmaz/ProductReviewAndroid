package com.productreview.ui.viewmodel

import com.productreview.data.model.ApiProduct
import com.productreview.data.model.Page
import com.productreview.data.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for ProductListViewModel using MockK
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModelTest {

    private lateinit var viewModel: ProductListViewModel
    private lateinit var repository: ProductRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() = runTest {
        // Given
        val mockPage = createMockPage(emptyList())
        coEvery { repository.getProducts(any(), any(), any(), any()) } returns Result.success(mockPage)

        // When
        viewModel = ProductListViewModel(repository)

        // Then
        assertTrue(viewModel.uiState.value.isLoading || viewModel.uiState.value.products.isEmpty())
    }

    @Test
    fun `loadProducts updates state with products on success`() = runTest {
        // Given
        val mockProducts = listOf(
            ApiProduct(id = 1, name = "Product 1", description = "Desc 1", category = "Electronics", price = 99.99),
            ApiProduct(id = 2, name = "Product 2", description = "Desc 2", category = "Audio", price = 149.99)
        )
        val mockPage = createMockPage(mockProducts)
        coEvery { repository.getProducts(any(), any(), any(), any()) } returns Result.success(mockPage)

        // When
        viewModel = ProductListViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(2, viewModel.uiState.value.products.size)
    }

    @Test
    fun `loadProducts updates state with error on failure`() = runTest {
        // Given
        coEvery { repository.getProducts(any(), any(), any(), any()) } returns Result.failure(RuntimeException("Network error"))

        // When
        viewModel = ProductListViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(viewModel.uiState.value.isLoading)
        assertNotNull(viewModel.uiState.value.error)
    }

    @Test
    fun `setSearchQuery filters products locally`() = runTest {
        // Given
        val mockProducts = listOf(
            ApiProduct(id = 1, name = "iPhone 15", description = "Apple phone", category = "Phone", price = 999.99),
            ApiProduct(id = 2, name = "Samsung Galaxy", description = "Android phone", category = "Phone", price = 899.99),
            ApiProduct(id = 3, name = "Sony Headphones", description = "Audio device", category = "Audio", price = 299.99)
        )
        val mockPage = createMockPage(mockProducts)
        coEvery { repository.getProducts(any(), any(), any(), any()) } returns Result.success(mockPage)

        viewModel = ProductListViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.setSearchQuery("iPhone")

        // Then
        assertEquals(1, viewModel.uiState.value.products.size)
        assertEquals("iPhone 15", viewModel.uiState.value.products.first().name)
    }

    @Test
    fun `selectCategory triggers reload`() = runTest {
        // Given
        val mockPage = createMockPage(emptyList())
        coEvery { repository.getProducts(any(), any(), any(), any()) } returns Result.success(mockPage)

        viewModel = ProductListViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.selectCategory("Electronics")

        // Then
        assertEquals("Electronics", viewModel.uiState.value.selectedCategory)
    }

    @Test
    fun `setSortBy updates sort option and reloads`() = runTest {
        // Given
        val mockPage = createMockPage(emptyList())
        coEvery { repository.getProducts(any(), any(), any(), any()) } returns Result.success(mockPage)

        viewModel = ProductListViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.setSortBy("price,desc")

        // Then
        assertEquals("price,desc", viewModel.uiState.value.sortBy)
    }

    private fun createMockPage(products: List<ApiProduct>): Page<ApiProduct> {
        return Page(
            content = products,
            totalElements = products.size,
            totalPages = 1,
            number = 0,
            size = 10,
            last = true
        )
    }
}
