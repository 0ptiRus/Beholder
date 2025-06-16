package com.evermore.beholder.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.evermore.beholder.databinding.ItemRaceDetailOptionBinding
import com.evermore.beholder.presentation.models.OptionDisplayData
import com.evermore.beholder.presentation.viewholders.RaceInnerOptionViewHolder

class RaceInnerOptionsAdapter :
    RecyclerView.Adapter<RaceInnerOptionViewHolder>() {

    private val options = mutableListOf<OptionDisplayData>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateOptions(newOptions: List<OptionDisplayData>) {
        options.clear()
        options.addAll(newOptions)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaceInnerOptionViewHolder {
        val binding = ItemRaceDetailOptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RaceInnerOptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RaceInnerOptionViewHolder, position: Int) {
        holder.bind(options[position])
    }

    override fun getItemCount(): Int = options.size
}
