package ru.otus.basicarchitecture

import javax.inject.Inject

interface SessionManager {
    fun getToken(): String

    class Impl @Inject constructor() : SessionManager {
        override fun getToken(): String {
            return BuildConfig.apiKey
        }
    }
}