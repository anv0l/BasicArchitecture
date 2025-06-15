package ru.otus.basicarchitecture.network

import okhttp3.Interceptor
import okhttp3.Response
import ru.otus.basicarchitecture.SessionManager
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val sessionManager: SessionManager) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestWithToken = request.newBuilder()
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("Authorization", "Token ${sessionManager.getToken()}")
            .build()

        return chain.proceed(requestWithToken)
    }
}