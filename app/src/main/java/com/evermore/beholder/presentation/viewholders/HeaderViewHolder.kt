package com.evermore.beholder.presentation.viewholders

import com.evermore.beholder.databinding.ItemClassDetailHeaderBinding
import com.evermore.beholder.presentation.models.HeaderDisplayable

class HeaderViewHolder<T : HeaderDisplayable>(
    private val binding: ItemClassDetailHeaderBinding
) :
    BaseViewHolder<T>(binding.root) {
    override fun bind(item: T) {
        binding.headerTitle.text = item.title
    }
}