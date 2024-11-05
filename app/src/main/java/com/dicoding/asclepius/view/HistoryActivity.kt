package com.dicoding.asclepius.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.database.local.DetectionHistory
import com.dicoding.asclepius.adapter.HistoryAdapter
import com.dicoding.asclepius.viewmodel.HistoryViewModel
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.google.android.material.snackbar.Snackbar

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val historyViewModel: HistoryViewModel by viewModels()
    private lateinit var adapter: HistoryAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = HistoryAdapter(listOf()) { history ->
            showDeleteConfirmationDialog(history)
        }

        binding.historyRecyclerView.adapter = adapter
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)

        historyViewModel.allHistory.observe(this) { histories ->
            adapter.historyList = histories
            adapter.notifyDataSetChanged()
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun showDeleteConfirmationDialog(history: DetectionHistory) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(R.string.delete_confirmation_title)
            .setMessage(R.string.delete_confirmation_message)
            .setPositiveButton(R.string.delete_confirmation_positive) { _, _ ->
                historyViewModel.delete(history)
                Snackbar.make(
                    binding.root,
                    getString(R.string.toast_delete_success),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton(R.string.delete_confirmation_negative) { dialog, _ ->
                dialog.dismiss()
                Snackbar.make(
                    binding.root,
                    getString(R.string.toast_delete_cancelled),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            .create()
            .show()
    }
}