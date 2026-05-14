package com.example.mad_eval.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mad_eval.data.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _headlines = MutableLiveData<NewsRepository.Result>()
    val headlines: LiveData<NewsRepository.Result> = _headlines

    val selectedCountry: MutableLiveData<String> = MutableLiveData("us")

    init {
        fetchNews(selectedCountry.value ?: "us")
    }

    fun fetchNews(country: String) {
        selectedCountry.value = country
        _headlines.value = NewsRepository.Result.Loading

        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.fetchHeadlines(country)
            withContext(Dispatchers.Main) {
                _headlines.value = result
            }
        }
    }

    fun refresh() {
        fetchNews(selectedCountry.value ?: "us")
    }
}
