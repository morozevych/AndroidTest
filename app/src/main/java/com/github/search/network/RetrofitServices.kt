package com.github.search.network

import com.github.search.model.dto.SearchResponseDTO

import retrofit2.Call
import retrofit2.http.*

interface RetrofitServices {
    @GET("search/repositories")
    fun searchRepositories(
        @Query("q") searchTerm: String?,
        @Query("per_page") perPage: Int?
    ): Call<SearchResponseDTO>
}