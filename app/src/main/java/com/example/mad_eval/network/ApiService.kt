package com.example.mad_eval.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// 1. Define your Data Model
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

// 2. Define the API Interface
interface ApiService {
    @GET("posts/1")
    suspend fun getPost(): Post
}

// 3. Create the Retrofit Client
object RetrofitClient {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
