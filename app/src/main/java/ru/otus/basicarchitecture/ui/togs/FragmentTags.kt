package ru.otus.basicarchitecture.ui.togs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
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
                val hobbies =
                    chipHobbiesTagsGroup.children.toList().filterIsInstance<Chip>().filter {
                        it.isChecked
                    }.map { it.text.toString() }
                viewModel.setHobbies(hobbies)
                navController.navigate(R.id.action_tags_to_summary)
            }
            tagsLoader.loadChipInto(chipHobbiesTagsGroup)
        }
    }
}