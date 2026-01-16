package com.productreview.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.productreview.ui.theme.LightStarEmpty
import com.productreview.ui.theme.LightStarFilled
import com.productreview.ui.theme.DarkStarEmpty
import com.productreview.ui.theme.DarkStarFilled
import kotlin.math.floor

/**
 * Star rating component
 * @param rating Current rating value
 * @param maxRating Maximum rating (default 5)
 * @param size Size of stars (sm=16dp, md=20dp, lg=24dp)
 * @param showValue Show numeric value next to stars
 * @param modifier Modifier for customization
 */
@Composable
fun StarRating(
    rating: Double,
    modifier: Modifier = Modifier,
    maxRating: Int = 5,
    size: StarSize = StarSize.Medium,
    showValue: Boolean = false
) {
    val isDark = MaterialTheme.colorScheme.background == com.productreview.ui.theme.DarkBackground
    val filledColor = if (isDark) DarkStarFilled else LightStarFilled
    val emptyColor = if (isDark) DarkStarEmpty else LightStarEmpty

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Star icons
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(maxRating) { index ->
                val isFilled = index < floor(rating).toInt()
                val isPartial = index == floor(rating).toInt() && rating % 1 >= 0.5

                Icon(
                    imageVector = if (isFilled || isPartial) Icons.Filled.Star else Icons.Outlined.StarOutline,
                    contentDescription = null,
                    modifier = Modifier.size(size.dp),
                    tint = if (isFilled || isPartial) filledColor else emptyColor
                )
            }
        }

        // Rating value
        if (showValue) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = String.format("%.1f", rating),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

enum class StarSize(val dp: Dp) {
    Small(16.dp),
    Medium(20.dp),
    Large(24.dp)
}
