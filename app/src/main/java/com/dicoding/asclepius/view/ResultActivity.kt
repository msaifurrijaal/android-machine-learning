package com.dicoding.asclepius.view

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

        val classifications = intent.getSerializableExtra(EXTRA_CLASSIFICATIONS) as? String
        classifications?.let { showResults(it) }
    }

    private fun showResults(result: String) {

        binding.resultText.text = result
    }

    companion object {
        const val EXTRA_CLASSIFICATIONS = "extra_classifications"
    }


}