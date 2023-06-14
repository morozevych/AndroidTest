package com.github.search.model.dto

data class RepositoryDTO (
    val id: Long,
    val name: String,
    val description: String?,
    val url: String,
    val language: String?
)