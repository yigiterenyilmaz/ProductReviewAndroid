package com.productreview.domain.model

/**
 * Notification model
 */
data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: Long,
    val type: NotificationType,
    val isRead: Boolean = false
)

/**
 * Notification types
 */
enum class NotificationType {
    NEW_REVIEW,
    PRICE_DROP,
    WISHLIST_UPDATE,
    GENERAL
}
