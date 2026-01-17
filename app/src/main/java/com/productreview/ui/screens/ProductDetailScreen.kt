package com.productreview.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.productreview.ui.components.RatingBreakdown
import com.productreview.ui.components.ReviewCard
import com.productreview.ui.components.StarRating
import com.productreview.ui.components.StarSize
import com.productreview.ui.viewmodel.ProductDetailViewModel

/**
 * Product Detail Screen
 * Shows product details, rating breakdown, AI summary, and reviews
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Long,
    onBackClick: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showAddReview by remember { mutableStateOf(false) }

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    // Show success message when review is submitted
    LaunchedEffect(uiState.reviewSubmitted) {
        if (uiState.reviewSubmitted) {
            viewModel.resetReviewSubmitted()
        }
    }

    // Show error if review submission fails
    LaunchedEffect(uiState.submitError) {
        if (uiState.submitError != null) {
            android.util.Log.e("ProductDetail", "Review submit error: ${uiState.submitError}")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.product?.name ?: "Product Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState.product != null) {
                FloatingActionButton(onClick = { showAddReview = true }) {
                    Text("+ Review")
                }
            }
        }
    ) { paddingValues ->
        when {
            uiState.isLoading && uiState.product == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null && uiState.product == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = uiState.error ?: "Unknown error",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadProduct(productId) }) {
                            Text("Retry")
                        }
                    }
                }
            }

            uiState.product != null -> {
                val product = uiState.product!!

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Product image
                    item {
                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = product.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Product info
                    item {
                        Card {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = product.name,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = product.formattedPrice,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = product.description,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    // Rating summary
                    item {
                        Card {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = String.format("%.1f", product.averageRating),
                                        style = MaterialTheme.typography.displayMedium
                                    )
                                    Column {
                                        StarRating(
                                            rating = product.averageRating,
                                            size = StarSize.Medium
                                        )
                                        Text(
                                            text = "${product.reviewCount} reviews",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Rating breakdown
                    if (product.hasReviews) {
                        item {
                            Card {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Rating Breakdown",
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )
                                    RatingBreakdown(
                                        breakdown = product.ratingBreakdown,
                                        totalCount = product.reviewCount,
                                        selectedRating = uiState.selectedRating,
                                        onSelectRating = { viewModel.filterByRating(it) }
                                    )
                                }
                            }
                        }
                    }

                    // AI Summary
                    if (!product.aiSummary.isNullOrBlank()) {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "🤖 AI Summary",
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    Text(
                                        text = product.aiSummary,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }

                    // Reviews section header
                    item {
                        Text(
                            text = "Reviews",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    // Reviews list
                    items(
                        items = uiState.reviews,
                        key = { it.id }
                    ) { review ->
                        ReviewCard(
                            review = review,
                            onHelpfulClick = { viewModel.markReviewAsHelpful(it) },
                            isHelpful = review.isMarkedHelpful
                        )
                    }

                    // Load more reviews
                    if (uiState.hasMoreReviews && !uiState.isLoadingReviews) {
                        item {
                            LaunchedEffect(Unit) {
                                viewModel.loadNextReviews()
                            }
                        }
                    }

                    if (uiState.isLoadingReviews) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Review Dialog
    if (showAddReview) {
        AddReviewDialog(
            onDismiss = { showAddReview = false },
            onSubmit = { name, rating, comment ->
                viewModel.submitReview(name, rating, comment)
                showAddReview = false
            },
            isSubmitting = uiState.isSubmittingReview
        )
    }
}

/**
 * Add Review Dialog
 */
@Composable
fun AddReviewDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, Int, String) -> Unit,
    isSubmitting: Boolean
) {
    var name by remember { mutableStateOf("") }
    var rating by remember { mutableIntStateOf(5) }
    var comment by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Write a Review") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Your Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Rating: $rating stars")
                Slider(
                    value = rating.toFloat(),
                    onValueChange = { rating = it.toInt() },
                    valueRange = 1f..5f,
                    steps = 3
                )

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Your Review") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(name, rating, comment) },
                enabled = !isSubmitting && name.isNotBlank() && comment.isNotBlank()
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text("Submit")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
