package ru.otus.basicarchitecture

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.time.LocalDate

data class WizardUser(
    val name: String,
    val lastname: String,
    val birthday: LocalDate
)

data class WizardAddress(
    val country: String,
    val city: String,
    val address: String
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
    private var user: WizardUser = WizardUser("", "", LocalDate.now())
    private var address: WizardAddress = WizardAddress("", "", "")

    private var hobbies: Set<String> = emptySet()

    fun setNewUser(newUser: WizardUser) {
        user = newUser
    }

    fun setNewAddress(newAddress: WizardAddress) {
        address = newAddress
    }

    fun setHobbies(newHobbies: Set<String>) {
        hobbies = newHobbies
    }

    fun getUser(): WizardUser {
        return user
    }

    fun getAddress(): WizardAddress {
        return address
    }

    fun getHobbies(): Set<String> {
        return hobbies
    }
}