package com.evermore.beholder.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.evermore.beholder.R
import com.evermore.beholder.data.models.OptionDisplayData
import com.evermore.beholder.data.models.RaceDetailItem
import com.evermore.beholder.databinding.ItemClassDetailCollapsibleTextBinding
import com.evermore.beholder.databinding.ItemClassDetailHeaderBinding
import com.evermore.beholder.databinding.ItemRaceDetailOptionBinding
import com.evermore.beholder.databinding.ItemRaceDetailOptionsBinding
import net.cachapa.expandablelayout.ExpandableLayout


class RaceDetailsAdapter(
    private var items: MutableList<RaceDetailItem>,
    private val stringProvider: (Int, Any?) -> String
) : RecyclerView.Adapter<RaceDetailsAdapter.BaseViewHolder<*>>() {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_COLLAPSIBLE_TEXT = 1
        private const val VIEW_TYPE_OPTIONS_LIST = 2 // Новый тип представления
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
                HeaderViewHolder(binding)
            }

            VIEW_TYPE_COLLAPSIBLE_TEXT -> {
                val binding = ItemClassDetailCollapsibleTextBinding.inflate(inflater, parent, false)
                CollapsibleTextViewHolder(binding)
            }

            VIEW_TYPE_OPTIONS_LIST -> {
                val binding = ItemRaceDetailOptionsBinding.inflate(inflater, parent, false)
                OptionsListViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = items[position]
        when (holder) {
            is HeaderViewHolder -> holder.bind(element as RaceDetailItem.Header)
            is CollapsibleTextViewHolder -> holder.bind(element as RaceDetailItem.CollapsibleText)
            is OptionsListViewHolder -> holder.bind(element as RaceDetailItem.OptionsList)
            else -> throw IllegalArgumentException("Invalid view holder type")
        }
    }

    override fun getItemCount(): Int = items.size

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    inner class HeaderViewHolder(private val binding: ItemClassDetailHeaderBinding) :
        BaseViewHolder<RaceDetailItem.Header>(binding.root) {
        override fun bind(item: RaceDetailItem.Header) {
            binding.headerTitle.text = item.title
        }
    }


    inner class CollapsibleTextViewHolder(
        private val binding: ItemClassDetailCollapsibleTextBinding
    ) : BaseViewHolder<RaceDetailItem.CollapsibleText>(binding.root) {
        private val collapsibleTextContainer: ExpandableLayout = binding.collapsibleTextContainer

        init {
            binding.collapsibleTextHeader.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = items[position] as RaceDetailItem.CollapsibleText
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

        override fun bind(item: RaceDetailItem.CollapsibleText) {
            binding.collapsibleTextHeader.text = stringProvider(item.stringResId, null)
            binding.collapsibleContent.text = item.content
            if (item.isExpanded) {
                collapsibleTextContainer.expand(false)
            } else {
                collapsibleTextContainer.collapse(false)
            }
        }
    }

    inner class OptionsListViewHolder(
        private val binding: ItemRaceDetailOptionsBinding
    ) : BaseViewHolder<RaceDetailItem.OptionsList>(binding.root) {

        private val optionsAdapter = InnerOptionsAdapter()

        init {
            binding.optionsRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            binding.optionsRecyclerView.adapter = optionsAdapter
            binding.optionsRecyclerView.isNestedScrollingEnabled = false

            binding.optionsHeader.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = items[position] as RaceDetailItem.OptionsList
                    if (binding.optionsExpandableLayout.isExpanded) {
                        binding.optionsExpandableLayout.collapse()
                        item.isExpanded = false
                    } else {
                        binding.optionsExpandableLayout.expand()
                        item.isExpanded = true
                    }
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
                binding.optionsExpandableLayout.expand(false)
            } else {
                binding.optionsExpandableLayout.collapse(false)
            }
        }
    }

    class InnerOptionsAdapter() :
        RecyclerView.Adapter<InnerOptionsAdapter.InnerOptionViewHolder>() {

        private val options = mutableListOf<OptionDisplayData>()

        fun updateOptions(newOptions: List<OptionDisplayData>) {
            options.clear()
            options.addAll(newOptions)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerOptionViewHolder {
            val binding = ItemRaceDetailOptionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return InnerOptionViewHolder(binding)
        }

        override fun onBindViewHolder(holder: InnerOptionViewHolder, position: Int) {
            holder.bind(options[position])
        }

        override fun getItemCount(): Int = options.size

        class InnerOptionViewHolder(private val binding: ItemRaceDetailOptionBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(option: OptionDisplayData) {
                binding.optionName.text = option.name
                binding.optionDescription.text = option.description
            }
        }
    }


    fun updateItems(newItems: List<RaceDetailItem>) {
        this.items = newItems.toMutableList()
        notifyDataSetChanged()
    }

    val currentList: List<RaceDetailItem>
        get() = items.toList()
}