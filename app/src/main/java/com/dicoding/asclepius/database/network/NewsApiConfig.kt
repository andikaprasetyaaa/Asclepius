package com.dicoding.asclepius.database.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewsApiConfig {

    fun getNewsApiService(): NewsApiService {
        return Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }
}