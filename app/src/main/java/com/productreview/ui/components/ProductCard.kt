package com.productreview.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.productreview.data.repository.WishlistRepository
import com.productreview.domain.model.Product
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing wishlist state in ProductCard
 */
@HiltViewModel
class ProductCardViewModel @Inject constructor(
    private val wishlistRepository: WishlistRepository
) : ViewModel() {

    fun isInWishlist(productId: Long): StateFlow<Boolean> {
        return wishlistRepository.isInWishlist(productId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = false
            )
    }

    fun toggleWishlist(product: Product, isInWishlist: Boolean) {
        viewModelScope.launch {
            wishlistRepository.toggleWishlist(product, isInWishlist)
        }
    }
}

/**
 * Product card component for grid display
 */
@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductCardViewModel = hiltViewModel()
) {
    val isInWishlist by viewModel.isInWishlist(product.id).collectAsStateWithLifecycle()
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            // Product image with category badge and wishlist button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            ) {
                AsyncImage(
                    model = product.imageUrl ?: getDefaultImageForCategory(product.category),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Category badge
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart)
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(999.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = product.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }

                // Wishlist button
                IconButton(
                    onClick = { viewModel.toggleWishlist(product, isInWishlist) },
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopEnd)
                        .background(
                            color = Color.White.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(999.dp)
                        )
                        .size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isInWishlist) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isInWishlist) "Remove from wishlist" else "Add to wishlist",
                        tint = if (isInWishlist) Color.Red else Color.Gray
                    )
                }
            }

            // Product info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Product name
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Rating and review count
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StarRating(
                        rating = product.averageRating,
                        size = StarSize.Small
                    )
                    Text(
                        text = "(${product.reviewCount})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Price
                Text(
                    text = product.formattedPrice,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * Get default image URL based on category
 */
private fun getDefaultImageForCategory(category: String): String {
    return when (category.lowercase()) {
        "audio" -> "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&q=80"
        "phone", "mobile", "smartphone" -> "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=800&q=80"
        "camera", "photo" -> "https://images.unsplash.com/photo-1519183071298-a2962be96cdb?w=800&q=80"
        "watch", "wearable" -> "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800&q=80"
        "laptop", "computer" -> "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=800&q=80"
        else -> "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=800&q=80"
    }
}
