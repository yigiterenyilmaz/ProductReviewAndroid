package com.productreview.domain.model

/**
 * Chat message model for AI Assistant
 */
data class ChatMessage(
    val id: String,
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
