package ru.otus.basicarchitecture.helpers

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import ru.otus.basicarchitecture.data.MockData

object ChipLoader {
    private val defaultHobbies = MockData().hobbies

    fun loadChipInto(
        chipGroup: ChipGroup, tags: Set<String> = defaultHobbies,
        style: (Chip.() -> Unit) = {
            isClickable = true
            isCheckable = true
        },
        onChipClicked: ((Chip) -> Unit)? = null,
        checkedChips: Set<String> = emptySet()
    ) {
        tags.forEach { tag ->
            Chip(chipGroup.context).apply {
                text = tag
                isSelected = checkedChips.contains(text)
                style()

                onChipClicked?.let { listener ->
                    setOnClickListener {
                        listener(this)
                    }
                }
            }.also {
                chipGroup.addView(it)
            }
        }
    }
}