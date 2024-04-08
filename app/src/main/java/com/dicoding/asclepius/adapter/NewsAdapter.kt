package com.dicoding.asclepius.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.model.ArticlesItem
import com.dicoding.asclepius.databinding.LayoutItemNewsBinding
import kotlin.random.Random

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    private var listNews = ArrayList<ArticlesItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setNews(newList: List<ArticlesItem>) {
        listNews.clear()
        listNews.addAll(newList)
        notifyDataSetChanged()
    }
    class ViewHolder(var binding: LayoutItemNewsBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listNews.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = listNews[position]
        val randomInt = Random.nextInt(70, 81)
        holder.binding.apply {
            tvTitleNews.text = news.title
            tvAuthor.text = news.author
            Glide.with(holder.itemView.context)
                .load("https://picsum.photos/id/$randomInt/300/200")
                .into(ivNews)
        }

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(news) }
    }

    interface OnItemClickCallback {
        fun onItemClicked(news: ArticlesItem)
    }
}