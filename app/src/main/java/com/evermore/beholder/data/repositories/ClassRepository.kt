package com.evermore.beholder.data.repositories

import com.evermore.beholder.data.dto.ClassData
import com.evermore.beholder.data.dto.ClassLevel
import com.evermore.beholder.domain.interfaces.Dnd5eApi

class ClassRepository(private val api: Dnd5eApi) {
    suspend fun getClassDetails(index: String): ClassData {
        return api.getClass(index)
    }

    suspend fun getClassLevels(index: String): List<ClassLevel> {
        return api.getClassLevels(index)
    }

}