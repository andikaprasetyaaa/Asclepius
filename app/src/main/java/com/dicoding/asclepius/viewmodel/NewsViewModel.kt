package com.dicoding.asclepius.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.database.network.Article
import com.dicoding.asclepius.repository.NewsRepository
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {
    val news = MutableLiveData<List<Article>>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    init {
        fetchNews()
    }

    private fun fetchNews() {
        loading.value = true
        viewModelScope.launch {
            try {
                val newsResponse = repository.getNews()
                news.value = newsResponse?.articles ?: emptyList()
            } catch (e: Exception) {
                error.value = "fetch_failed"
            } finally {
                loading.value = false
            }
        }
    }
}