package com.productreview.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.productreview.ui.theme.ProductReviewTheme
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for ProductListScreen using Compose Test Rule
 */
class ProductListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun productListScreen_displaysTopBar() {
        // Given
        composeTestRule.setContent {
            ProductReviewTheme {
                ProductListScreen(
                    onProductClick = {},
                    onWishlistClick = {},
                    onNotificationClick = {},
                    onSettingsClick = {},
                    onAIAssistantClick = {}
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Products").assertIsDisplayed()
    }

    @Test
    fun productListScreen_displaysSearchBar() {
        // Given
        composeTestRule.setContent {
            ProductReviewTheme {
                ProductListScreen(
                    onProductClick = {},
                    onWishlistClick = {},
                    onNotificationClick = {},
                    onSettingsClick = {},
                    onAIAssistantClick = {}
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Search products...").assertIsDisplayed()
    }

    @Test
    fun productListScreen_displaysAIHelpButton() {
        // Given
        composeTestRule.setContent {
            ProductReviewTheme {
                ProductListScreen(
                    onProductClick = {},
                    onWishlistClick = {},
                    onNotificationClick = {},
                    onSettingsClick = {},
                    onAIAssistantClick = {}
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("AI Help").assertIsDisplayed()
    }

    @Test
    fun productListScreen_settingsButtonClickable() {
        // Given
        var clicked = false
        composeTestRule.setContent {
            ProductReviewTheme {
                ProductListScreen(
                    onProductClick = {},
                    onWishlistClick = {},
                    onNotificationClick = {},
                    onSettingsClick = { clicked = true },
                    onAIAssistantClick = {}
                )
            }
        }

        // When
        composeTestRule.onNodeWithContentDescription("Settings").performClick()

        // Then
        assert(clicked)
    }

    @Test
    fun productListScreen_wishlistButtonClickable() {
        // Given
        var clicked = false
        composeTestRule.setContent {
            ProductReviewTheme {
                ProductListScreen(
                    onProductClick = {},
                    onWishlistClick = { clicked = true },
                    onNotificationClick = {},
                    onSettingsClick = {},
                    onAIAssistantClick = {}
                )
            }
        }

        // When
        composeTestRule.onNodeWithContentDescription("Wishlist").performClick()

        // Then
        assert(clicked)
    }

    @Test
    fun productListScreen_filterButtonClickable() {
        // Given
        composeTestRule.setContent {
            ProductReviewTheme {
                ProductListScreen(
                    onProductClick = {},
                    onWishlistClick = {},
                    onNotificationClick = {},
                    onSettingsClick = {},
                    onAIAssistantClick = {}
                )
            }
        }

        // When
        composeTestRule.onNodeWithContentDescription("Filter").performClick()

        // Then - Filter bottom sheet should appear
        composeTestRule.onNodeWithText("Filter by Category").assertIsDisplayed()
    }

    @Test
    fun productListScreen_sortButtonClickable() {
        // Given
        composeTestRule.setContent {
            ProductReviewTheme {
                ProductListScreen(
                    onProductClick = {},
                    onWishlistClick = {},
                    onNotificationClick = {},
                    onSettingsClick = {},
                    onAIAssistantClick = {}
                )
            }
        }

        // When
        composeTestRule.onNodeWithContentDescription("Sort").performClick()

        // Then - Sort bottom sheet should appear
        composeTestRule.onNodeWithText("Sort by").assertIsDisplayed()
    }
}
