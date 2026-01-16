package com.productreview.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.productreview.data.mock.MockNotifications
import com.productreview.domain.model.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * ViewModel for Notification Screen
 */
@HiltViewModel
class NotificationViewModel @Inject constructor() : ViewModel() {

    private val _notifications = MutableStateFlow(MockNotifications.getNotifications())
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()

    private val _unreadCount = MutableStateFlow(MockNotifications.getUnreadCount())
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    /**
     * Mark notification as read
     */
    fun markAsRead(notificationId: String) {
        _notifications.update { notificationList ->
            notificationList.map { notification ->
                if (notification.id == notificationId && !notification.isRead) {
                    _unreadCount.update { it - 1 }
                    notification.copy(isRead = true)
                } else {
                    notification
                }
            }
        }
    }

    /**
     * Mark all notifications as read
     */
    fun markAllAsRead() {
        _notifications.update { notificationList ->
            notificationList.map { it.copy(isRead = true) }
        }
        _unreadCount.value = 0
    }

    /**
     * Delete notification
     */
    fun deleteNotification(notificationId: String) {
        val notification = _notifications.value.find { it.id == notificationId }
        if (notification != null && !notification.isRead) {
            _unreadCount.update { it - 1 }
        }
        _notifications.update { it.filter { notification -> notification.id != notificationId } }
    }
}
