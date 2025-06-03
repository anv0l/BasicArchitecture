package ru.otus.basicarchitecture.ui.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.otus.basicarchitecture.WizardAddress
import ru.otus.basicarchitecture.WizardCache
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(private val wizardCache: WizardCache) : ViewModel() {
    private var _address = MutableStateFlow<WizardAddress?>(null)
    val address = _address.asStateFlow()

    val nextAvailable = _address.map { value ->
        value?.let {
            it.address.isNotBlank() && it.city.isNotBlank() && it.country.isNotBlank()
        } ?: false
    }.stateIn(
        scope = viewModelScope,
        initialValue = false,
        started = SharingStarted.WhileSubscribed(5000)
    )

    fun setAddress(newAddress: WizardAddress) {
        _address.value = newAddress
    }

    fun initAddress() {
        _address.value = wizardCache.getAddress()
    }

    fun saveToWizardCache() {
        _address.value?.let { wizardCache.setNewAddress(it) }
    }
}