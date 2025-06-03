package ru.otus.basicarchitecture.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.otus.basicarchitecture.WizardCache
import ru.otus.basicarchitecture.WizardUser
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val wizardCache: WizardCache) : ViewModel() {
    private var _user = MutableStateFlow<WizardUser?>(null)
    val user = _user.asStateFlow()

    val isUserBirthdayValid = _user.map { user ->
        Period.between(user?.birthday ?: LocalDate.now(), LocalDate.now()).years >= 18
    }.stateIn(
        initialValue = false,
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L)
    )

    fun setBirthday(newBirthday: LocalDate) {
        _user.value = _user.value?.copy(birthday = newBirthday) ?: WizardUser("", "", newBirthday)
    }

    fun setUser(userName: String, userSurname: String) {
        _user.value = _user.value?.copy(name = userName, lastname = userSurname) ?: WizardUser(
            userName,
            userSurname,
            LocalDate.now()
        )
    }

    fun initUser() {
        _user.value = wizardCache.getUser()
    }

    fun saveUserToWizard() {
        _user.value?.let { wizardCache.setNewUser(it) }
    }

}