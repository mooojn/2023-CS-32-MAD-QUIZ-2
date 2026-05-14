package com.example.mad_eval.data.model

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("totalArticles")
    val totalArticles: Int,
    @SerializedName("articles")
    val articles: List<Article>
)
