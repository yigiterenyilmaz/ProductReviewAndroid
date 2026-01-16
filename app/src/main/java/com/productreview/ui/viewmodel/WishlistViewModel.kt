package com.productreview.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.productreview.data.local.WishlistEntity
import com.productreview.data.repository.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Wishlist Screen
 */
@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val repository: WishlistRepository
) : ViewModel() {

    val wishlistItems: StateFlow<List<WishlistEntity>> = repository.getAllWishlistItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Remove item from wishlist
     */
    fun removeFromWishlist(productId: Long) {
        viewModelScope.launch {
            repository.removeFromWishlist(productId)
        }
    }
}
