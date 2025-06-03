package ru.otus.basicarchitecture.ui.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.otus.basicarchitecture.R
import ru.otus.basicarchitecture.WizardAddress
import ru.otus.basicarchitecture.databinding.FragmentAddressBinding
import ru.otus.basicarchitecture.helpers.navController

@AndroidEntryPoint
class FragmentAddress : Fragment() {
    private lateinit var binding: FragmentAddressBinding

    private val viewModel: AddressViewModel by viewModels()

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
            R.layout.fragment_address,
            container,
            false
        )
        binding = FragmentAddressBinding.bind(view)
        return binding.root
    }

    private fun setupBindings() {
        with(binding) {
            btnAddressNext.setOnClickListener {
                viewModel.saveToWizardCache()
                navController.navigate(R.id.action_address_to_tags)
            }

            lifecycleScope.launch {
                viewModel.nextAvailable.collect { isAvailable ->
                    btnAddressNext.isEnabled = isAvailable
                }
            }

            viewModel.initAddress()
            viewModel.address.value.let {
                txtAddress.setText(it?.address)
                txtCity.setText(it?.city)
                txtCountry.setText(it?.address)
            }

            txtAddress.doAfterTextChanged { updateAddress() }
            txtCountry.doAfterTextChanged { updateAddress() }
            txtCountry.doAfterTextChanged { updateAddress() }
        }
    }

    private fun updateAddress() {
        viewModel.setAddress(
            WizardAddress(
                country = binding.txtCountry.text.toString(),
                city = binding.txtCity.text.toString(),
                address = binding.txtAddress.text.toString()
            )
        )
    }

}