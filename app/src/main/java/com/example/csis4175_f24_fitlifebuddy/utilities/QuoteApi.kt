package com.example.csis4175_f24_fitlifebuddy.utilities

import retrofit2.http.GET


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query
import retrofit2.http.Header

data class Quote(
    val quote: String,
    val author: String
)

interface QuoteApiService {
    @GET("quotes")
    suspend fun getQuotes(
        @Query("category") category: String, // Specify the category here
        @Header("X-Api-Key") apiKey: String // Pass the API key as a header
    ): List<Quote>
}

object ApiClient {
    private const val BASE_URL = "https://api.api-ninjas.com/v1/"

    val quoteApiService: QuoteApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuoteApiService::class.java)
    }
}