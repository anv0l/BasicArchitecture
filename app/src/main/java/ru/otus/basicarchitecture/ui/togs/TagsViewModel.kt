package ru.otus.basicarchitecture.ui.togs

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.otus.basicarchitecture.WizardCache
import javax.inject.Inject

@HiltViewModel
class TagsViewModel @Inject constructor(private val wizardCache: WizardCache) : ViewModel() {
    fun setHobbies(newHobbies: List<String>) {
        wizardCache.setHobbies(newHobbies)
    }
}