package com.evermore.beholder.presentation.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.evermore.beholder.data.dto.MonsterResult
import com.evermore.beholder.databinding.ItemGeneralListItemBinding

class BestiaryAdapter(
        private val onItemClick: (MonsterResult) -> Unit
): RecyclerView.Adapter<BestiaryAdapter.MonsterViewHolder>()
{
    private var monsters: List<MonsterResult> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun updateMonsters(newMonsters: List<MonsterResult>)
    {
        this.monsters = newMonsters
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterViewHolder
    {
        val binding = ItemGeneralListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MonsterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonsterViewHolder, position: Int)
    {
        val monster = monsters[position]
        holder.bind(monster)
    }

    override fun getItemCount(): Int = monsters.size

    inner class MonsterViewHolder(private val binding: ItemGeneralListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(monster: MonsterResult) {
            binding.itemName.text = monster.name

            binding.root.setOnClickListener { onItemClick(monster) }
        }
    }
}