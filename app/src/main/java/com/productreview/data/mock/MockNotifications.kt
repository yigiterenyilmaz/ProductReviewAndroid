package com.productreview.data.mock

import com.productreview.domain.model.Notification
import com.productreview.domain.model.NotificationType

/**
 * Mock notification data
 */
object MockNotifications {

    private val currentTime = System.currentTimeMillis()

    private val notifications = listOf(
        Notification(
            id = "1",
            title = "New Review on Your Wishlist!",
            message = "Premium Wireless Headphones received a new 5-star review",
            timestamp = currentTime - 3600000, // 1 hour ago
            type = NotificationType.NEW_REVIEW,
            isRead = false
        ),
        Notification(
            id = "2",
            title = "Price Drop Alert!",
            message = "Smart Fitness Watch Pro is now 15% off - $254.99",
            timestamp = currentTime - 7200000, // 2 hours ago
            type = NotificationType.PRICE_DROP,
            isRead = false
        ),
        Notification(
            id = "3",
            title = "Back in Stock",
            message = "Mechanical Gaming Keyboard is back in stock!",
            timestamp = currentTime - 21600000, // 6 hours ago
            type = NotificationType.WISHLIST_UPDATE,
            isRead = true
        ),
        Notification(
            id = "4",
            title = "Weekly Deals",
            message = "Check out this week's featured deals on electronics",
            timestamp = currentTime - 86400000, // 1 day ago
            type = NotificationType.GENERAL,
            isRead = true
        ),
        Notification(
            id = "5",
            title = "New Product Launch",
            message = "Ultra-Slim Power Bank 30,000mAh is now available",
            timestamp = currentTime - 172800000, // 2 days ago
            type = NotificationType.GENERAL,
            isRead = true
        ),
        Notification(
            id = "6",
            title = "Review Reminder",
            message = "How's your recent purchase? Leave a review and help others!",
            timestamp = currentTime - 259200000, // 3 days ago
            type = NotificationType.GENERAL,
            isRead = true
        ),
        Notification(
            id = "7",
            title = "Price Drop Alert!",
            message = "Portable Bluetooth Speaker is now $99.99 (23% off)",
            timestamp = currentTime - 345600000, // 4 days ago
            type = NotificationType.PRICE_DROP,
            isRead = true
        ),
        Notification(
            id = "8",
            title = "New Review",
            message = "4K Webcam Pro received 10 new reviews this week",
            timestamp = currentTime - 432000000, // 5 days ago
            type = NotificationType.NEW_REVIEW,
            isRead = true
        )
    )

    fun getNotifications(): List<Notification> = notifications

    fun getUnreadCount(): Int = notifications.count { !it.isRead }
}
