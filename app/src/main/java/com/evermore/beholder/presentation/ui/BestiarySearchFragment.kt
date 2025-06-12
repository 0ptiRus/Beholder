// com/evermore/beholder/presentation/ui/BestiarySearchFragment.kt
package com.evermore.beholder.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.evermore.beholder.R
import com.evermore.beholder.databinding.FragmentBestiaryBinding
import com.evermore.beholder.presentation.adapters.BestiaryAdapter
import com.evermore.beholder.presentation.viewmodels.BestiarySearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class BestiarySearchFragment : Fragment() {

    private var _binding: FragmentBestiaryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BestiarySearchViewModel by viewModel()
    private lateinit var bestiaryAdapter: BestiaryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.itemTitle.text = getString(R.string.bestiary_search_title)

        bestiaryAdapter = BestiaryAdapter { entry ->
            val action = BestiarySearchFragmentDirections
                .actionMonsterSelectionToMonsterDetails(entry.index)
            findNavController().navigate(action)
        }
        binding.itemsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.itemsRecyclerView.adapter = bestiaryAdapter

        viewModel.filteredMonsters.observe(viewLifecycleOwner) { monsters ->
            bestiaryAdapter.updateMonsters(monsters)
        }

//        binding.challengeRatingInput.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                viewModel.filterMonstersByCr(s.toString())
//            }
//            override fun afterTextChanged(s: Editable?) {}
//        })

        val raceJsonString =
            requireContext().assets.open("bestiary_search.json").bufferedReader().readText()
        viewModel.loadMonsters(raceJsonString)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}