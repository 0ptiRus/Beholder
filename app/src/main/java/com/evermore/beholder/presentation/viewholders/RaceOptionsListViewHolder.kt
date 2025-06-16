package com.evermore.beholder.presentation.viewholders

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.evermore.beholder.R
import com.evermore.beholder.databinding.ItemRaceDetailOptionsBinding
import com.evermore.beholder.presentation.adapters.RaceInnerOptionsAdapter
import com.evermore.beholder.presentation.models.RaceDetailItem
import net.cachapa.expandablelayout.ExpandableLayout // Assuming this is the correct import for ExpandableLayout

class RaceOptionsListViewHolder(
    private val binding: ItemRaceDetailOptionsBinding,
    private val stringProvider: (Int, Any?) -> String,
    private val onExpandedStateChanged: (Int, Boolean) -> Unit
) : BaseViewHolder<RaceDetailItem.OptionsList>(binding.root) {

    private val optionsAdapter = RaceInnerOptionsAdapter()
    private val optionsExpandableLayout: ExpandableLayout =
        binding.optionsExpandableLayout // Cache it

    init {
        binding.optionsRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
        binding.optionsRecyclerView.adapter = optionsAdapter
        binding.optionsRecyclerView.isNestedScrollingEnabled = false

        binding.optionsHeader.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                optionsExpandableLayout.toggle()
                onExpandedStateChanged(position, optionsExpandableLayout.isExpanded)
            }
        }
    }

    override fun bind(item: RaceDetailItem.OptionsList) {
        binding.optionsTitle.text = stringProvider(item.stringResId, null)
        binding.optionsChooseCount.text =
            String.format(
                stringProvider(R.string.race_choose_options_template, null),
                item.chooseCount
            )
        optionsAdapter.updateOptions(item.options)

        if (item.isExpanded) {
            optionsExpandableLayout.expand(false)
        } else {
            optionsExpandableLayout.collapse(false)
        }
    }
}