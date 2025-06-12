// com/evermore/beholder/presentation/ui/MonsterDetailsFragment.kt
package com.evermore.beholder.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.evermore.beholder.databinding.FragmentMonsterDetailsBinding
import com.evermore.beholder.presentation.adapters.MonsterDetailsAdapter
import com.evermore.beholder.presentation.viewmodels.MonsterDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MonsterDetailsFragment : Fragment() {

    private var _binding: FragmentMonsterDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MonsterDetailsViewModel by viewModel()
    private lateinit var monsterDetailsAdapter: MonsterDetailsAdapter

    companion object {
        const val ARG_MONSTER_INDEX = "monster_index"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonsterDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Теперь адаптеру снова нужен stringProvider
        monsterDetailsAdapter = MonsterDetailsAdapter { stringResId, arg ->
            if (arg != null) {
                getString(stringResId, arg)
            } else {
                getString(stringResId)
            }
        }
        binding.monsterDetailsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.monsterDetailsRecyclerView.adapter = monsterDetailsAdapter

        viewModel.monsterDetailItems.observe(viewLifecycleOwner) { items ->
            monsterDetailsAdapter.updateItems(items)
        }

        //val monsterIndex = arguments?.getString(ARG_MONSTER_INDEX)
        val monsterIndex = requireContext().assets.open("monster.json").bufferedReader().readText()
        if (monsterIndex != null) {
            viewModel.loadMonsterDetails(monsterIndex)
        } else {
            // TODO: Обработка ошибки, если индекс не передан
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}