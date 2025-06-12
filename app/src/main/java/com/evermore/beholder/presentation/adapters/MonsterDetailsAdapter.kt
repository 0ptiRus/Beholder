// com/evermore/beholder/presentation/adapters/MonsterDetailsAdapter.kt
package com.evermore.beholder.presentation.adapters

// Импортируем ваши макеты

// Импорты для остальных макетов

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.evermore.beholder.R
import com.evermore.beholder.databinding.ItemClassDetailCollapsibleTextBinding
import com.evermore.beholder.databinding.ItemClassDetailHeaderBinding
import com.evermore.beholder.databinding.ItemDetailActionBinding
import com.evermore.beholder.databinding.ItemDetailSpellcastingBinding
import com.evermore.beholder.databinding.ItemDetailStatBlockBinding
import com.evermore.beholder.presentation.models.MonsterDetailItem

// StringProvider теперь нужен в конструкторе адаптера
class MonsterDetailsAdapter(
    private val stringProvider: (Int, Any?) -> String // Для получения строк из StringResId
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<MonsterDetailItem>()

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_COLLAPSIBLE_TEXT = 1
        private const val VIEW_TYPE_STAT_BLOCK = 2
        private const val VIEW_TYPE_SPELLCASTING = 3
        private const val VIEW_TYPE_ACTION_ITEM = 4
    }

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
                HeaderViewHolder(binding)
            }
            VIEW_TYPE_COLLAPSIBLE_TEXT -> {
                val binding = ItemClassDetailCollapsibleTextBinding.inflate(inflater, parent, false)
                CollapsibleTextViewHolder(binding, stringProvider) // Передаем stringProvider
            }
            VIEW_TYPE_STAT_BLOCK -> {
                val binding = ItemDetailStatBlockBinding.inflate(inflater, parent, false)
                StatBlockViewHolder(binding)
            }
            VIEW_TYPE_SPELLCASTING -> {
                val binding = ItemDetailSpellcastingBinding.inflate(inflater, parent, false)
                SpellcastingViewHolder(binding)
            }
            VIEW_TYPE_ACTION_ITEM -> {
                val binding = ItemDetailActionBinding.inflate(inflater, parent, false)
                ActionItemViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is MonsterDetailItem.Header -> (holder as HeaderViewHolder).bind(item)
            is MonsterDetailItem.CollapsibleText -> (holder as CollapsibleTextViewHolder).bind(item)
            is MonsterDetailItem.StatBlock -> (holder as StatBlockViewHolder).bind(item)
            is MonsterDetailItem.Spellcasting -> (holder as SpellcastingViewHolder).bind(item)
            is MonsterDetailItem.ActionItem -> (holder as ActionItemViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    // --- ViewHolders ---

    class HeaderViewHolder(private val binding: ItemClassDetailHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MonsterDetailItem.Header) {
            binding.headerTitle.text = item.title
        }
    }

    class CollapsibleTextViewHolder(
        private val binding: ItemClassDetailCollapsibleTextBinding,
        private val stringProvider: (Int, Any?) -> String // Теперь CollapsibleTextViewHolder принимает stringProvider
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.collapsibleTextHeader.setOnClickListener {
                binding.collapsibleTextContainer.toggle()
                // Поскольку у вас нет стрелки, логика вращения не нужна.
            }
        }

        fun bind(item: MonsterDetailItem.CollapsibleText) {
            // Разрешаем StringResId для заголовка
            binding.collapsibleTextHeader.text = stringProvider(item.stringResId, null)
            binding.collapsibleContent.text = item.content

            if (item.isExpanded) {
                binding.collapsibleTextContainer.expand(false) // Развернуть без анимации
            } else {
                binding.collapsibleTextContainer.collapse(false) // Свернуть без анимации
            }
            // Здесь мы не устанавливаем isClickable = false, потому что "isAlwaysExpanded" нет.
            // Вместо этого, состояние управляется isExpanded в data class.
        }
    }

    // StatBlockViewHolder, SpellcastingViewHolder, ActionItemViewHolder остаются без изменений
    class StatBlockViewHolder(private val binding: ItemDetailStatBlockBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MonsterDetailItem.StatBlock) {
            val context = binding.root.context
            binding.tvStrength.text = context.getString(R.string.stat_template, "STR", item.str, calculateModifier(item.str))
            binding.tvDexterity.text = context.getString(R.string.stat_template, "DEX", item.dex, calculateModifier(item.dex))
            binding.tvConstitution.text = context.getString(R.string.stat_template, "CON", item.con, calculateModifier(item.con))
            binding.tvIntelligence.text = context.getString(R.string.stat_template, "INT", item.int, calculateModifier(item.int))
            binding.tvWisdom.text = context.getString(R.string.stat_template, "WIS", item.wis, calculateModifier(item.wis))
            binding.tvCharisma.text = context.getString(R.string.stat_template, "CHA", item.cha, calculateModifier(item.cha))
        }

        private fun calculateModifier(score: Int): String {
            val modifier = (score - 10) / 2
            return if (modifier >= 0) "+$modifier" else "$modifier"
        }
    }

    class SpellcastingViewHolder(private val binding: ItemDetailSpellcastingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MonsterDetailItem.Spellcasting) {
            val context = binding.root.context
            binding.itemTitle.setOnClickListener {
                binding.expandableLayout.toggle()
                binding.arrowIcon.rotation = if (binding.expandableLayout.isExpanded) 180f else 0f
            }
            binding.tvSpellcastingLevel.text = context.getString(R.string.creature_spellcasting_level_template, item.level)
            binding.tvSpellcastingAbility.text = context.getString(R.string.creature_spellcasting_ability_template, item.ability, item.dc, item.modifier)

            binding.tvCantrips.text = context.getString(R.string.cantrips_template, item.cantrips)
            binding.tvCantrips.visibility = if (item.cantrips.isNotBlank()) View.VISIBLE else View.GONE

            binding.llSpellSlotsContainer.removeAllViews()
            item.spellSlots.forEach { (level, slots) ->
                val slotText = context.getString(R.string.spell_slot_template, level, slots)
                val textView = TextView(context).apply {
                    text = slotText
                }
                binding.llSpellSlotsContainer.addView(textView)
            }
            binding.llSpellSlotsContainer.visibility = if (item.spellSlots.isNotEmpty()) View.VISIBLE else View.GONE

            binding.llSpellsByLevelContainer.removeAllViews()
            item.spellsByLevel.forEach { (level, spells) ->
                if (spells.isNotEmpty()) {
                    val levelHeader = TextView(context).apply {
                        text = context.getString(R.string.spells_level_header_template, level)
                        setPadding(0, context.resources.getDimensionPixelSize(R.dimen._8dp), 0, 0)
                    }
                    binding.llSpellsByLevelContainer.addView(levelHeader)

                    spells.forEach { spellName ->
                        val spellTextView = TextView(context).apply {
                            text = context.getString(R.string.spell_item_text, spellName)
                        }
                        binding.llSpellsByLevelContainer.addView(spellTextView)
                    }
                }
            }
            binding.llSpellsByLevelContainer.visibility = if (item.spellsByLevel.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }

    class ActionItemViewHolder(private val binding: ItemDetailActionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MonsterDetailItem.ActionItem) {
            binding.actionName.text = item.name
            binding.actionDescription.text = item.description

            val context = binding.root.context
            if (item.attackBonus != null) {
                binding.actionAttackBonus.text = context.getString(R.string.attack_bonus_template, item.attackBonus)
                binding.actionAttackBonus.visibility = View.VISIBLE
            } else {
                binding.actionAttackBonus.visibility = View.GONE
            }

            if (item.damageInfo != null) {
                binding.actionDamage.text = context.getString(R.string.damage_template, item.damageInfo)
                binding.actionDamage.visibility = View.VISIBLE
            } else {
                binding.actionDamage.visibility = View.GONE
            }
        }
    }
}