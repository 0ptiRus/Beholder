package com.evermore.beholder.presentation.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.evermore.beholder.databinding.ItemClassDetailCollapsibleTextBinding
import com.evermore.beholder.databinding.ItemClassDetailHeaderBinding
import com.evermore.beholder.databinding.ItemDetailActionBinding
import com.evermore.beholder.databinding.ItemDetailSpellcastingBinding
import com.evermore.beholder.databinding.ItemDetailStatBlockBinding
import com.evermore.beholder.presentation.models.CollapsibleTextDisplayable
import com.evermore.beholder.presentation.models.HeaderDisplayable
import com.evermore.beholder.presentation.models.MonsterDetailItem
import com.evermore.beholder.presentation.viewholders.CollapsibleTextViewHolder
import com.evermore.beholder.presentation.viewholders.HeaderViewHolder
import com.evermore.beholder.presentation.viewholders.MonsterActionItemViewHolder
import com.evermore.beholder.presentation.viewholders.MonsterSpellcastingViewHolder
import com.evermore.beholder.presentation.viewholders.MonsterStatBlockViewHolder

class MonsterDetailsAdapter(
    private val stringProvider: (Int, Any?) -> String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<MonsterDetailItem>()

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_COLLAPSIBLE_TEXT = 1
        private const val VIEW_TYPE_STAT_BLOCK = 2
        private const val VIEW_TYPE_SPELLCASTING = 3
        private const val VIEW_TYPE_ACTION_ITEM = 4
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<MonsterDetailItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is MonsterDetailItem.Header -> VIEW_TYPE_HEADER
            is MonsterDetailItem.CollapsibleText -> VIEW_TYPE_COLLAPSIBLE_TEXT
            is MonsterDetailItem.StatBlock -> VIEW_TYPE_STAT_BLOCK
            is MonsterDetailItem.Spellcasting -> VIEW_TYPE_SPELLCASTING
            is MonsterDetailItem.ActionItem -> VIEW_TYPE_ACTION_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = ItemClassDetailHeaderBinding.inflate(inflater, parent, false)
                HeaderViewHolder<MonsterDetailItem.Header>(binding)
            }
            VIEW_TYPE_COLLAPSIBLE_TEXT -> {
                val binding = ItemClassDetailCollapsibleTextBinding.inflate(inflater, parent, false)
                CollapsibleTextViewHolder<MonsterDetailItem.CollapsibleText>(
                    binding,
                    stringProvider
                ) { position, isExpanded ->

                    if (position != RecyclerView.NO_POSITION && position < items.size) {
                        (items[position] as? MonsterDetailItem.CollapsibleText)?.isExpanded =
                            isExpanded
                    }
                }
            }
            VIEW_TYPE_STAT_BLOCK -> {
                val binding = ItemDetailStatBlockBinding.inflate(inflater, parent, false)
                MonsterStatBlockViewHolder(binding)
            }
            VIEW_TYPE_SPELLCASTING -> {
                val binding = ItemDetailSpellcastingBinding.inflate(inflater, parent, false)
                MonsterSpellcastingViewHolder(binding)
            }
            VIEW_TYPE_ACTION_ITEM -> {
                val binding = ItemDetailActionBinding.inflate(inflater, parent, false)
                MonsterActionItemViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is MonsterDetailItem.Header -> (holder as HeaderViewHolder<HeaderDisplayable>)
                .bind(item)

            is MonsterDetailItem.CollapsibleText ->
                (holder as CollapsibleTextViewHolder<CollapsibleTextDisplayable>)
                    .bind(item)

            is MonsterDetailItem.StatBlock -> (holder as MonsterStatBlockViewHolder).bind(item)
            is MonsterDetailItem.Spellcasting -> (holder as MonsterSpellcastingViewHolder).bind(item)
            is MonsterDetailItem.ActionItem -> (holder as MonsterActionItemViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = items.size
}