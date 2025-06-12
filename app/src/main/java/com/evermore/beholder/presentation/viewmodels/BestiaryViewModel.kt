// com/evermore/beholder/presentation/viewmodels/BestiarySearchViewModel.kt
package com.evermore.beholder.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evermore.beholder.data.models.MonsterResult
import com.evermore.beholder.data.models.BestiaryResponse
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BestiarySearchViewModel : ViewModel() {

    private val _filteredMonsters = MutableLiveData<List<MonsterResult>>()
    val filteredMonsters: LiveData<List<MonsterResult>> = _filteredMonsters

    private var searchJob: Job? = null
    private var lastQuery: String? = null


    fun loadMonsters(json: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            try {
                val gson = Gson()
                val data = gson.fromJson(json, BestiaryResponse::class.java)
                val monsters =
                _filteredMonsters.postValue(data.results)
                lastQuery = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//    fun filterMonstersByCr(inputCr: String) {
//        if (inputCr == lastQuery) {
//            return
//        }
//        lastQuery = inputCr
//
//        searchJob?.cancel()
//        searchJob = viewModelScope.launch {
//            delay(300)
//            try {
//                val challengeRatingParam = if (inputCr.isNotBlank()) inputCr else null
//                val monsters = repository.getMonsters(challengeRatingParam)
//                _filteredMonsters.postValue(monsters)
//            } catch (e: Exception) {
//                e.printStackTrace()
//                _filteredMonsters.postValue(emptyList())
//            }
//        }
//    }
//    // Методы parseCrToDouble и areCrsEqual больше не нужны, так как ViewModel не фильтрует
}