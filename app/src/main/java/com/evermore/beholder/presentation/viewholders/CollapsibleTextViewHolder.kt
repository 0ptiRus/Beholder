package com.evermore.beholder.presentation.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.evermore.beholder.databinding.ItemClassDetailCollapsibleTextBinding
import com.evermore.beholder.presentation.models.CollapsibleTextDisplayable // Import the interface
import net.cachapa.expandablelayout.ExpandableLayout

class CollapsibleTextViewHolder<T : CollapsibleTextDisplayable>(
    private val binding: ItemClassDetailCollapsibleTextBinding,
    private val stringProvider: (Int, Any?) -> String,
    private val onExpandedStateChanged: (Int, Boolean) -> Unit
) : BaseViewHolder<T>(binding.root) {

    private val collapsibleTextContainer: ExpandableLayout = binding.collapsibleTextContainer

    init {
        binding.collapsibleTextHeader.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                collapsibleTextContainer.toggle()
                onExpandedStateChanged(position, collapsibleTextContainer.isExpanded)
            }
        }
    }

    override fun bind(item: T) {
        binding.collapsibleTextHeader.text = item.stringResId?.let { resId ->
            stringProvider(resId, null)
        }

        binding.collapsibleContent.text = item.content

        if (item.isExpanded) {
            collapsibleTextContainer.expand(false)
        } else {
            collapsibleTextContainer.collapse(false)
        }
    }
}