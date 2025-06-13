package com.evermore.beholder.data.repositories

import com.evermore.beholder.data.models.BestiaryResponse
import com.evermore.beholder.data.models.Monster
import com.evermore.beholder.domain.interfaces.Dnd5eApi

class MonsterRepository(private val api: Dnd5eApi) {

    suspend fun getMonsters(challengeRating: String?): BestiaryResponse {
        return api.getMonsters(challengeRating)
    }

    suspend fun getMonsterDetails(index: String): Monster {
        return api.getMonster(index)
    }
}