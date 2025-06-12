// com/evermore/beholder/presentation/viewmodels/ClassDetailsViewModel.kt
package com.evermore.beholder.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evermore.beholder.data.models.ClassData
import com.evermore.beholder.data.models.LevelProgressionRow
import com.evermore.beholder.data.models.SpellcastingLevel
import com.evermore.beholder.data.repositories.ClassRepository
import kotlinx.coroutines.launch

// ViewModel теперь принимает ClassRepository через конструктор
class ClassDetailsViewModel(private val classRepository: ClassRepository) : ViewModel() {

    private val _classData = MutableLiveData<ClassData>()
    val classData: LiveData<ClassData> = _classData

    private val _levelProgression = MutableLiveData<List<LevelProgressionRow>>()
    val levelProgression: LiveData<List<LevelProgressionRow>> get() = _levelProgression

    // Добавим LiveData для обработки состояния загрузки/ошибок
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Новый метод для загрузки всех данных из API
    fun loadDetails(classIndex: String) {
        _isLoading.value = true // Начинаем загрузку
        viewModelScope.launch {
            try {
                // Загружаем данные класса
                val classResponse = classRepository.getClassDetails(classIndex)
                _classData.value = classResponse

                // Загружаем данные уровней
                val levelsResponse = classRepository.getClassLevels(classIndex)
                val progressionRows = levelsResponse.map { levelData ->
                    val featuresText = levelData.features.joinToString("\n") { it.name }
                    val spellSlotsText = formatSpellSlots(levelData.spellcasting)
                    LevelProgressionRow(
                        level = levelData.level,
                        profBonus = levelData.profBonus,
                        features = featuresText,
                        spellSlots = spellSlotsText
                    )
                }
                _levelProgression.value = progressionRows

            } catch (e: Exception) {
                // Обработка ошибок сети или парсинга
                _error.value = "Error loading details: ${e.localizedMessage}"
                println("Error loading details for $classIndex: ${e.stackTraceToString()}") // Для отладки
            } finally {
                _isLoading.value = false // Загрузка завершена (успех или ошибка)
            }
        }
    }

    private fun formatSpellSlots(spellcasting: SpellcastingLevel?): String {
        if (spellcasting == null) return "N/A"

        val slots = mutableListOf<String>()
        if (spellcasting.cantripsKnown > 0) {
            slots.add("Cantrips: ${spellcasting.cantripsKnown}")
        }
        if (spellcasting.spellSlotsLevel1 > 0) {
            slots.add("Lvl 1: ${spellcasting.spellSlotsLevel1}")
        }
        if (spellcasting.spellSlotsLevel2 > 0) {
            slots.add("Lvl 2: ${spellcasting.spellSlotsLevel2}")
        }
        if (spellcasting.spellSlotsLevel3 > 0) {
            slots.add("Lvl 3: ${spellcasting.spellSlotsLevel3}")
        }
        if (spellcasting.spellSlotsLevel4 > 0) {
            slots.add("Lvl 4: ${spellcasting.spellSlotsLevel4}")
        }
        if (spellcasting.spellSlotsLevel5 > 0) {
            slots.add("Lvl 5: ${spellcasting.spellSlotsLevel5}")
        }
        if (spellcasting.spellSlotsLevel6 > 0) {
            slots.add("Lvl 6: ${spellcasting.spellSlotsLevel6}")
        }
        if (spellcasting.spellSlotsLevel7 > 0) {
            slots.add("Lvl 7: ${spellcasting.spellSlotsLevel7}")
        }
        if (spellcasting.spellSlotsLevel8 > 0) {
            slots.add("Lvl 8: ${spellcasting.spellSlotsLevel8}")
        }
        if (spellcasting.spellSlotsLevel9 > 0) {
            slots.add("Lvl 9: ${spellcasting.spellSlotsLevel9}")
        }
        return if (slots.isEmpty()) "—" else slots.joinToString("\n")
    }
}