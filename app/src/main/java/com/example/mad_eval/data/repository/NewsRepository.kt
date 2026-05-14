package com.example.mad_eval.data.repository

import android.util.Log
import com.example.mad_eval.Constants
import com.example.mad_eval.data.model.Article
import com.example.mad_eval.data.remote.ApiService

class NewsRepository(private val apiService: ApiService) {

    sealed class Result {
        data object Loading : Result()
        data class Success(val articles: List<Article>) : Result()
        data class Error(val message: String) : Result()
    }

    suspend fun fetchHeadlines(country: String): Result {
        return try {
            val response = apiService.getTopHeadlines(
                country = country,
                apiKey = Constants.API_KEY
            )
            Result.Success(response.articles)
        } catch (exception: Exception) {
            Log.e("NewsRepository", "Failed to fetch headlines", exception)
            Result.Error(exception.localizedMessage ?: "Something went wrong")
        }
    }
}
