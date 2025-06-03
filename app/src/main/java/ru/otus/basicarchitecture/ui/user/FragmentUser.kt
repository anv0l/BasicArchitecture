package ru.otus.basicarchitecture.ui.user

import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.otus.basicarchitecture.R
import ru.otus.basicarchitecture.databinding.FragmentPersonBinding
import ru.otus.basicarchitecture.helpers.navController
import ru.otus.basicarchitecture.helpers.toText
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@AndroidEntryPoint
class FragmentUser : Fragment() {
    private lateinit var binding: FragmentPersonBinding
    private val viewModel: UserViewModel by viewModels()

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
            R.layout.fragment_person,
            container,
            false
        )
        binding = FragmentPersonBinding.bind(view)
        return binding.root
    }

    private fun setupBindings() {
        with(binding) {
            btnPersonNext.setOnClickListener {
                viewModel.saveUserToWizard()
                navController.navigate(R.id.action_person_to_address)
            }
            txtBirthday.setOnClickListener {
                showDatePicker()
            }
            lifecycleScope.launch {
                viewModel.isUserBirthdayValid.collect { isValid ->
                    btnPersonNext.isEnabled = isValid
                    txt18Yo.visibility = if (isValid) View.GONE else View.VISIBLE
                }
            }

            viewModel.initUser()
            viewModel.user.value.let {
                txtName.setText(it?.name)
                txtSurname.setText(it?.lastname)
                val birthdayResult = it?.birthday?.toText()
                birthdayResult?.onSuccess { date -> txtBirthday.setText(date) }
            }

            txtName.doAfterTextChanged { updateUser() }
            txtSurname.doAfterTextChanged { updateUser() }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val currentBirthday = viewModel.user.value?.birthday ?: LocalDate.now()
        calendar.set(
            currentBirthday.year,
            currentBirthday.monthValue - 1,
            currentBirthday.dayOfMonth
        )
        val timestamp = calendar.timeInMillis

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select your birthday")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setSelection(timestamp)
            .build()

        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            val localDate = Instant.ofEpochMilli(selectedDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            val formattedDateResult = localDate.toText()
            formattedDateResult
                .onSuccess { formattedDate ->
                    binding.txtBirthday.setText(formattedDate)
                    viewModel.setBirthday(localDate)
                }
                .onFailure {
                    Toast.makeText(
                        context,
                        "Invalid date format for birthday!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
        datePicker.show(parentFragmentManager, "DATE_PICKER")
    }

    private fun updateUser() {
        viewModel.setUser(binding.txtName.text.toString(), binding.txtSurname.text.toString())
    }

}