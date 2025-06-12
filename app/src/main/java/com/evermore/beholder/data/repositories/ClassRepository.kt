package com.evermore.beholder.data.repositories

import com.evermore.beholder.data.models.ClassData
import com.evermore.beholder.data.models.ClassLevel
import com.evermore.beholder.domain.interfaces.Dnd5eApi

class ClassRepository(private val api: Dnd5eApi) {
    suspend fun getClassDetails(index: String): ClassData {
        return api.getClass(index)
    }

    // Добавляем метод для получения данных об уровнях
    suspend fun getClassLevels(index: String): List<ClassLevel> {
        return api.getClassLevels(index) // Предполагаем, что API возвращает объект с полем 'results'
    }

}