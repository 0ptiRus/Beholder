package com.evermore.beholder.data.models

import com.google.gson.annotations.SerializedName

data class ClassLevel(
    val level: Int,
    @SerializedName("ability_score_bonuses") val abilityScoreBonuses: Int,
    @SerializedName("prof_bonus") val profBonus: Int,
    val features: List<FeatureReference>,
    val spellcasting: SpellcastingLevel?,
    @SerializedName("class_specific") val classSpecific: ClassSpecificLevel?
)

data class FeatureReference(
    val index: String,
    val name: String,
    val url: String
)

data class SpellcastingLevel(
    @SerializedName("cantrips_known") val cantripsKnown: Int,
    @SerializedName("spell_slots_level_1") val spellSlotsLevel1: Int,
    @SerializedName("spell_slots_level_2") val spellSlotsLevel2: Int,
    @SerializedName("spell_slots_level_3") val spellSlotsLevel3: Int,
    @SerializedName("spell_slots_level_4") val spellSlotsLevel4: Int,
    @SerializedName("spell_slots_level_5") val spellSlotsLevel5: Int,
    @SerializedName("spell_slots_level_6") val spellSlotsLevel6: Int,
    @SerializedName("spell_slots_level_7") val spellSlotsLevel7: Int,
    @SerializedName("spell_slots_level_8") val spellSlotsLevel8: Int,
    @SerializedName("spell_slots_level_9") val spellSlotsLevel9: Int
)

data class ClassSpecificLevel(
    @SerializedName("wild_shape_max_cr") val wildShapeMaxCr: Double,
    @SerializedName("wild_shape_swim") val wildShapeSwim: Boolean,
    @SerializedName("wild_shape_fly") val wildShapeFly: Boolean
)