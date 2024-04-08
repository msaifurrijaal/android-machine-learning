package com.dicoding.asclepius.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.HistoryAdapter
import com.dicoding.asclepius.data.Resource
import com.dicoding.asclepius.data.model.ResultData
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.ui.viewmodel.HistoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarHistory)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        historyViewModel = ViewModelProvider(this)[HistoryViewModel::class.java]

        observeDataHistory()
        onAction()
    }

    private fun onAction() {
        binding.apply {
            ibBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun observeDataHistory() {
        lifecycleScope.launch(Dispatchers.Main) {
            historyViewModel.getAllResultData().observe(this@HistoryActivity) { resources ->
                when (resources) {
                    is Resource.Error -> {
                        showError()
                    }

                    is Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Success -> {
                        Log.d("HistoryActivity", resources.data?.get(0)?.date ?: "gada")
                        showDataHistory(resources.data)
                    }
                }
            }
        }
    }

    private fun showDataHistory(data: List<ResultData>?) {
        data?.let {
            val historyAdapter = HistoryAdapter()
            historyAdapter.setResultData(data.reversed())
            binding.rvHistory.apply {
                layoutManager = LinearLayoutManager(this@HistoryActivity)
                adapter = historyAdapter
                setHasFixedSize(true)
            }

            binding.apply {
                pgHistory.visibility = View.GONE
                containerEmpty.visibility = View.GONE
                rvHistory.visibility = View.VISIBLE
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            rvHistory.visibility = View.GONE
            containerEmpty.visibility = View.GONE
            pgHistory.visibility = View.VISIBLE
        }
    }

    private fun showError() {
        binding.apply {
            rvHistory.visibility = View.GONE
            pgHistory.visibility = View.GONE
            containerEmpty.visibility = View.VISIBLE
        }
    }
}