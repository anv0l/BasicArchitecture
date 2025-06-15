package ru.otus.basicarchitecture.network

import okio.IOException
import ru.otus.basicarchitecture.ui.address.SuggestionResponse
import ru.otus.basicarchitecture.ui.address.SuggestQuery
import javax.inject.Inject

interface GetSuggestions {

    suspend operator fun invoke(searchString: SuggestQuery): SuggestionResponse

    class Impl @Inject constructor(private val api: Api) :
        GetSuggestions {
        override suspend fun invoke(searchString: SuggestQuery): SuggestionResponse {
            val response = api.getSuggestions(searchString)
            if (response.isSuccessful) {
                return response.body() ?: throw IOException("Empty body $response")
            } else
                throw IOException("Unexpected code: $response")
        }
    }
}