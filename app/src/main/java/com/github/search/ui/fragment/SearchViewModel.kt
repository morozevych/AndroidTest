package com.github.search.ui.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.search.model.LanguageCount
import com.github.search.model.LanguageCountResult
import com.github.search.model.dto.RepositoryDTO
import com.github.search.network.ApiCalls
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel : ViewModel() {
    private val perPage = 1000

    val searchText = MutableLiveData<String>().apply { value = "" } // MutableLiveData to store the search text
    val isButtonEnabled = MutableLiveData<Boolean>().apply { value = false } // MutableLiveData to store the enabled state of the button
    val searchError = MutableLiveData<String?>().apply { value = null } // MutableLiveData to store the search error message
    val languageCountResult = MutableLiveData<LanguageCountResult?>().apply { value = null } // MutableLiveData to store the result of language count
    val isLoading = MutableLiveData<Boolean>().apply { value = false } // MutableLiveData to store the loading state

    private var searchJob: Job? = null // Coroutine job to manage the search operation

    init {
        // Observe changes in searchText and update the button enabled state accordingly
        searchText.observeForever { searchValue ->
            isButtonEnabled.value = !searchValue.isNullOrEmpty()
            searchJob?.cancel() // Cancel any previous search job if it's still active
        }
    }

    fun search() {
        searchJob?.cancel() // Cancel any previous search job if it's still active
        searchJob = viewModelScope.launch {
            isLoading.value = true // Update the loading state to indicate that the search is in progress
            try {
                val searchTerm = searchText.value.orEmpty()

                // Make an API call to search repositories based on the searchTerm and handle the response
                val (searchResult, error) = ApiCalls.searchRepositories(searchTerm, perPage)
                val repositories = searchResult?.items

                val filteredRepositories = withContext(Dispatchers.IO) {
                    repositories?.let { filter(it, searchTerm) } ?: emptyList()
                }

                languageCountResult.value = LanguageCountResult(filteredRepositories, searchResult?.totalCount ?: 0)
                searchError.value = error?.message
            } finally {
                isLoading.value = false // Update the loading state to indicate that the search has completed
            }
        }
    }

    private fun filter(repositories: List<RepositoryDTO>, searchTerm: String): List<LanguageCount> {
        // Step 1: Filter repositories based on the search term in the description
        val filteredRepositories = repositories.filter { repository ->
            repository.description?.contains(searchTerm, ignoreCase = true) == true
        }

        // Step 2: Filter out repositories with an empty language
        val nonEmptyLanguageRepositories = filteredRepositories.filter { repository ->
            !repository.language.isNullOrEmpty()
        }

        // Step 3: Group repositories by language and count the occurrences for each language
        val languageCounts = nonEmptyLanguageRepositories.groupBy { repository ->
            repository.language
        }.mapValues { (_, repositories) ->
            repositories.size
        }

        // Step 4: Convert the language counts into a list of LanguageCount objects
        val languageCountList = languageCounts.map { (language, count) ->
            LanguageCount(language, count)
        }

        // Step 5: Sort the languages by the occurrence count in descending order
        return languageCountList.sortedByDescending { languageCount ->
            languageCount.count
        }
    }
}
