package com.github.search.model.dto

import com.google.gson.annotations.SerializedName

data class SearchResponseDTO (
    @SerializedName("total_count")
    val totalCount: Int,
    val items: List<RepositoryDTO>?
)