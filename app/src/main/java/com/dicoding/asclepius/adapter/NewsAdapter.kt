package com.dicoding.asclepius.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.database.network.Article
import com.dicoding.asclepius.databinding.ItemNewsBinding

class NewsAdapter(private val articles: List<Article>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size

    inner class NewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.tvNewsName.text = article.source?.name
            binding.tvNewsAuthor.text = article.author
            binding.tvPublishAt.text = article.publishedAt
            binding.tvNewsTitle.text = article.title
            binding.tvNewsDescription.text = article.description

            Glide.with(binding.root.context)
                .load(article.urlToImage)
                .into(binding.ivNewsImage)

            binding.fabOpenBrowser.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                binding.root.context.startActivity(intent)
            }
        }
    }
}