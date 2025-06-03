package ru.otus.basicarchitecture.ui.togs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.otus.basicarchitecture.R
import ru.otus.basicarchitecture.databinding.FragmentTagsBinding
import ru.otus.basicarchitecture.helpers.ChipLoader
import ru.otus.basicarchitecture.helpers.navController

@AndroidEntryPoint
class FragmentTags : Fragment() {
    private lateinit var binding: FragmentTagsBinding
    private val viewModel: TagsViewModel by viewModels()

    private val tagsLoader: ChipLoader = ChipLoader

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupBindings()

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.fragment_tags,
            container,
            false
        )
        binding = FragmentTagsBinding.bind(view)
        return binding.root
    }

    private fun setupBindings() {
        with(binding) {

            btnTagsNext.setOnClickListener {
                viewModel.saveTagsToWizard()
                navController.navigate(R.id.action_tags_to_summary)
            }
            viewModel.initHobbies()

            tagsLoader.loadChipInto(
                chipHobbiesTagsGroup, onChipClicked = { chip ->
                    toggleChip(chip)
                }, checkedChips = viewModel.selectedTags.value
            )

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.isTagsEmpty.collect { isEmpty ->
                    btnTagsNext.isEnabled = !isEmpty
                }
            }
        }
    }

    private fun toggleChip(chip: Chip) {
        viewModel.toggleChip(chip.text.toString())
    }

}