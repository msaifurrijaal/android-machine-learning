package com.dicoding.asclepius.ui.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding
import org.tensorflow.lite.task.vision.classifier.Classifications

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val classifications = intent.getStringExtra(EXTRA_CLASSIFICATIONS)
        val image = intent.getStringExtra(EXTRA_IMAGE)

        if (classifications != null && image != null) {
            showResults(classifications, image)
        }

    }

    private fun showResults(result: String, image: String) {
        val imageUri = Uri.parse(image)
        binding.resultImage.setImageURI(imageUri)
        binding.resultText.text = result
    }

    companion object {
        const val EXTRA_CLASSIFICATIONS = "extra_classifications"
        const val EXTRA_IMAGE = "extra_image"

    }


}