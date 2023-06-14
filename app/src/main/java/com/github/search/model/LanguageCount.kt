package com.github.search.model

// Class to represent language count
data class LanguageCount(val language: String?,
                         val count: Int) {
    fun formattedString(): String {
        return "${language}: $count"
    }
}
