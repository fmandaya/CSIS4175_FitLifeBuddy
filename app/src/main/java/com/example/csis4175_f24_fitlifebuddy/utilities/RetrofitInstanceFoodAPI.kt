package com.example.csis4175_f24_fitlifebuddy.utilities

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstanceFoodAPI {
    private const val BASE_URL = "https://world.openfoodfacts.org/"

    private val client = OkHttpClient.Builder()
        .followRedirects(true) // Enable automatic handling of redirects
        .followSslRedirects(true) // Handle HTTPS redirects
        .build()

    val api: OpenFoodFactsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(OpenFoodFactsApi::class.java)
    }
}