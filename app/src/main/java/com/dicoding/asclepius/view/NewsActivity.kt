package com.dicoding.asclepius.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.ActivityNewsBinding
import com.dicoding.asclepius.adapter.NewsAdapter
import com.dicoding.asclepius.repository.NewsRepository
import com.dicoding.asclepius.viewmodel.NewsViewModel
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.R
import com.dicoding.asclepius.factory.NewsViewModelFactory

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    private val viewModel: NewsViewModel by viewModels {
        NewsViewModelFactory(NewsRepository(BuildConfig.NEWS_API_KEY))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObserver()
        setupLoadingIndicator()

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        binding.newsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.newsRecyclerView.adapter = NewsAdapter(emptyList())
    }

    private fun setupObserver() {
        viewModel.news.observe(this) { articles ->
            binding.newsRecyclerView.adapter = NewsAdapter(articles)
            binding.progressBarNews.visibility = View.GONE
        }

        viewModel.error.observe(this) { errorMessage ->
            when (errorMessage) {
                "fetch_failed" -> Toast.makeText(
                    this,
                    getString(R.string.toast_fetch_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupLoadingIndicator() {
        viewModel.loading.observe(this) { isLoading ->
            binding.progressBarNews.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}