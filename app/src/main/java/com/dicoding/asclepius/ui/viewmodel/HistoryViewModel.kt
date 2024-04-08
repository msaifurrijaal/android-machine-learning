package com.dicoding.asclepius.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.Repository
import com.dicoding.asclepius.data.Resource
import com.dicoding.asclepius.data.model.ResultData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel(application: Application): AndroidViewModel(application) {

    private val repository = Repository(application)

    suspend fun getAllResultData(): LiveData<Resource<List<ResultData>>> = withContext(
        Dispatchers.IO
    ) {
        var listResultData: LiveData<Resource<List<ResultData>>>? = null
        viewModelScope.launch {
            listResultData = repository.getDataResults()
        }.join()
        return@withContext listResultData!!
    }
}