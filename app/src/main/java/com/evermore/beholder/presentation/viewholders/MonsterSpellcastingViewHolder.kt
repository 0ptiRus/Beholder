package com.evermore.beholder.presentation.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.evermore.beholder.R
import com.evermore.beholder.databinding.ItemDetailSpellcastingBinding
import com.evermore.beholder.presentation.models.MonsterDetailItem

class MonsterSpellcastingViewHolder(private val binding: ItemDetailSpellcastingBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MonsterDetailItem.Spellcasting) {
        val context = binding.root.context
        binding.itemTitle.setOnClickListener {
            binding.expandableLayout.toggle()
            binding.arrowIcon.rotation = if (binding.expandableLayout.isExpanded) 180f else 0f
        }
        binding.tvSpellcastingLevel.text =
            context.getString(R.string.creature_spellcasting_level_template, item.level)
        binding.tvSpellcastingAbility.text = context.getString(
            R.string.creature_spellcasting_ability_template,
            item.ability,
            item.dc,
            item.modifier
        )

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
        binding.llSpellSlotsContainer.visibility =
            if (item.spellSlots.isNotEmpty()) View.VISIBLE else View.GONE

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
        binding.llSpellsByLevelContainer.visibility =
            if (item.spellsByLevel.isNotEmpty()) View.VISIBLE else View.GONE
    }
}