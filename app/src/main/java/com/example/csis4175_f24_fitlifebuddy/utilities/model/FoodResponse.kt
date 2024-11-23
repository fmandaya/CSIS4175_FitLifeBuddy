package com.example.csis4175_f24_fitlifebuddy.utilities.model

import com.google.gson.annotations.SerializedName

data class FoodResponse(
    val product: Product?
)

data class Nutriments(
    @SerializedName("energy-kcal_100g") private val _energy_kcal_100g: Float? = null,
    @SerializedName("proteins_100g") private val _proteins_100g: Float? = null,
    @SerializedName("fat_100g") private val _fat_100g: Float? = null,
    @SerializedName("carbohydrates_100g") private val _carbohydrates_100g: Float? = null,
    @SerializedName("sugars_100g") private val _sugars_100g: Float? = null,
    @SerializedName("fiber_100g") private val _fiber_100g: Float? = null,
    @SerializedName("salt_100g") private val _salt_100g: Float? = null
) {
    // No-argument constructor for Firestore
    constructor() : this(null, null, null, null, null, null, null)

    val energy_kcal_100g: Float?
        get() = _energy_kcal_100g?.let { String.format("%.2f", it).toFloat() }

    val proteins_100g: Float?
        get() = _proteins_100g?.let { String.format("%.2f", it).toFloat() }

    val fat_100g: Float?
        get() = _fat_100g?.let { String.format("%.2f", it).toFloat() }

    val carbohydrates_100g: Float?
        get() = _carbohydrates_100g?.let { String.format("%.2f", it).toFloat() }

    val sugars_100g: Float?
        get() = _sugars_100g?.let { String.format("%.2f", it).toFloat() }

    val fiber_100g: Float?
        get() = _fiber_100g?.let { String.format("%.2f", it).toFloat() }

    val salt_100g: Float?
        get() = _salt_100g?.let { String.format("%.2f", it).toFloat() }

    // Converts the Nutriments object into a Map for Firestore
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "energy_kcal_100g" to energy_kcal_100g,
            "proteins_100g" to proteins_100g,
            "fat_100g" to fat_100g,
            "carbohydrates_100g" to carbohydrates_100g,
            "sugars_100g" to sugars_100g,
            "fiber_100g" to fiber_100g,
            "salt_100g" to salt_100g
        )
    }
}

data class Product(
    @SerializedName("id") val product_ID: String? = null,
    @SerializedName("product_name") val product_name: String? = null,
    @SerializedName("image_url") val image_url: String? = null,
    @SerializedName("nutriments") val nutriments: Nutriments? = null
) {
    // No-argument constructor for Firestore
    constructor() : this(null, null, null, null)

    // Converts the Product object into a Map for Firestore
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "productID" to product_ID,
            "productName" to product_name,
            "image_url" to image_url,
            "nutriments" to nutriments?.toMap() // Call the similar method in Nutriments
        )
    }
}

data class SearchResponse(
    @SerializedName("products") val products: List<Product>?
)