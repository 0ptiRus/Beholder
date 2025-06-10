package com.evermore.beholder.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.evermore.beholder.data.models.LevelProgressionRow
import com.evermore.beholder.databinding.ItemLevelProgressionRowBinding

class ProgressionTableAdapter(private val rows: List<LevelProgressionRow>) :
    RecyclerView.Adapter<ProgressionTableAdapter.ProgressionRowViewHolder>() {

    class ProgressionRowViewHolder(val binding: ItemLevelProgressionRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressionRowViewHolder {
        val binding = ItemLevelProgressionRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProgressionRowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProgressionRowViewHolder, position: Int) {
        val row = rows[position]
        holder.binding.levelTextView.text = row.level.toString()
        holder.binding.profBonusTextView.text = row.profBonus.toString()
        holder.binding.featuresTextView.text = row.features
        holder.binding.spellSlotsTextView.text = row.spellSlots
    }

    override fun getItemCount(): Int = rows.size
}