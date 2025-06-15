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
import ru.otus.basicarchitecture.databinding.FragmentAddressBinding
import ru.otus.basicarchitecture.helpers.navController

@AndroidEntryPoint
class FragmentAddress : Fragment() {
    private lateinit var binding: FragmentAddressBinding

    private val viewModel: AddressViewModel by viewModels()
    private lateinit var adapter: AddressAdapter

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
                viewModel.setStateDataIsSet()
                txtAddress.setText(it?.address)
            }

            txtAddress.doAfterTextChanged {
                if (viewModel.state.value == State.DataIsSet) {
                    viewModel.setStateReady()
                } else if (viewModel.state.value == State.Ready)
                    findAddress()
            }

            adapter = AddressAdapter { pos ->
                val wizardAddress = viewModel.addressSuggestions.value[pos]
                viewModel.setAddress(wizardAddress)
                txtAddress.setText(wizardAddress.value)
            }
            lstAddressSuggestions.adapter = adapter

            lifecycleScope.launch {
                viewModel.addressSuggestions.collect { suggestions ->
                    adapter.setSuggestions(suggestions)
                }
            }

            lifecycleScope.launch {
                viewModel.state.collect { state ->
                    when (state) {
                        State.Loading -> viewLoading.visibility = View.VISIBLE
                        else -> viewLoading.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun findAddress() {
        val searchString = binding.txtAddress.text.toString()

        viewModel.getSuggestion(searchString)
    }

}