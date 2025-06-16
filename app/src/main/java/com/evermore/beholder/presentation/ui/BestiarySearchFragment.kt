package com.evermore.beholder.presentation.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.skeletonLayout.shimmerLayout.startShimmer()
                binding.skeletonLayout.shimmerLayout.visibility = View.VISIBLE
                binding.itemsRecyclerView.visibility = View.GONE
            } else {
                binding.skeletonLayout.shimmerLayout.stopShimmer()
                binding.skeletonLayout.shimmerLayout.visibility = View.GONE
                binding.itemsRecyclerView.visibility = View.VISIBLE
            }
        }

        binding.challengeRatingInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.filterMonstersByCr(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}