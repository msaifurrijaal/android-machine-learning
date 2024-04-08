package com.dicoding.asclepius.ui.view

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.NewsAdapter
import com.dicoding.asclepius.data.Resource
import com.dicoding.asclepius.data.model.ArticlesItem
import com.dicoding.asclepius.data.model.ResponseNews
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.ui.viewmodel.MainViewModel
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentImageUri: Uri? = null
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        observeDataNews()
        onAction()
    }

    private fun observeDataNews() {
        mainViewModel.dataNews.observe(this) { resources ->
            when (resources) {
                is Resource.Error -> {
                    showError(resources.message)
                }
                is Resource.Loading -> {
                    showLoading()
                }
                is Resource.Success -> {
                    Log.d("MainActivity", "data news : ${resources.data?.articles?.get(0)?.author}")
                    showDataNews(resources.data)
                }

                else -> {
                    showError(getString(R.string.gagal_memuat_berita))
                }
            }
        }
    }

    private fun showError(message: String?) {
        binding.apply {
            pgNews.visibility = View.GONE
            rvNews.visibility = View.GONE
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading() {
        binding.apply {
            rvNews.visibility = View.GONE
            pgNews.visibility = View.VISIBLE
        }
    }


    private fun showDataNews(data: ResponseNews?) {
        data?.let {
            val newsAdapter = NewsAdapter()
            newsAdapter.setNews(data.articles!!.take(10) as ArrayList<ArticlesItem>)
            binding.rvNews.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = newsAdapter
                setHasFixedSize(true)
            }

            newsAdapter.setOnItemClickCallback(object : NewsAdapter.OnItemClickCallback{
                override fun onItemClicked(news: ArticlesItem) {
                    val newsUrl = news.url
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(newsUrl))
                    startActivity(browserIntent)
                }
            })
            binding.apply {
                pgNews.visibility = View.GONE
                rvNews.visibility = View.VISIBLE
            }
        }
    }

    private fun onAction() {
        binding.apply {
            galleryButton.setOnClickListener {
                startGallery()
            }

            analyzeButton.setOnClickListener {
                analyzeImage()
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        currentImageUri?.let { uri ->
            binding.apply {
                progressIndicator.visibility = View.VISIBLE
                val imageClassifierHelper = ImageClassifierHelper(
                    context = this@MainActivity,
                    classifierListener = object : ImageClassifierHelper.ClassifierListener {
                        override fun onError(error: String) {
                            showToast(error)
                            progressIndicator.visibility = View.GONE
                        }

                        override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                            results?.let { it ->
                                if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                    println(it)
                                    val sortedCategories =
                                        it[0].categories.sortedByDescending { it?.score }
                                    val displayResult =
                                        sortedCategories.joinToString("\n") {
                                            "${it.label} " + NumberFormat.getPercentInstance()
                                                .format(it.score).trim()
                                        }
                                    moveToResult(displayResult)
                                }
                            }
                            progressIndicator.visibility = View.GONE
                        }
                    }
                )
                imageClassifierHelper.classifyStaticImage(uri)
            }

        } ?: showToast("No image selected")
    }

    private fun moveToResult(results: String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_CLASSIFICATIONS, results)
        intent.putExtra(ResultActivity.EXTRA_IMAGE, currentImageUri.toString())
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}