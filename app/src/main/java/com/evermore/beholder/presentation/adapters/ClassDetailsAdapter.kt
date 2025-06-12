package com.evermore.beholder.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.evermore.beholder.data.models.LevelProgressionRow
import com.evermore.beholder.databinding.ItemClassDetailCollapsibleTextBinding
import com.evermore.beholder.databinding.ItemClassDetailHeaderBinding
import com.evermore.beholder.databinding.ItemClassDetailLevelProgressionBinding
import net.cachapa.expandablelayout.ExpandableLayout

sealed class ClassDetailItem {
    data class Header(val title: String) : ClassDetailItem()
    data class CollapsibleText(
        val stringResId: Int,
        val content: String,
        var isExpanded: Boolean = true
    ) : ClassDetailItem()

    data class LevelProgression(val rows: List<LevelProgressionRow>) : ClassDetailItem()
}


class ClassDetailsAdapter(
    private var items: MutableList<ClassDetailItem>,
    private val stringProvider: (Int, Any?) -> String
) : RecyclerView.Adapter<ClassDetailsAdapter.BaseViewHolder<*>>() {

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
                HeaderViewHolder(binding)
            }

            VIEW_TYPE_COLLAPSIBLE_TEXT -> {
                val binding = ItemClassDetailCollapsibleTextBinding.inflate(inflater, parent, false)
                CollapsibleTextViewHolder(binding)
            }

            VIEW_TYPE_LEVEL_PROGRESSION -> {
                val binding =
                    ItemClassDetailLevelProgressionBinding.inflate(inflater, parent, false)
                LevelProgressionViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = items[position]
        when (holder) {
            is HeaderViewHolder -> holder.bind(element as ClassDetailItem.Header)
            is CollapsibleTextViewHolder -> holder.bind(element as ClassDetailItem.CollapsibleText)
            is LevelProgressionViewHolder -> holder.bind(element as ClassDetailItem.LevelProgression)
            else -> throw IllegalArgumentException("Invalid view holder type")
        }
    }

    override fun getItemCount(): Int = items.size

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    inner class HeaderViewHolder(private val binding: ItemClassDetailHeaderBinding) :
        BaseViewHolder<ClassDetailItem.Header>(binding.root) {
        override fun bind(item: ClassDetailItem.Header) {
            binding.headerTitle.text = item.title
        }
    }

    inner class CollapsibleTextViewHolder(private val binding: ItemClassDetailCollapsibleTextBinding) :
        BaseViewHolder<ClassDetailItem.CollapsibleText>(binding.root) {
        private val collapsibleTextContainer: ExpandableLayout = binding.collapsibleTextContainer

        init {
            binding.collapsibleTextHeader.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = items[position] as ClassDetailItem.CollapsibleText
                    if (collapsibleTextContainer.isExpanded) {
                        collapsibleTextContainer.collapse()
                        item.isExpanded = false
                    } else {
                        collapsibleTextContainer.expand()
                        item.isExpanded = true
                    }
                }
            }
        }

        override fun bind(item: ClassDetailItem.CollapsibleText) {
            binding.collapsibleTextHeader.text =
                stringProvider(item.stringResId, null)
            binding.collapsibleContent.text = item.content

            if (item.isExpanded) {
                collapsibleTextContainer.expand(false)
            } else {
                collapsibleTextContainer.collapse(false)
            }
        }
    }

    inner class LevelProgressionViewHolder(private val binding: ItemClassDetailLevelProgressionBinding) :
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

    fun updateItems(newItems: List<ClassDetailItem>) {
        this.items = newItems.toMutableList()
        notifyDataSetChanged()
    }

    val currentList: List<ClassDetailItem>
        get() = items.toList()
}