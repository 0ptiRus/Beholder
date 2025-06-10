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

class RaceSelectionFragment : Fragment() {

    private var _binding: FragmentItemSelectionBinding? = null
    private val binding get() = _binding!!

    private val classList = listOf(
        SelectableEntry("dragonborn", "Dragonborn", R.drawable.ic_dragonborn),
        SelectableEntry("dwarf", "Dwarf", R.drawable.ic_races),
        SelectableEntry("elf", "Elf", R.drawable.ic_elf),
        SelectableEntry("gnome", "Gnome", R.drawable.ic_gnome),
        SelectableEntry("half-elf", "Half-elf", R.drawable.ic_halfelf),
        SelectableEntry("half-orc", "Half-orc", R.drawable.ic_halforc),
        SelectableEntry("halfling", "Halfling", R.drawable.ic_human),
        SelectableEntry("human", "Human", R.drawable.ic_human),
        SelectableEntry("tiefling", "Tiefling", R.drawable.ic_tiefling),
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
            val action = RaceSelectionFragmentDirections
                .actionRaceSelectionToClassDetail(entry.index)
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
