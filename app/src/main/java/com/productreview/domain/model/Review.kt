package com.productreview.domain.model

import com.productreview.data.model.ApiReview
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Domain model for Review
 */
data class Review(
    val id: Long,
    val reviewerName: String,
    val rating: Int,
    val comment: String,
    val helpfulCount: Int,
    val createdAt: LocalDateTime?,
    val isMarkedHelpful: Boolean = false
) {
    companion object {
        private val ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME

        /**
         * Map API DTO to domain model
         */
        fun fromApiModel(apiReview: ApiReview): Review {
            val createdAt = try {
                apiReview.createdAt?.let { LocalDateTime.parse(it, ISO_FORMATTER) }
            } catch (e: Exception) {
                null
            }

            return Review(
                id = apiReview.id ?: 0L,
                reviewerName = apiReview.reviewerName ?: "Anonymous",
                rating = apiReview.rating,
                comment = apiReview.comment,
                helpfulCount = apiReview.helpfulCount ?: 0,
                createdAt = createdAt
            )
        }
    }

    /**
     * Get relative time string (e.g., "2 days ago")
     */
    val relativeTime: String
        get() {
            if (createdAt == null) return "Recently"

            val now = LocalDateTime.now()
            val days = ChronoUnit.DAYS.between(createdAt, now)
            val hours = ChronoUnit.HOURS.between(createdAt, now)
            val minutes = ChronoUnit.MINUTES.between(createdAt, now)

            return when {
                days > 30 -> "${days / 30} month${if (days / 30 > 1) "s" else ""} ago"
                days > 0 -> "$days day${if (days > 1) "s" else ""} ago"
                hours > 0 -> "$hours hour${if (hours > 1) "s" else ""} ago"
                minutes > 0 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
                else -> "Just now"
            }
        }

    /**
     * Formatted date string
     */
    val formattedDate: String
        get() = createdAt?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) ?: "Unknown"
}
