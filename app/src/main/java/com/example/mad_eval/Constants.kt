package com.example.mad_eval

object Constants {
    const val API_KEY = "90ea804e1d7b4ff3bafe873032d8386c"
    const val BASE_URL = "https://gnews.io/api/v4/"

    val SUPPORTED_COUNTRIES: LinkedHashMap<String, String> = linkedMapOf(
        "Pakistan" to "pk",
        "US" to "us",
        "UK" to "gb",
        "India" to "in",
        "Saudi Arabia" to "sa",
        "UAE" to "ae"
    )
}
