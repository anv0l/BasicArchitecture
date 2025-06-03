package ru.otus.basicarchitecture.ui.togs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.otus.basicarchitecture.WizardCache
import javax.inject.Inject

@HiltViewModel
class TagsViewModel @Inject constructor(private val wizardCache: WizardCache) : ViewModel() {

    private var _selectedTags = MutableStateFlow<Set<String>>(emptySet())
    val selectedTags = _selectedTags.asStateFlow()

    val isTagsEmpty = _selectedTags.map { tags ->
        tags.isEmpty()
    }.stateIn(
        scope = viewModelScope,
        initialValue = false,
        started = SharingStarted.WhileSubscribed(5000)
    )

    fun initHobbies() {
        _selectedTags.value = wizardCache.getHobbies()
    }

    fun saveTagsToWizard() {
        _selectedTags.value.let { wizardCache.setHobbies(it) }
    }

    fun toggleChip(text: String) {
        if (_selectedTags.value.contains(text))
            _selectedTags.value -= text
        else
            _selectedTags.value += text
    }


}