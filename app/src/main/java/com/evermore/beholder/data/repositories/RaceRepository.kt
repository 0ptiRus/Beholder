package com.evermore.beholder.data.repositories

import com.evermore.beholder.data.models.Race
import com.evermore.beholder.domain.interfaces.Dnd5eApi

class RaceRepository(private val api: Dnd5eApi) {
    suspend fun getRace(index: String): Race {
        return api.getRace(index)
    }
}