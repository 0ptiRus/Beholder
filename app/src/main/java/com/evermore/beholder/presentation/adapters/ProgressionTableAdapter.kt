package com.evermore.beholder.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.evermore.beholder.databinding.TableRowBinding
import com.evermore.beholder.presentation.classes.LevelProgressionRow

class ProgressionTableAdapter(
    private val rows: List<LevelProgressionRow>
) : RecyclerView.Adapter<ProgressionTableAdapter.TableViewHolder>() {

    inner class TableViewHolder(val binding: TableRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val binding = TableRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        val row = rows[position]
        holder.binding.level.text = row.level.toString()
        holder.binding.features.text = row.features
        holder.binding.spellSlots.text = row.spellSlots
    }

    override fun getItemCount(): Int = rows.size
}
