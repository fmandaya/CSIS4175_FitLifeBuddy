package com.example.csis4175_f24_fitlifebuddy.utilities.model

import com.google.gson.annotations.SerializedName

data class FoodResponse(
    val product: Product?
)

data class Nutriments(
    @SerializedName("energy-kcal_100g") val energy_kcal_100g: Float?, // Caloric info per 100g
    @SerializedName("proteins_100g") val proteins_100g: Float?,
    @SerializedName("fat_100g") val fat_100g: Float?,
    @SerializedName("carbohydrates_100g") val carbohydrates_100g: Float?,
    @SerializedName("sugars_100g") val sugars_100g: Float?,
    @SerializedName("fiber_100g") val fiber_100g: Float?,
    @SerializedName("salt_100g") val salt_100g: Float?
)

data class Product(
    @SerializedName("product_name") val product_name: String?,
    @SerializedName("image_url") val image_url: String?,
    @SerializedName("nutriments") val nutriments: Nutriments?
)

data class SearchResponse(
    @SerializedName("products") val products: List<Product>?
)