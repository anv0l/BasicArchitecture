package ru.otus.basicarchitecture.network

interface Debouncer {
    suspend fun debounce()
}