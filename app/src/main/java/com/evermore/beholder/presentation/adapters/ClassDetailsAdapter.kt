package com.evermore.beholder.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.evermore.beholder.databinding.ItemClassDetailCollapsibleTextBinding
import com.evermore.beholder.databinding.ItemClassDetailHeaderBinding
import com.evermore.beholder.databinding.ItemClassDetailLevelProgressionBinding
import com.evermore.beholder.presentation.models.CollapsibleTextDisplayable
import com.evermore.beholder.presentation.models.HeaderDisplayable
import com.evermore.beholder.presentation.models.LevelProgressionRow
import com.evermore.beholder.presentation.viewholders.BaseViewHolder
import com.evermore.beholder.presentation.viewholders.CollapsibleTextViewHolder
import com.evermore.beholder.presentation.viewholders.HeaderViewHolder
import com.evermore.beholder.presentation.viewholders.LevelProgressionViewHolder

sealed class ClassDetailItem {
    data class Header(override val title: String) : ClassDetailItem(), HeaderDisplayable
    data class CollapsibleText(
        override val stringResId: Int,
        override val content: String,
        override var isExpanded: Boolean = true,
    ) : ClassDetailItem(), CollapsibleTextDisplayable

    data class LevelProgression(val rows: List<LevelProgressionRow>) : ClassDetailItem()
}

class ClassDetailsAdapter(
    private val stringProvider: (Int, Any?) -> String
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var items: MutableList<ClassDetailItem> = mutableListOf()

    var currentList: MutableList<ClassDetailItem> = items


    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_COLLAPSIBLE_TEXT = 1
        private const val VIEW_TYPE_LEVEL_PROGRESSION = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ClassDetailItem.Header -> VIEW_TYPE_HEADER
            is ClassDetailItem.CollapsibleText -> VIEW_TYPE_COLLAPSIBLE_TEXT
            is ClassDetailItem.LevelProgression -> VIEW_TYPE_LEVEL_PROGRESSION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = ItemClassDetailHeaderBinding.inflate(inflater, parent, false)
                HeaderViewHolder<ClassDetailItem.Header>(binding)
            }

            VIEW_TYPE_COLLAPSIBLE_TEXT -> {
                val binding = ItemClassDetailCollapsibleTextBinding.inflate(inflater, parent, false)
                CollapsibleTextViewHolder<ClassDetailItem.CollapsibleText>(
                    binding,
                    stringProvider
                ) { position, isExpanded ->

                    if (position != RecyclerView.NO_POSITION && position < items.size) {
                        (items[position] as? ClassDetailItem.CollapsibleText)?.isExpanded =
                            isExpanded
                    }
                }
            }

            VIEW_TYPE_LEVEL_PROGRESSION -> {
                val binding =
                    ItemClassDetailLevelProgressionBinding.inflate(inflater, parent, false)
                LevelProgressionViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type: $viewType")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = items[position]
        when (holder) {
            is HeaderViewHolder -> (holder as HeaderViewHolder<HeaderDisplayable>)
                .bind(element as ClassDetailItem.Header)

            is CollapsibleTextViewHolder<*> -> (holder as CollapsibleTextViewHolder<CollapsibleTextDisplayable>)
                .bind(element as CollapsibleTextDisplayable)
            is LevelProgressionViewHolder -> holder.bind(element as ClassDetailItem.LevelProgression)
            else -> throw IllegalArgumentException("Invalid view holder type")
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<ClassDetailItem>) {
        val diffResult = DiffUtil.calculateDiff(ClassDetailDiffCallback(this.items, newItems))
        this.items.clear()
        this.items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }


}


class ClassDetailDiffCallback(
    private val oldList: List<ClassDetailItem>,
    private val newList: List<ClassDetailItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return when {
            oldItem is ClassDetailItem.Header && newItem is ClassDetailItem.Header ->
                oldItem.title == newItem.title

            oldItem is ClassDetailItem.CollapsibleText && newItem is ClassDetailItem.CollapsibleText ->
                oldItem.stringResId == newItem.stringResId && oldItem.content == newItem.content

            oldItem is ClassDetailItem.LevelProgression && newItem is ClassDetailItem.LevelProgression ->
                oldItem.rows == newItem.rows

            else -> oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem == newItem
    }
}