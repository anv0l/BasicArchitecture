package ru.otus.basicarchitecture.ui.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.otus.basicarchitecture.R
import ru.otus.basicarchitecture.databinding.FragmentSummaryBinding
import ru.otus.basicarchitecture.helpers.ChipLoader
import ru.otus.basicarchitecture.helpers.toText
import ru.otus.basicarchitecture.toText

@AndroidEntryPoint
class FragmentSummary : Fragment() {
    private lateinit var binding: FragmentSummaryBinding
    private val tagsLoader: ChipLoader = ChipLoader
    private val viewModel: SummaryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBindings()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.fragment_summary,
            container,
            false
        )
        binding = FragmentSummaryBinding.bind(view)
        return binding.root
    }

    private fun setupBindings() {
        val wizardCache = viewModel.getWizard()

        val user = wizardCache.getUser()
        val address = wizardCache.getAddress()
        val hobbies = wizardCache.getHobbies()
        with(binding) {
            txtName.text = user.name
            txtSurname.text = user.lastname
            user.birthday.toText()
                .onSuccess { date ->
                    txtBirthday.text = date
                }
                .onFailure { e ->
                    txtBirthday.text = e.message
                }

            txtAddress.text = address.toText()
            tagsLoader.loadChipInto(chipHobbiesTagsGroup, hobbies, {
                isCheckable = false
                isClickable = false
                isSelected = true
                setChipBackgroundColorResource(R.color.m3_chip_background_color)
            })
        }
    }
}