package com.example.csis4175_f24_fitlifebuddy.utilities.model

import com.google.gson.annotations.SerializedName

data class FoodResponse(
    val product: Product?
)

data class Nutriments(
    @SerializedName("energy-kcal_100g") private val _energy_kcal_100g: Float?, // Caloric info per 100g
    @SerializedName("proteins_100g") private val _proteins_100g: Float?,
    @SerializedName("fat_100g") private val _fat_100g: Float?,
    @SerializedName("carbohydrates_100g") private val _carbohydrates_100g: Float?,
    @SerializedName("sugars_100g") private val _sugars_100g: Float?,
    @SerializedName("fiber_100g") private val _fiber_100g: Float?,
    @SerializedName("salt_100g") private val _salt_100g: Float?
) {
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
}

data class Product(
    @SerializedName("product_name") val product_name: String?,
    @SerializedName("image_url") val image_url: String?,
    @SerializedName("nutriments") val nutriments: Nutriments?
)

data class SearchResponse(
    @SerializedName("products") val products: List<Product>?
)