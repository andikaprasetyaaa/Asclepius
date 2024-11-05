package com.dicoding.asclepius.database.network

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)