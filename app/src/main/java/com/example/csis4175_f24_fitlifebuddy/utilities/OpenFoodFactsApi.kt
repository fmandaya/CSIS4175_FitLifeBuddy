package com.example.csis4175_f24_fitlifebuddy.utilities

import com.example.csis4175_f24_fitlifebuddy.utilities.model.FoodResponse
import com.example.csis4175_f24_fitlifebuddy.utilities.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface OpenFoodFactsApi {
    @GET("api/v0/product/{barcode}.json")
    suspend fun getProductByBarcode(@Path("barcode") barcode: String): FoodResponse

    @GET("cgi/search.pl")
    suspend fun searchFood(
        @Query("search_terms") searchTerms: String,
        @Query("tagtype_0") tagType: String = "countries",
        @Query("tag_contains_0") tagContains: String = "contains",
        @Query("tag_0") tag: String = "Canada",
        @Query("json") json: Int = 1
    ): SearchResponse
}

