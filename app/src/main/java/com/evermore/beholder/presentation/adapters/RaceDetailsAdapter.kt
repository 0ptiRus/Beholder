package com.evermore.beholder.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.evermore.beholder.databinding.ItemClassDetailCollapsibleTextBinding
import com.evermore.beholder.databinding.ItemClassDetailHeaderBinding
import com.evermore.beholder.databinding.ItemRaceDetailOptionsBinding
import com.evermore.beholder.presentation.models.CollapsibleTextDisplayable
import com.evermore.beholder.presentation.models.HeaderDisplayable
import com.evermore.beholder.presentation.models.RaceDetailItem
import com.evermore.beholder.presentation.viewholders.BaseViewHolder
import com.evermore.beholder.presentation.viewholders.CollapsibleTextViewHolder
import com.evermore.beholder.presentation.viewholders.HeaderViewHolder
import com.evermore.beholder.presentation.viewholders.RaceOptionsListViewHolder


class RaceDetailsAdapter(
    private var items: MutableList<RaceDetailItem>,
    private val stringProvider: (Int, Any?) -> String
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_COLLAPSIBLE_TEXT = 1
        private const val VIEW_TYPE_OPTIONS_LIST = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is RaceDetailItem.Header -> VIEW_TYPE_HEADER
            is RaceDetailItem.CollapsibleText -> VIEW_TYPE_COLLAPSIBLE_TEXT
            is RaceDetailItem.OptionsList -> VIEW_TYPE_OPTIONS_LIST
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = ItemClassDetailHeaderBinding.inflate(inflater, parent, false)
                HeaderViewHolder<RaceDetailItem.Header>(binding)
            }

            VIEW_TYPE_COLLAPSIBLE_TEXT -> {
                val binding = ItemClassDetailCollapsibleTextBinding.inflate(inflater, parent, false)
                CollapsibleTextViewHolder<RaceDetailItem.CollapsibleText>(
                    binding,
                    stringProvider
                ) { position, isExpanded ->

                    if (position != RecyclerView.NO_POSITION && position < items.size) {
                        (items[position] as? RaceDetailItem.CollapsibleText)?.isExpanded =
                            isExpanded
                    }
                }
            }

            VIEW_TYPE_OPTIONS_LIST -> {
                val binding = ItemRaceDetailOptionsBinding.inflate(inflater, parent, false)
                RaceOptionsListViewHolder(binding, stringProvider) { position, isExpanded ->

                    if (position != RecyclerView.NO_POSITION && position < items.size) {
                        (items[position] as? RaceDetailItem.OptionsList)?.isExpanded =
                            isExpanded
                    }
                }
            }

            else -> throw IllegalArgumentException("Invalid view type: $viewType")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = items[position]
        when (holder) {
            is HeaderViewHolder -> (holder as HeaderViewHolder<HeaderDisplayable>)
                .bind(element as RaceDetailItem.Header)

            is CollapsibleTextViewHolder -> (holder as CollapsibleTextViewHolder<CollapsibleTextDisplayable>)
                .bind(element as RaceDetailItem.CollapsibleText)

            is RaceOptionsListViewHolder -> holder.bind(element as RaceDetailItem.OptionsList)
            else -> throw IllegalArgumentException("Invalid view holder type")
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<RaceDetailItem>) {
        this.items = newItems.toMutableList()
        notifyDataSetChanged()
    }

}