package ru.otus.basicarchitecture.ui.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.otus.basicarchitecture.WizardAddress
import ru.otus.basicarchitecture.WizardCache
import ru.otus.basicarchitecture.network.Api
import ru.otus.basicarchitecture.network.AuthInterceptor
import ru.otus.basicarchitecture.network.Debouncer
import ru.otus.basicarchitecture.network.GetSuggestions
import ru.otus.basicarchitecture.network.NetService
import ru.otus.basicarchitecture.network.buildRetrofit
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val wizardCache: WizardCache,
    private val service: NetService,
    private val debouncer: Debouncer
) : ViewModel() {
    private var _address = MutableStateFlow<WizardAddress?>(null)
    val address = _address.asStateFlow()

    private val _addressSuggestions = MutableStateFlow<List<WizardAddress>>(emptyList())
    val addressSuggestions = _addressSuggestions.asStateFlow()

    private val _state = MutableStateFlow<State>(State.Ready)
    val state = _state.asStateFlow()

    val nextAvailable = _address.map { value ->
        value?.let {
            it.address.isNotBlank() && it.city.isNotBlank() && it.country.isNotBlank()
        } ?: false
    }.stateIn(
        scope = viewModelScope,
        initialValue = false,
        started = SharingStarted.WhileSubscribed(5000)
    )

    private var suggestionJob: Job? = null

    fun getSuggestion(searchString: String) {
        suggestionJob?.cancel()
        _state.value = State.Ready

        if (searchString.length < 3) return

        suggestionJob = CoroutineScope(Dispatchers.IO).launch {
            debouncer.debounce()
            _state.value = State.Loading
            kotlin.runCatching {
                service.getSuggestion(searchString).suggestions.map { it.toWizardAddress() }
            }.onSuccess { res -> _addressSuggestions.value = res }

            _state.value = State.Ready
        }
    }

    fun setStateReady() {
        _state.value = State.Ready
    }

    fun setStateDataIsSet() {
        _state.value = State.DataIsSet
    }


    fun setAddress(newAddress: WizardAddress) {
        _state.value = State.DataIsSet
        _addressSuggestions.value = emptyList()
        _address.value = newAddress
    }

    fun initAddress() {
        _address.value = wizardCache.getAddress()
    }

    fun saveToWizardCache() {
        _address.value?.let { wizardCache.setNewAddress(it) }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class MainModule {
    @Binds
    abstract fun netService(impl: NetService.Impl): NetService

    @Binds
    abstract fun getSuggestion(impl: GetSuggestions.Impl): GetSuggestions
}

@Module
@InstallIn(ViewModelComponent::class)
class MainModuleProvider {
    @Provides
    fun okHttp(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .callTimeout(30L, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BASIC)
            })
            .build()
    }

    @Provides
    fun retrofit(okhttp: OkHttpClient): Retrofit {
        return buildRetrofit(okhttp)
    }

    @Provides
    fun api(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }
}

sealed class State {
    data object Loading : State()
    data object Ready : State()
    data object DataIsSet : State()
}