package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.asclepius.R
import com.dicoding.asclepius.database.local.DetectionHistory
import com.dicoding.asclepius.viewmodel.HistoryViewModel
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.util.Locale

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var lastSavedResult: DetectionHistory

    companion object {
        const val IMAGE_URI = "img_uri"
        const val TAG = "imagePicker"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra(IMAGE_URI)
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            displayImage(imageUri)

            val imageClassifierHelper = ImageClassifierHelper(
                contextValue = this,
                classifierListenerValue = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(errorMsg: String) {
                        Log.d(TAG, "Error: $errorMsg")
                        showToast(getString(R.string.classification_process_error))
                    }

                    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                        results?.let {
                            displayResult(it, imageUri)
                        } ?: run {
                            showToast(getString(R.string.classification_process_error))
                        }
                    }
                }
            )
            imageClassifierHelper.classifyImage(imageUri)
        } else {
            Log.e(TAG, "No image URI provided")
            finish()
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.shareButton.setOnClickListener {
            shareResult()
        }

        binding.saveButton.setOnClickListener {
            saveResult()
        }
    }

    private fun displayImage(uri: Uri) {
        binding.resultImage.setImageURI(uri)
    }

    private fun displayResult(results: List<Classifications>, imageUri: Uri) {
        val topResult = results[0]
        val label = topResult.categories[0].label
        val score = topResult.categories[0].score

        fun Float.formatToString(): String {
            return String.format(Locale.getDefault(), "%.2f%%", this * 100)
        }

        val resultText = "$label ${score.formatToString()}"
        binding.resultText.text = resultText

        lastSavedResult = DetectionHistory(
            title = label,
            imagePath = imageUri.toString(),
            date = System.currentTimeMillis().toString(),
            cancerInfo = resultText
        )
    }

    private fun saveResult() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.insert(lastSavedResult)
            withContext(Dispatchers.Main) {
                showToast(getString(R.string.toast_save_success))
            }
        }
    }

    private fun shareResult() {
        val shareText = "Result: ${lastSavedResult.title} - ${lastSavedResult.cancerInfo}"
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}