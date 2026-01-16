package com.productreview.domain.model

/**
 * Product categories matching backend data
 */
object Category {
    const val ALL = "All"
    const val ELECTRONICS = "Electronics"
    const val LAPTOPS = "Laptops"
    const val TABLETS = "Tablets"
    const val WEARABLES = "Wearables"
    const val GAMING = "Gaming"
    const val AUDIO = "Audio"
    const val ACCESSORIES = "Accessories"

    val categories = listOf(
        ALL,
        ELECTRONICS,
        LAPTOPS,
        TABLETS,
        WEARABLES,
        GAMING,
        AUDIO,
        ACCESSORIES
    )
}
