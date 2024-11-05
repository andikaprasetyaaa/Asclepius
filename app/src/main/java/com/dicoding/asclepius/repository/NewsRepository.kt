package com.dicoding.asclepius.repository

import com.dicoding.asclepius.database.network.NewsApiConfig
import com.dicoding.asclepius.database.network.NewsResponse

class NewsRepository(private val apiKey: String) {

    private val api = NewsApiConfig.getNewsApiService()

    suspend fun getNews(): NewsResponse? {
        val response = api.getNews(apiKey = apiKey)

        return if (response.isSuccessful) {
            val filteredArticles = response.body()?.articles?.filter { article ->
                article.source?.name != "[Removed]" && article.urlToImage != null
            }
            val filteredResponse = response.body()?.copy(articles = filteredArticles ?: emptyList())
            filteredResponse
        } else {
            null
        }
    }
}