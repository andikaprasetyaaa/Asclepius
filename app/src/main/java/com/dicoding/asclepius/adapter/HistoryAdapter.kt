package com.dicoding.asclepius.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.database.local.DetectionHistory
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(
    var historyList: List<DetectionHistory>,
    private val onDeleteClick: (DetectionHistory) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(history: DetectionHistory) {
            binding.apply {
                tvDetectionTitle.text = history.title
                tvDetectionDate.text = formatDate(history.date)
                tvCancerInfo.text = history.cancerInfo

                Glide.with(root.context)
                    .load(history.imagePath)
                    .into(ivDetectionImage)

                fabDelete.setOnClickListener {
                    onDeleteClick(history)
                }
            }
        }

        private fun formatDate(dateString: String): String {
            return try {
                val timestamp = dateString.toLong()
                val date = Date(timestamp)
                val formatter = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
                formatter.format(date)
            } catch (e: Exception) {
                dateString
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int = historyList.size
}