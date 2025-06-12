package com.evermore.beholder.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.evermore.beholder.R
import com.evermore.beholder.databinding.FragmentClassDetailsBinding
import com.evermore.beholder.presentation.adapters.ClassDetailItem
import com.evermore.beholder.presentation.adapters.ClassDetailsAdapter
import com.evermore.beholder.presentation.viewmodels.ClassDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ClassDetailsFragment : Fragment() {

    private var _binding: FragmentClassDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClassDetailsViewModel by viewModel()
    private lateinit var classDetailsAdapter: ClassDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        classDetailsAdapter = ClassDetailsAdapter(mutableListOf()) { stringResId, _ ->
            getString(stringResId)
        }
        binding.classDetailsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.classDetailsRecyclerView.adapter = classDetailsAdapter

        observeClassData()
        observeLevelData()

        // Load data from assets
        val classJsonString =
            requireContext().assets.open("class_druid.json").bufferedReader().readText()
        viewModel.loadClassData(classJsonString)

        val levelsJsonString =
            requireContext().assets.open("druid_levels.json").bufferedReader().readText()
        viewModel.loadLevelProgressionData(levelsJsonString)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun observeClassData() {
        viewModel.classData.observe(viewLifecycleOwner)
        { data ->

            val items = mutableListOf<ClassDetailItem>()

            items.add(ClassDetailItem.Header(data.name))
            items.add(
                ClassDetailItem.CollapsibleText(
                    R.string.hit_dice_text,
                    getString(R.string.hitdie_template, data.hit_die)
                )
            )
            items.add(
                ClassDetailItem.CollapsibleText(
                    R.string.proficiencies_text,
                    getString(
                        R.string.proficiencies_template,
                        data.proficiencies.joinToString("\n") { it.name })
                )
            )
            items.add(
                ClassDetailItem.CollapsibleText(
                    R.string.saving_throws_text,
                    getString(
                        R.string.saving_throws_template,
                        data.saving_throws.joinToString(", ") { it.name })
                )
            )

            val spellcasting = data.spellcasting
            if (spellcasting != null) {
                val spellText = buildString {
                    append(
                        getString(
                            R.string.spellcasting_ability_template,
                            spellcasting.spellcasting_ability.name
                        )
                    )
                    append("\n\n")
                    spellcasting.info.forEach { block ->
                        append("${block.name}:\n")
                        block.desc.forEach { line -> append("• $line\n") }
                        append("\n")
                    }
                }
                items.add(ClassDetailItem.CollapsibleText(R.string.spellcasting_text, spellText))
            } else {
                items.add(
                    ClassDetailItem.CollapsibleText(
                        R.string.spellcasting_text,
                        getString(R.string.no_spellcasting_text)
                    )
                )
            }

            items.add(ClassDetailItem.LevelProgression(emptyList()))

            classDetailsAdapter.updateItems(items)
        }
    }

    fun observeLevelData() {
        viewModel.levelProgression.observe(viewLifecycleOwner)
        { levelProgressionRows ->
            val currentItems = classDetailsAdapter.currentList.toMutableList()

            val existingTableIndex =
                currentItems.indexOfFirst { it is ClassDetailItem.LevelProgression }

            if (existingTableIndex != -1) {
                currentItems[existingTableIndex] =
                    ClassDetailItem.LevelProgression(levelProgressionRows)
            } else {
                currentItems.add(ClassDetailItem.LevelProgression(levelProgressionRows))
            }
            classDetailsAdapter.updateItems(currentItems)
        }
    }
}