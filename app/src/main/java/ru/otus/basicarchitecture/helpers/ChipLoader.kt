package ru.otus.basicarchitecture.helpers

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import ru.otus.basicarchitecture.data.MockData

object ChipLoader {
    private val defaultHobbies = MockData().hobbies

    fun loadChipInto(
        chipGroup: ChipGroup, tags: List<String> = defaultHobbies,
        style: (Chip.() -> Unit) = {
            isClickable = true
            isCheckable = true
        }
    ) {
        tags.forEach { tag ->
            Chip(chipGroup.context).apply {
                text = tag
                style()
            }.also {
                chipGroup.addView(it)
            }
        }
    }
}