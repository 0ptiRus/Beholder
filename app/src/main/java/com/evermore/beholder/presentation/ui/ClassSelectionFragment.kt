package com.evermore.beholder.presentation.ui

import ClassAdapter
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
import com.evermore.beholder.databinding.FragmentClassSelectionBinding
import com.evermore.beholder.presentation.classes.ClassEntry


class ClassSelectionFragment : Fragment() {

    private var _binding: FragmentClassSelectionBinding? = null
    private val binding get() = _binding!!

    private val classList = listOf(
        ClassEntry("barbarian", "Barbarian", R.drawable.ic_barbarian),
        ClassEntry("bard", "Bard", R.drawable.ic_bard),
        ClassEntry("cleric", "Cleric", R.drawable.ic_cleric),
        ClassEntry("druid", "Druid", R.drawable.ic_druid),
        ClassEntry("fighter", "Fighter", R.drawable.ic_fighter),
        ClassEntry("monk", "Monk", R.drawable.ic_monk),
        ClassEntry("paladin", "Paladin", R.drawable.ic_paladin),
        ClassEntry("ranger", "Ranger", R.drawable.ic_ranger),
        ClassEntry("rogue", "Rogue", R.drawable.ic_rogue),
        ClassEntry("sorcerer", "Sorcerer", R.drawable.ic_sorcerer),
        ClassEntry("warlock", "Warlock", R.drawable.ic_warlock),
        ClassEntry("wizard", "Wizard", R.drawable.ic_wizard)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.classRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.classRecyclerView.adapter = ClassAdapter(classList) { entry ->
            val action = ClassSelectionFragmentDirections
                .actionClassSelectionToClassDetail(entry.index)
            findNavController().navigate(action)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.classRecyclerView) { view, insets ->
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
