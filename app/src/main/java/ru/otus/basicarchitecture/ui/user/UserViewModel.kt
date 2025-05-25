package ru.otus.basicarchitecture.ui.user

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.otus.basicarchitecture.WizardCache
import ru.otus.basicarchitecture.WizardUser
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val wizardCache: WizardCache) : ViewModel() {
    private val _birthday = MutableStateFlow<LocalDate?>(null)
    var birthday = _birthday.asStateFlow()

    val isBirthdayValid = _birthday.combine(_birthday) { day, _ ->
        Period.between(day ?: LocalDate.now(), LocalDate.now()).years >= 18
    }.stateIn(
        initialValue = false,
        scope = CoroutineScope(Dispatchers.Default),
        started = SharingStarted.WhileSubscribed(5000L)
    )

    fun setUserData(name: String, surname: String) {
        wizardCache.setNewUser(
            WizardUser(
                name = name,
                lastname = surname,
                birthday = _birthday.value ?: LocalDate.now()
            )
        )
    }

    fun setBirthday(newDate: LocalDate) {
        _birthday.value = newDate
    }
}