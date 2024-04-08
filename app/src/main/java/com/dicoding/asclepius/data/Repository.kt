package com.dicoding.asclepius.data

import android.app.Application
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.model.ResponseNews
import com.dicoding.asclepius.data.remote.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(private val application: Application) {

    private val apiService = RetrofitInstance.getApiService()

    fun getNews(callback: (Resource<ResponseNews>) -> Unit) {
        apiService.getNews(
            country = "id",
            category = "health",
            apiKey = BuildConfig.API_KEY
        ).enqueue(object :
            Callback<ResponseNews> {
            override fun onResponse(call: Call<ResponseNews>, response: Response<ResponseNews>) {
                if (response.isSuccessful) {
                    callback(Resource.Success(response.body()))
                } else {
                    callback(Resource.Error("Failed to fetch users"))
                }
            }

            override fun onFailure(call: Call<ResponseNews>, t: Throwable) {
                callback(Resource.Error(t.message))
            }
        })
    }
}