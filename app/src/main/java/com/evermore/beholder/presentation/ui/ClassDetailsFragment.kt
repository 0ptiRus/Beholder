package com.evermore.beholder.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.evermore.beholder.R
import com.evermore.beholder.databinding.FragmentClassDetailsBinding
import com.evermore.beholder.presentation.viewmodels.ClassDetailsViewModel

class ClassDetailsFragment : Fragment() {

    private var _binding: FragmentClassDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClassDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.classData.observe(viewLifecycleOwner) { data ->
            binding.className.text = data.name
            binding.hitDie.text = getString(R.string.hitdie_template, data.hit_die)

            binding.proficiencies.text =
                getString(
                    R.string.proficiencies_template,
                    data.proficiencies.joinToString("\n") { it.name })

            binding.savingThrows.text =
                getString(
                    R.string.saving_throws_template,
                    data.saving_throws.joinToString(", ") { it.name })

            val spellcasting = data.spellcasting
            if (spellcasting != null) {
                val spellText = buildString {
                    append(
                        getString(
                            R.string.spellcasting_ability_template,
                            spellcasting.spellcasting_ability.name
                        )
                    )
                    spellcasting.info.forEach { block ->
                        append("${block.name}:\n")
                        block.desc.forEach { line -> append("• $line\n") }
                        append("\n")
                    }
                }
                binding.spellcasting.text = spellText
            } else {
                binding.spellcasting.text = getString(R.string.no_spellcasting_text)
            }
        }


        val jsonString =
            requireContext().assets.open("class_druid.json").bufferedReader().readText()
        viewModel.loadClassData(jsonString)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
