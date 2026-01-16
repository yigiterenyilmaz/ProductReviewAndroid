package com.productreview.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/**
 * Rating breakdown component showing distribution of star ratings
 * with optional filtering capability
 */
@Composable
fun RatingBreakdown(
    breakdown: Map<Int, Int>,
    totalCount: Int,
    selectedRating: Int? = null,
    onSelectRating: ((Int?) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Rating bars for 5 to 1 stars
        (5 downTo 1).forEach { rating ->
            val count = breakdown[rating] ?: 0
            val percentage = if (totalCount > 0) (count.toFloat() / totalCount) else 0f
            val isSelected = selectedRating == rating

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(enabled = onSelectRating != null) {
                        onSelectRating?.invoke(if (isSelected) null else rating)
                    }
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.background
                    )
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Star label
                Text(
                    text = "${rating}★",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.width(38.dp)
                )

                // Progress bar
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(MaterialTheme.colorScheme.outline)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(percentage)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(999.dp)
                            )
                    )
                }

                // Count
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) MaterialTheme.colorScheme.onSurface
                           else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(32.dp)
                )
            }
        }

        // Helper text when filter is active
        if (onSelectRating != null && selectedRating != null) {
            Text(
                text = "Showing ${selectedRating}★ reviews — tap again to clear.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
