package com.dicoding.asclepius.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.local.ResultDataDB
import com.dicoding.asclepius.data.model.ResponseNews
import com.dicoding.asclepius.data.model.ResultData
import com.dicoding.asclepius.data.remote.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(private val application: Application) {

    private val apiService = RetrofitInstance.getApiService()
    private val resultDataDB: ResultDataDB = ResultDataDB.getInstance(application)
    private val dao = resultDataDB.resultDao()

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

    suspend fun getDataResults(): LiveData<Resource<List<ResultData>>> = withContext(Dispatchers.IO) {
        val listResultData = MutableLiveData<Resource<List<ResultData>>>()
        listResultData.postValue(Resource.Loading())

        if (dao.getAll().isNullOrEmpty()) {
            listResultData.postValue(Resource.Error("Data Kosong"))
        } else {
            listResultData.postValue(Resource.Success(dao.getAll()))
        }
        return@withContext listResultData
    }

    fun addResultData(data: ResultData) = dao.insert(data)
}