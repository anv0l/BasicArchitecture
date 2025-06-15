package ru.otus.basicarchitecture.ui.address

import kotlinx.serialization.Serializable
import ru.otus.basicarchitecture.WizardAddress

@Serializable
data class SuggestionResponse(
    val suggestions: List<Suggestion>
)

@Serializable
data class Suggestion(
    val value: String,
    val data: AddressData
) {
    fun toWizardAddress(): WizardAddress {
        return WizardAddress(
            country = this.data.country ?: "",
            city = this.data.city ?: "",
            address = this.value,
            value = this.value,
            house = this.data.house ?: "",
            street = this.data.street ?: ""
        )
    }
}

@Serializable
data class AddressData(
    val city: String? = "",
    val street: String? = "",
    val house: String? = "",
    val country: String? = ""
)

@Serializable
data class SuggestQuery(
    val query: String
)