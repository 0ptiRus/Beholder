package com.evermore.beholder.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ClassLevel(
    val level: Int,
    @Json(name = "ability_score_bonuses") val abilityScoreBonuses: Int = 0,
    @Json(name = "prof_bonus") val profBonus: Int,
    val features: List<FeatureReference>,
    val spellcasting: SpellcastingLevel?,
    @Json(name = "class_specific") val classSpecific: Map<String, Any?>?
)

data class FeatureReference(
    val index: String,
    val name: String,
    val url: String
)

data class SpellcastingLevel(
    @Json(name = "cantrips_known") val cantripsKnown: Int?,
    @Json(name = "spell_slots_level_1") val spellSlotsLevel1: Int,
    @Json(name = "spell_slots_level_2") val spellSlotsLevel2: Int,
    @Json(name = "spell_slots_level_3") val spellSlotsLevel3: Int,
    @Json(name = "spell_slots_level_4") val spellSlotsLevel4: Int,
    @Json(name = "spell_slots_level_5") val spellSlotsLevel5: Int,
    @Json(name = "spell_slots_level_6") val spellSlotsLevel6: Int?,
    @Json(name = "spell_slots_level_7") val spellSlotsLevel7: Int?,
    @Json(name = "spell_slots_level_8") val spellSlotsLevel8: Int?,
    @Json(name = "spell_slots_level_9") val spellSlotsLevel9: Int?
)
