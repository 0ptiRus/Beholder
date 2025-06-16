// com/evermore/beholder/presentation/ui/MonsterDetailsFragment.kt

package com.evermore.beholder.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.evermore.beholder.R
import com.evermore.beholder.databinding.FragmentMonsterDetailsBinding
import com.evermore.beholder.presentation.adapters.MonsterDetailsAdapter
import com.evermore.beholder.presentation.viewmodels.MonsterDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MonsterDetailsFragment : Fragment() {

    private var _binding: FragmentMonsterDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MonsterDetailsViewModel by viewModel()
    private lateinit var monsterDetailsAdapter: MonsterDetailsAdapter

    private val args: MonsterDetailsFragmentArgs by navArgs()

    private lateinit var baseImageUrl: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonsterDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        baseImageUrl = getString(R.string.api_url)

        monsterDetailsAdapter = MonsterDetailsAdapter { stringResId, arg ->
            if (arg != null) {
                getString(stringResId, arg)
            } else {
                getString(stringResId)
            }
        }
        binding.monsterDetailsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.monsterDetailsRecyclerView.adapter = monsterDetailsAdapter

        observeImageLoad()
        observeUpdates()
        observeLoading()
        observeErrors()

        val monsterIndex = args.index
        viewModel.loadMonsterDetails(monsterIndex)
    }

    private fun observeImageLoad() {
        viewModel.monsterData.observe(viewLifecycleOwner) { monster ->
            monster?.image?.let { relativeImageUrl ->
                val fullImageUrl = baseImageUrl + relativeImageUrl

                binding.monsterImageView.load(fullImageUrl) {
                    placeholder(android.R.drawable.ic_menu_gallery)
                    error(android.R.drawable.ic_delete)
                }
            } ?: run {
                binding.monsterImageView.visibility = View.GONE
                binding.monsterImageCard.visibility = View.GONE
            }
        }
    }

    private fun observeUpdates() {
        viewModel.monsterDetailItems.observe(viewLifecycleOwner) { items ->
            monsterDetailsAdapter.updateItems(items)
            binding.skeletonLayout.shimmerLayout.stopShimmer()
            binding.skeletonLayout.shimmerLayout.visibility = View.GONE
            binding.monsterDetailsRecyclerView.visibility = View.VISIBLE
            binding.monsterImageCard.visibility = View.VISIBLE
        }
    }

    private fun observeLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.skeletonLayout.shimmerLayout.startShimmer()
                binding.skeletonLayout.shimmerLayout.visibility = View.VISIBLE
                binding.monsterDetailsRecyclerView.visibility = View.GONE
                binding.monsterImageCard.visibility = View.GONE
            }
        }
    }

    private fun observeErrors() {
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                binding.skeletonLayout.shimmerLayout.stopShimmer()
                binding.skeletonLayout.shimmerLayout.visibility = View.GONE
                binding.monsterDetailsRecyclerView.visibility = View.GONE
                binding.monsterImageCard.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.skeletonLayout.shimmerLayout.stopShimmer()
        _binding = null
    }
}