package ru.otus.basicarchitecture.ui.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.otus.basicarchitecture.WizardAddress
import ru.otus.basicarchitecture.databinding.VhAddressSuggestionsBinding

class AddressAdapter(private val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<AddressViewHolder>() {
    private var suggestions: List<WizardAddress> = emptyList()

    fun setSuggestions(newSuggestions: List<WizardAddress>) {
        suggestions = newSuggestions
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            VhAddressSuggestionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = suggestions.size

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val suggestion = suggestions[position]
        holder.bind(suggestion, onClick)
    }

}