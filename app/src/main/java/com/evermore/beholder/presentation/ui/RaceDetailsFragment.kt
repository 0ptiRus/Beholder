package com.evermore.beholder.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.evermore.beholder.databinding.FragmentRaceDetailsBinding
import com.evermore.beholder.presentation.adapters.RaceDetailsAdapter
import com.evermore.beholder.presentation.viewmodels.RaceDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RaceDetailsFragment : Fragment() {

    private var _binding: FragmentRaceDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RaceDetailsViewModel by viewModel()
    private lateinit var raceDetailsAdapter: RaceDetailsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRaceDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.container) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        raceDetailsAdapter = RaceDetailsAdapter(mutableListOf(), { stringResId, _ ->
            getString(stringResId)
        })
        binding.raceDetailsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.raceDetailsRecyclerView.adapter = raceDetailsAdapter

        viewModel.raceDetailItems.observe(viewLifecycleOwner) { items ->
            raceDetailsAdapter.updateItems(items)
        }

        val raceJsonString =
            requireContext().assets.open("race.json").bufferedReader().readText()
        viewModel.loadRaceData(raceJsonString)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onReferenceItemClick(index: String, name: String) {
//        val jsonData = dummyReferenceData[index]
//
//        if (jsonData != null) {
//            val bottomSheet = SkillDetailBottomSheetFragment()
//            val args = Bundle().apply {
//                putString(SkillDetailBottomSheetFragment.ARG_SKILL_INDEX, index)
//                putString(SkillDetailBottomSheetFragment.ARG_SKILL_NAME, name)
//                putString(SkillDetailBottomSheetFragment.ARG_SKILL_JSON_DATA, jsonData)
//            }
//            bottomSheet.arguments = args
//            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
//        } else {
//            // Toast.makeText(requireContext(), "No data for $name (Index: $index)", Toast.LENGTH_SHORT).show()
//        }
//    }
}