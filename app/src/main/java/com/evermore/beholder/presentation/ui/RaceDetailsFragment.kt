package com.evermore.beholder.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
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

    private val args: RaceDetailsFragmentArgs by navArgs()

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

        raceDetailsAdapter = RaceDetailsAdapter(mutableListOf()) { stringResId, _ ->
            getString(stringResId)
        }
        binding.raceDetailsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.raceDetailsRecyclerView.adapter = raceDetailsAdapter

        viewModel.raceDetailItems.observe(viewLifecycleOwner) { items ->
            raceDetailsAdapter.updateItems(items)
        }

        val raceIndex = args.index

        observeLoadingState()
        observeError()

        viewModel.loadRaceData(raceIndex)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeLoadingState() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.skeletonLayout.shimmerLayout.startShimmer()
                binding.skeletonLayout.shimmerLayout.visibility = View.VISIBLE
                binding.raceDetailsRecyclerView.visibility = View.GONE
            } else {
                binding.skeletonLayout.shimmerLayout.stopShimmer()
                binding.skeletonLayout.shimmerLayout.visibility = View.GONE
                binding.raceDetailsRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun observeError() {
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                binding.skeletonLayout.shimmerLayout.stopShimmer()
                binding.skeletonLayout.shimmerLayout.visibility = View.GONE
                binding.raceDetailsRecyclerView.visibility = View.GONE
                binding.raceDetailsRecyclerView.visibility = View.GONE
            }
        }
    }
}