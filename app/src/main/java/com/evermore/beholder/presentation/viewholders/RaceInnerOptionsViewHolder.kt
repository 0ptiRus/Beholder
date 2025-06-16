package com.evermore.beholder.presentation.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.evermore.beholder.databinding.ItemRaceDetailOptionBinding
import com.evermore.beholder.presentation.models.OptionDisplayData

class RaceInnerOptionViewHolder(private val binding: ItemRaceDetailOptionBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(option: OptionDisplayData) {
        binding.optionName.text = option.name
        binding.optionDescription.text = option.description
    }
}