package com.evermore.beholder.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class APIReference(
    val index: String,
    val name: String,
    val url: String
)

@JsonClass(generateAdapter = true)
data class AbilityBonus(
    @Json(name = "ability_score") val abilityScore: APIReference,
    val bonus: Int
)

@JsonClass(generateAdapter = true)
data class AbilityBonusOptionSet(
    val choose: Int,
    val type: String,
    val from: OptionSetFrom
)

@JsonClass(generateAdapter = true)
data class OptionSetFrom(
    @Json(name = "option_set_type") val optionSetType: String,
    val options: List<Option>
)

@JsonClass(generateAdapter = true)
data class Option(
    @Json(name = "option_type") val optionType: String,
    val item: APIReference? = null,
    @Json(name = "ability_score") val abilityScore: APIReference? = null,
    val bonus: Int? = null
)


@JsonClass(generateAdapter = true)
data class Race(
    val index: String,
    val name: String,
    val speed: Int,
    @Json(name = "ability_bonuses") val abilityBonuses: List<AbilityBonus>,
    @Json(name = "ability_bonus_options") val abilityBonusOptions: AbilityBonusOptionSet?,
    val alignment: String,
    val age: String,
    val size: String,
    @Json(name = "size_description") val sizeDescription: String,
    @Json(name = "starting_proficiencies") val startingProficiencies: List<APIReference>,
    @Json(name = "starting_proficiency_options") val startingProficiencyOptions: AbilityBonusOptionSet?,
    val languages: List<APIReference>,
    @Json(name = "language_options") val languageOptions: AbilityBonusOptionSet?,
    @Json(name = "language_desc") val languageDesc: String,
    val traits: List<APIReference>,
    val subraces: List<APIReference>,
    val url: String
)