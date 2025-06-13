// com/evermore/beholder/presentation/viewmodels/BestiarySearchViewModel.kt
package com.evermore.beholder.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evermore.beholder.data.models.MonsterResult
import com.evermore.beholder.data.repositories.MonsterRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BestiarySearchViewModel(
    val repository: MonsterRepository
) : ViewModel() {

    private val _filteredMonsters = MutableLiveData<List<MonsterResult>>()
    val filteredMonsters: LiveData<List<MonsterResult>> = _filteredMonsters

    private var searchJob: Job? = null
    private var lastQuery: String? = null

    init {
        loadMonsters()
    }


    fun loadMonsters() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            try {
                val monsters = repository.getMonsters(null)
                _filteredMonsters.postValue(monsters.results)
                lastQuery = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun filterMonstersByCr(inputCr: String) {
        if (inputCr == lastQuery) {
            return
        }
        lastQuery = inputCr

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            try {
                val challengeRatingParam = if (inputCr.isNotBlank()) inputCr else null
                val monsters = repository.getMonsters(challengeRatingParam)
                _filteredMonsters.postValue(monsters.results)
            } catch (e: Exception) {
                e.printStackTrace()
                _filteredMonsters.postValue(emptyList())
            }
        }
    }
}