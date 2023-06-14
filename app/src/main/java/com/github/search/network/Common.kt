package com.github.search.network

object Common {
    private const val baseUrl = "https://api.github.com/"

    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(baseUrl).create(RetrofitServices::class.java)
}