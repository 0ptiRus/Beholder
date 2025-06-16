package com.evermore.beholder.presentation.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.evermore.beholder.R
import com.evermore.beholder.databinding.ItemDetailStatBlockBinding
import com.evermore.beholder.presentation.models.MonsterDetailItem

class MonsterStatBlockViewHolder(private val binding: ItemDetailStatBlockBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MonsterDetailItem.StatBlock) {
        val context = binding.root.context
        binding.tvStrength.text =
            context.getString(R.string.stat_template, "STR", item.str, calculateModifier(item.str))
        binding.tvDexterity.text =
            context.getString(R.string.stat_template, "DEX", item.dex, calculateModifier(item.dex))
        binding.tvConstitution.text =
            context.getString(R.string.stat_template, "CON", item.con, calculateModifier(item.con))
        binding.tvIntelligence.text =
            context.getString(R.string.stat_template, "INT", item.int, calculateModifier(item.int))
        binding.tvWisdom.text =
            context.getString(R.string.stat_template, "WIS", item.wis, calculateModifier(item.wis))
        binding.tvCharisma.text =
            context.getString(R.string.stat_template, "CHA", item.cha, calculateModifier(item.cha))
    }

    private fun calculateModifier(score: Int): String {
        val modifier = (score - 10) / 2
        return if (modifier >= 0) "+$modifier" else "$modifier"
    }
}