package ru.otus.basicarchitecture.ui.address

import androidx.recyclerview.widget.RecyclerView
import ru.otus.basicarchitecture.WizardAddress
import ru.otus.basicarchitecture.databinding.VhAddressSuggestionsBinding

class AddressViewHolder(private val binding: VhAddressSuggestionsBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        suggestItem: WizardAddress,
        onClick: (Int) -> Unit
    ) {
        with(binding) {
            txtSuggestStreetHouse.text = "${suggestItem.street} ${suggestItem.house}"
            txtSuggestRegionCity.text = suggestItem.value
            itemSuggestion.setOnClickListener { onClick(adapterPosition) }
        }
    }
}