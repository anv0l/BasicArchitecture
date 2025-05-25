package ru.otus.basicarchitecture.ui.address

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.otus.basicarchitecture.WizardAddress
import ru.otus.basicarchitecture.WizardCache
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(private val wizardCache: WizardCache) : ViewModel() {
    fun setAddress(newAddress: WizardAddress) {
        wizardCache.setNewAddress(newAddress)
    }
}