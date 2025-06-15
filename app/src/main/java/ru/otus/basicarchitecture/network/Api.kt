package ru.otus.basicarchitecture.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import ru.otus.basicarchitecture.ui.address.SuggestionResponse
import ru.otus.basicarchitecture.ui.address.SuggestQuery


private const val suggestionUrl =
    "https://suggestions.dadata.ru/suggestions/api/4_1/rs/suggest/"

interface Api {
    @POST("address")
    suspend fun getSuggestions(@Body query: SuggestQuery): Response<SuggestionResponse>
}

fun buildRetrofit(okHttpClient: OkHttpClient): Retrofit {
    val json = Json { ignoreUnknownKeys = true }
    return Retrofit.Builder()
        .baseUrl(suggestionUrl)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}