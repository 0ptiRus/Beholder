package com.evermore.beholder.presentation.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.evermore.beholder.R
import com.evermore.beholder.databinding.ItemDetailActionBinding
import com.evermore.beholder.presentation.models.MonsterDetailItem

class MonsterActionItemViewHolder(private val binding: ItemDetailActionBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MonsterDetailItem.ActionItem) {
        binding.actionName.text = item.name
        binding.actionDescription.text = item.description

        val context = binding.root.context
        if (item.attackBonus != null) {
            binding.actionAttackBonus.text =
                context.getString(R.string.attack_bonus_template, item.attackBonus)
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