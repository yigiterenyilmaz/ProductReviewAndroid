package com.productreview.data.repository

import com.productreview.data.api.ProductApiService
import com.productreview.data.model.ApiProduct
import com.productreview.data.model.Page
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for ProductRepository using MockK
 */
class ProductRepositoryTest {

    private lateinit var repository: ProductRepository
    private lateinit var apiService: ProductApiService

    @Before
    fun setup() {
        apiService = mockk()
        repository = ProductRepository(apiService)
    }

    @Test
    fun `getProducts returns success when API call succeeds`() = runTest {
        // Given
        val mockProducts = listOf(
            ApiProduct(
                id = 1,
                name = "Test Product",
                description = "Test Description",
                category = "Electronics",
                price = 99.99
            )
        )
        val mockPage = Page(
            content = mockProducts,
            totalElements = 1,
            totalPages = 1,
            number = 0,
            size = 10,
            last = true
        )
        coEvery { apiService.getProducts(0, 10, "name,asc", null) } returns mockPage

        // When
        val result = repository.getProducts(page = 0, size = 10, sort = "name,asc", category = null)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.content?.size)
        assertEquals("Test Product", result.getOrNull()?.content?.first()?.name)
    }

    @Test
    fun `getProducts returns failure when API call fails`() = runTest {
        // Given
        coEvery { apiService.getProducts(0, 10, "name,asc", null) } throws RuntimeException("Network error")

        // When
        val result = repository.getProducts(page = 0, size = 10, sort = "name,asc", category = null)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `getProductById returns success when product exists`() = runTest {
        // Given
        val mockProduct = ApiProduct(
            id = 1,
            name = "Test Product",
            description = "Test Description",
            category = "Electronics",
            price = 99.99
        )
        coEvery { apiService.getProductById(1) } returns mockProduct

        // When
        val result = repository.getProductById(1)

        // Then
        assertTrue(result.isSuccess)
        assertEquals("Test Product", result.getOrNull()?.name)
    }

    @Test
    fun `getProductById returns failure when product not found`() = runTest {
        // Given
        coEvery { apiService.getProductById(999) } throws RuntimeException("Product not found")

        // When
        val result = repository.getProductById(999)

        // Then
        assertTrue(result.isFailure)
    }
}
