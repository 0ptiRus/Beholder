package com.evermore.beholder.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.evermore.beholder.data.models.ClassData
import com.evermore.beholder.data.models.ClassLevel
import com.evermore.beholder.data.models.LevelProgressionRow
import com.evermore.beholder.data.models.SpellcastingLevel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ClassDetailsViewModel : ViewModel() {

    private val _classData = MutableLiveData<ClassData>()
    val classData: LiveData<ClassData> = _classData

    private val _levelProgression = MutableLiveData<List<LevelProgressionRow>>()
    val levelProgression: LiveData<List<LevelProgressionRow>> get() = _levelProgression


    fun loadClassData(classJson: String) {
        val gson = Gson()
        val data = gson.fromJson(classJson, ClassData::class.java)
        _classData.value = data
    }

    fun loadLevelProgressionData(jsonString: String) {
        val gson = Gson()
        val type = object : TypeToken<List<ClassLevel>>() {}.type
        val classLevels: List<ClassLevel> = gson.fromJson(jsonString, type)

        val progressionRows = classLevels.map { levelData ->
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
