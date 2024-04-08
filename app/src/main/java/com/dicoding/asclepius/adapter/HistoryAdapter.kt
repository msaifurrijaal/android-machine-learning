package com.dicoding.asclepius.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.data.model.ResultData
import com.dicoding.asclepius.databinding.LayoutItemNewsBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var listResultData = ArrayList<ResultData>()

    @SuppressLint("NotifyDataSetChanged")
    fun setResultData(newList: List<ResultData>) {
        listResultData.clear()
        listResultData.addAll(newList)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: LayoutItemNewsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listResultData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resultData = listResultData[position]

        holder.binding.apply {
            tvTitleNews.text = resultData.result
            tvAuthor.text = resultData.date
            ivNews.setImageURI(Uri.parse(resultData.image))
        }
    }


}