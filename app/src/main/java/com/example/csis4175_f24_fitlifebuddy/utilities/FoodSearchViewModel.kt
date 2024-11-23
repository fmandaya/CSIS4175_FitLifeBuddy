package com.example.csis4175_f24_fitlifebuddy.utilities

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csis4175_f24_fitlifebuddy.utilities.model.Product
import kotlinx.coroutines.launch

class FoodSearchViewModel : ViewModel() {
    var foodItems by mutableStateOf<List<Product>>(emptyList())
    var isLoading by mutableStateOf(false)
    var selectedProduct by mutableStateOf<Product?>(null)

    fun selectProduct(product: Product) {
        selectedProduct = product
    }

    fun searchFood(query: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitInstance.api.searchFood(
                    searchTerms = query,
                    tagType = "countries",
                    tagContains = "contains",
                    tag = "Canada"
                )
                Log.d("API Response", response.toString())

                // Check for caloric information and filter out French names
                foodItems = response.products?.filter { product ->
                    val calories = product.nutriments?.energy_kcal_100g

                    // Check for French names using regex or keywords
                    val productName = product.product_name ?: ""
                    val isFrenchName = productName.contains(Regex("[éèêçàùûîô]")) ||
                            listOf("de", "le", "la", "du").any { productName.contains(it, ignoreCase = true) }

                    Log.d("Product Filter", "Product: $productName, Calories: $calories, Is French: $isFrenchName")

                    // Include only products with calories and non-French names
                    calories != null && !isFrenchName
                } ?: emptyList()
            } catch (e: Exception) {
                Log.e("FoodSearchViewModel", "Error fetching products: ${e.message}")
                foodItems = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    suspend fun getProductDetailsForNutritionHistoryScreen(productID: String): Product? {
        isLoading = true
        return try {
            // Fetch product details using productID
            val response = RetrofitInstance.api.getProductDetails(productID)

            if (response.product != null) {
                Log.d("Product Details", "Fetched Product: ${response.product}")
                response.product
            } else {
                Log.w("Product Details", "No product found for ID: $productID")
                null
            }
        } catch (e: Exception) {
            Log.e("FoodSearchViewModel", "Error fetching product by ID: ${e.message}")
            null
        } finally {
            isLoading = false
        }
    }
}