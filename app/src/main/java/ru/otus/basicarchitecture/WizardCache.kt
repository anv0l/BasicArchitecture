package ru.otus.basicarchitecture

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.time.LocalDate

data class WizardUser(
    var name: String,
    var lastname: String,
    var birthday: LocalDate
)

data class WizardAddress(
    var country: String,
    var city: String,
    var address: String
)

fun WizardAddress.toText(): String {
    return "${if (country != "") "$country, " else ""}${if (city != "") "$city, " else ""}$address"
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object WizardModule {
    @Provides
    @ActivityRetainedScoped
    fun wizardCache(): WizardCache = WizardCache()
}

class WizardCache {
    private lateinit var user: WizardUser
    private lateinit var address: WizardAddress

    private lateinit var hobbies: List<String>

    fun setNewUser(newUser: WizardUser) {
        user = newUser
    }

    fun setNewAddress(newAddress: WizardAddress) {
        address = newAddress
    }

    fun setHobbies(newHobbies: List<String>) {
        hobbies = newHobbies
    }

    fun getUser(): WizardUser {
        return user
    }

    fun getAddress(): WizardAddress {
        return address
    }

    fun getHobbies(): List<String> {
        return hobbies
    }
}