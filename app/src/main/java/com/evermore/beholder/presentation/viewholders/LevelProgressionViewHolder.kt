package com.evermore.beholder.presentation.viewholders

import androidx.recyclerview.widget.LinearLayoutManager
import com.evermore.beholder.databinding.ItemClassDetailLevelProgressionBinding
import com.evermore.beholder.presentation.adapters.ClassDetailItem
import com.evermore.beholder.presentation.adapters.ProgressionTableAdapter

class LevelProgressionViewHolder(private val binding: ItemClassDetailLevelProgressionBinding) :
    BaseViewHolder<ClassDetailItem.LevelProgression>(binding.root) {
    init {
        binding.progressionTableRecyclerView.layoutManager =
            LinearLayoutManager(itemView.context)
        binding.progressionTableRecyclerView.isNestedScrollingEnabled = false
    }

    override fun bind(item: ClassDetailItem.LevelProgression) {
        binding.progressionTableRecyclerView.adapter = ProgressionTableAdapter(item.rows)
    }
}