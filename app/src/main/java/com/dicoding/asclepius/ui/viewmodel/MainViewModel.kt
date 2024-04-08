package com.dicoding.asclepius.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.Repository
import com.dicoding.asclepius.data.Resource
import com.dicoding.asclepius.data.model.ResponseNews
import com.dicoding.asclepius.data.model.ResultData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(application)

    private val _dataNews = MutableLiveData<Resource<ResponseNews>>()
    val dataNews: LiveData<Resource<ResponseNews>> get() = _dataNews

    init {
        getDataNews()
    }

    fun getDataNews() {
        if (_dataNews.value !is Resource.Success && _dataNews.value !is Resource.Loading && _dataNews.value !is Resource.Error) {
            _dataNews.value = Resource.Loading()
            repository.getNews() { resource ->
                _dataNews.value = resource
            }
        }
    }

    fun addResultData(data: ResultData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addResultData(data)
        }
    }
}