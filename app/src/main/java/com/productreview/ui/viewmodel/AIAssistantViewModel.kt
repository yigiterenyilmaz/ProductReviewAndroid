package com.productreview.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.productreview.domain.model.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel for AI Assistant Screen
 */
@HiltViewModel
class AIAssistantViewModel @Inject constructor() : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                id = UUID.randomUUID().toString(),
                text = "Hi! I'm your AI shopping assistant. How can I help you today? I can help you find products, compare features, or answer questions about our products.",
                isFromUser = false
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping.asStateFlow()

    /**
     * Send a message
     */
    fun sendMessage(text: String) {
        if (text.isBlank()) return

        // Add user message
        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = text,
            isFromUser = true
        )
        _messages.update { it + userMessage }

        // Simulate AI response
        viewModelScope.launch {
            _isTyping.value = true
            delay(1500) // Simulate processing time

            val aiResponse = generateAIResponse(text)
            val aiMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                text = aiResponse,
                isFromUser = false
            )
            _messages.update { it + aiMessage }
            _isTyping.value = false
        }
    }

    /**
     * Generate mock AI response based on user input
     */
    private fun generateAIResponse(userInput: String): String {
        val lowerInput = userInput.lowercase()

        return when {
            lowerInput.contains("headphone") || lowerInput.contains("audio") -> {
                "I'd recommend our Premium Wireless Headphones! They feature:\n\n" +
                "• Active noise cancellation\n" +
                "• 40-hour battery life\n" +
                "• Premium comfort\n" +
                "• Adaptive EQ\n\n" +
                "Price: $349.99\n" +
                "Rating: ⭐ 4.7/5 (2,847 reviews)\n\n" +
                "Would you like to know more about this product or compare it with others?"
            }
            lowerInput.contains("watch") || lowerInput.contains("fitness") || lowerInput.contains("wearable") -> {
                "The Smart Fitness Watch Pro is perfect for you! Key features:\n\n" +
                "• Advanced health sensors\n" +
                "• GPS tracking\n" +
                "• 7-day battery life\n" +
                "• Water-resistant (50m)\n" +
                "• Sleep & stress monitoring\n\n" +
                "Price: $299.99\n" +
                "Rating: ⭐ 4.5/5 (1,923 reviews)\n\n" +
                "It's great for tracking workouts and daily health metrics!"
            }
            lowerInput.contains("laptop") || lowerInput.contains("computer") -> {
                "We have several excellent laptops available! Could you tell me more about your needs?\n\n" +
                "• Budget range?\n" +
                "• Primary use (gaming, work, study)?\n" +
                "• Screen size preference?\n" +
                "• Any specific requirements?\n\n" +
                "This will help me recommend the best laptop for you!"
            }
            lowerInput.contains("gaming") || lowerInput.contains("keyboard") -> {
                "Check out our Mechanical Gaming Keyboard! Features:\n\n" +
                "• RGB backlit\n" +
                "• Customizable switches\n" +
                "• Macro keys\n" +
                "• Aircraft-grade aluminum frame\n" +
                "• N-key rollover\n\n" +
                "Price: $179.99\n" +
                "Rating: ⭐ 4.6/5 (1,567 reviews)\n\n" +
                "Perfect for both competitive gaming and typing!"
            }
            lowerInput.contains("compare") -> {
                "I'd be happy to help you compare products! Please tell me which products you're interested in comparing, and I'll highlight the key differences in:\n\n" +
                "• Features\n" +
                "• Price\n" +
                "• Ratings\n" +
                "• User reviews\n\n" +
                "Just let me know the product names or categories!"
            }
            lowerInput.contains("price") || lowerInput.contains("deal") || lowerInput.contains("discount") -> {
                "We have some great deals right now! Here are today's featured offers:\n\n" +
                "🔥 Smart Fitness Watch Pro - 15% off ($254.99)\n" +
                "🔥 Portable Bluetooth Speaker - 23% off ($99.99)\n" +
                "🔥 Ultra-Slim Power Bank - Free shipping\n\n" +
                "Would you like more details on any of these deals?"
            }
            lowerInput.contains("recommend") || lowerInput.contains("suggest") || lowerInput.contains("best") -> {
                "I'd love to help you find the perfect product! Could you tell me:\n\n" +
                "• What are you looking for?\n" +
                "• Your budget range?\n" +
                "• Any must-have features?\n" +
                "• Preferred category (Audio, Wearables, Gaming, etc.)?\n\n" +
                "The more details you share, the better I can help!"
            }
            lowerInput.contains("thank") || lowerInput.contains("thanks") -> {
                "You're very welcome! Is there anything else I can help you with today? I'm here to assist with product recommendations, comparisons, or any questions you might have! 😊"
            }
            lowerInput.contains("hello") || lowerInput.contains("hi") || lowerInput.contains("hey") -> {
                "Hello! 👋 It's great to hear from you! How can I assist you today? I can help you:\n\n" +
                "• Find the perfect product\n" +
                "• Compare different options\n" +
                "• Check current deals\n" +
                "• Answer product questions\n\n" +
                "What are you interested in?"
            }
            else -> {
                "That's an interesting question! Based on what you've asked, I can help you explore our product catalog. We have a wide range of:\n\n" +
                "• Electronics & Laptops\n" +
                "• Audio Equipment\n" +
                "• Wearables & Fitness Trackers\n" +
                "• Gaming Accessories\n" +
                "• Mobile Accessories\n\n" +
                "Could you tell me more about what you're looking for? I'm here to help you find exactly what you need!"
            }
        }
    }

    /**
     * Clear chat history
     */
    fun clearChat() {
        _messages.value = listOf(
            ChatMessage(
                id = UUID.randomUUID().toString(),
                text = "Chat cleared! How can I help you today?",
                isFromUser = false
            )
        )
    }
}
