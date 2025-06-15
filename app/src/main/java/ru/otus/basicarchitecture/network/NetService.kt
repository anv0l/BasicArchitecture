package ru.otus.basicarchitecture.network

import ru.otus.basicarchitecture.ui.address.SuggestionResponse
import ru.otus.basicarchitecture.ui.address.SuggestQuery
import javax.inject.Inject

interface NetService {
    suspend fun getSuggestion(searchString: String): SuggestionResponse

    class Impl @Inject constructor(private val suggestion: GetSuggestions) : NetService {
        override suspend fun getSuggestion(searchString: String): SuggestionResponse {
            return suggestion(SuggestQuery(searchString))
        }
    }
}