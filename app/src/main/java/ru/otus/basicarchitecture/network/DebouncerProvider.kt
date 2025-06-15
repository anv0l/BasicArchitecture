package ru.otus.basicarchitecture.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.delay

@Module
@InstallIn(ViewModelComponent::class)
object DebouncerProvider {
    private const val debouncePeriod = 500L

    class DebouncerImpl : Debouncer {
        override suspend fun debounce() {
            delay(debouncePeriod)
        }
    }

    @Provides
    fun provideDebouncer(): Debouncer {
        return DebouncerImpl()
    }
}