package com.productreview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.productreview.ui.screens.AIAssistantScreen
import com.productreview.ui.screens.NotificationScreen
import com.productreview.ui.screens.ProductDetailScreen
import com.productreview.ui.screens.ProductListScreen
import com.productreview.ui.screens.SettingsScreen
import com.productreview.ui.screens.SplashScreen
import com.productreview.ui.screens.WishlistScreen
import com.productreview.ui.theme.ProductReviewTheme
import com.productreview.ui.viewmodel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity - Entry point of the application
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var showSplash by remember { mutableStateOf(true) }

            if (showSplash) {
                SplashScreen(
                    onSplashComplete = { showSplash = false }
                )
            } else {
                val themeViewModel: ThemeViewModel = hiltViewModel()
                val isDarkMode by themeViewModel.isDarkMode.collectAsStateWithLifecycle()

                ProductReviewTheme(darkTheme = isDarkMode) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()

                        NavHost(
                            navController = navController,
                            startDestination = "product_list"
                        ) {
                        // Product List Screen
                        composable("product_list") {
                            ProductListScreen(
                                onProductClick = { productId ->
                                    navController.navigate("product_detail/$productId")
                                },
                                onWishlistClick = {
                                    navController.navigate("wishlist")
                                },
                                onNotificationClick = {
                                    navController.navigate("notifications")
                                },
                                onSettingsClick = {
                                    navController.navigate("settings")
                                },
                                onAIAssistantClick = {
                                    navController.navigate("ai_assistant")
                                }
                            )
                        }

                        // Product Detail Screen
                        composable(
                            route = "product_detail/{productId}",
                            arguments = listOf(
                                navArgument("productId") { type = NavType.LongType }
                            )
                        ) { backStackEntry ->
                            val productId = backStackEntry.arguments?.getLong("productId") ?: 0L
                            ProductDetailScreen(
                                productId = productId,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        // Wishlist Screen
                        composable("wishlist") {
                            WishlistScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onProductClick = { productId ->
                                    navController.navigate("product_detail/$productId")
                                }
                            )
                        }

                        // Notifications Screen
                        composable("notifications") {
                            NotificationScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        // Settings Screen
                        composable("settings") {
                            SettingsScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        // AI Assistant Screen
                        composable("ai_assistant") {
                            AIAssistantScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
            }
        }
    }
}
