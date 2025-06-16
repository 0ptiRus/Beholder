package com.evermore.beholder.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evermore.beholder.data.dto.MonsterResult
import com.evermore.beholder.data.repositories.MonsterRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BestiarySearchViewModel(
    private val repository: MonsterRepository
) : ViewModel() {

    private val _filteredMonsters = MutableLiveData<List<MonsterResult>>()
    val filteredMonsters: LiveData<List<MonsterResult>> = _filteredMonsters
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var searchJob: Job? = null
    private var lastQuery: String? = null

    init {
        loadMonsters()
    }

    private fun loadMonsters() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val monsters = repository.getMonsters(null)
                _filteredMonsters.postValue(monsters.results)
                lastQuery = null
            } catch (e: Exception) {
                e.printStackTrace()
                _filteredMonsters.postValue(emptyList())
            } finally {
                _isLoading.postValue(false)
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
            _isLoading.postValue(true)
            try {
                val challengeRatingParam = inputCr.ifBlank { null }
                val monsters = repository.getMonsters(challengeRatingParam)
                _filteredMonsters.postValue(monsters.results)
            } catch (e: Exception) {
                e.printStackTrace()
                _filteredMonsters.postValue(emptyList())
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}