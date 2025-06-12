package com.evermore.beholder.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.evermore.beholder.R
import com.evermore.beholder.data.models.SelectableEntry
import com.evermore.beholder.databinding.FragmentItemSelectionBinding
import com.evermore.beholder.presentation.adapters.SelectableItemAdapter


class ClassSelectionFragment : Fragment() {

    private var _binding: FragmentItemSelectionBinding? = null
    private val binding get() = _binding!!

    private val classList = listOf(
        SelectableEntry("barbarian", "Barbarian", R.drawable.ic_barbarian),
        SelectableEntry("bard", "Bard", R.drawable.ic_bard),
        SelectableEntry("cleric", "Cleric", R.drawable.ic_cleric),
        SelectableEntry("druid", "Druid", R.drawable.ic_druid),
        SelectableEntry("fighter", "Fighter", R.drawable.ic_fighter),
        SelectableEntry("monk", "Monk", R.drawable.ic_monk),
        SelectableEntry("paladin", "Paladin", R.drawable.ic_paladin),
        SelectableEntry("ranger", "Ranger", R.drawable.ic_ranger),
        SelectableEntry("rogue", "Rogue", R.drawable.ic_rogue),
        SelectableEntry("sorcerer", "Sorcerer", R.drawable.ic_sorcerer),
        SelectableEntry("warlock", "Warlock", R.drawable.ic_warlock),
        SelectableEntry("wizard", "Wizard", R.drawable.ic_wizard)

    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.itemsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.itemsRecyclerView.adapter = SelectableItemAdapter(classList) { entry ->
            val action = ClassSelectionFragmentDirections
                .actionClassSelectionToClassDetail(entry.index)
            findNavController().navigate(action)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.itemsRecyclerView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                systemBars.bottom
            )
            insets
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
