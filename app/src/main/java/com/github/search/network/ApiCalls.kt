package com.github.search.network

import com.github.search.model.dto.SearchResponseDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Error

object ApiCalls {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun searchRepositories(searchTerm: String, perPage: Int): Pair<SearchResponseDTO?, Error?> =
        suspendCancellableCoroutine { continuation ->
            val call = Common.retrofitService.searchRepositories(searchTerm, perPage)
            val callback = object : Callback<SearchResponseDTO> {
                override fun onResponse(call: Call<SearchResponseDTO>, response: Response<SearchResponseDTO>) {
                    if (response.isSuccessful) {
                        val searchResponse = response.body()
                        continuation.resume(Pair(searchResponse, null), null)
                    } else {
                        val errorBodyString = response.errorBody()?.string()
                        continuation.resume(Pair(null, Error(errorBodyString)), null)
                    }
                }

                override fun onFailure(call: Call<SearchResponseDTO>, t: Throwable) {
                    if (continuation.isCancelled) {
                        continuation.resume(Pair(null, null), null)
                    } else {
                        continuation.resume(Pair(null, Error(t.message)), null)
                    }
                }
            }

            call.enqueue(callback)

            continuation.invokeOnCancellation {
                call.cancel()
            }
        }
}