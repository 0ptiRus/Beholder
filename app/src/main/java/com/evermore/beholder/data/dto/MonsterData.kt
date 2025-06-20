package com.evermore.beholder.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Monster(
    val index: String,
    val name: String,
    val desc: String?,
    val size: String,
    val type: String,
    val subtype: String?,
    val alignment: String,
    @Json(name = "armor_class") val armorClass: List<ArmorClass>?,
    @Json(name = "hit_points") val hitPoints: Int,
    @Json(name = "hit_dice") val hitDice: String,
    @Json(name = "hit_points_roll") val hitPointsRoll: String,
    val speed: Speed?,
    val strength: Int,
    val dexterity: Int,
    val constitution: Int,
    val intelligence: Int,
    val wisdom: Int,
    val charisma: Int,
    val proficiencies: List<CreatureProficiency>?,
    @Json(name = "damage_vulnerabilities") val damageVulnerabilities: List<String>?,
    @Json(name = "damage_resistances") val damageResistances: List<String>?,
    @Json(name = "damage_immunities") val damageImmunities: List<String>?,
    @Json(name = "condition_immunities") val conditionImmunities: List<String>?,
    val senses: Senses?,
    val languages: String?,
    @Json(name = "challenge_rating") val challengeRating: Double,
    @Json(name = "proficiency_bonus") val proficiencyBonus: Int,
    val xp: Int,
    @Json(name = "special_abilities") val specialAbilities: List<SpecialAbility>?,
    val actions: List<Action>?,
    val url: String,
    @Json(name = "updated_at") val updatedAt: String,
    val image: String?,
    val forms: List<Any>?,
    @Json(name = "legendary_actions") val legendaryActions: List<LegendaryAction>?,
    val reactions: List<Reaction>?
)

@JsonClass(generateAdapter = true)
data class ArmorClass(
    val type: String,
    val value: Int,
    val spell: CreatureReferenceItem? = null
)

@JsonClass(generateAdapter = true)
data class Speed(
    val walk: String?,
    @Json(name = "fly") val fly: String? = null,
    @Json(name = "swim") val swim: String? = null
)

@JsonClass(generateAdapter = true)
data class CreatureProficiency(
    val value: Int,
    val proficiency: CreatureReferenceItem
)

@JsonClass(generateAdapter = true)
data class Senses(
    @Json(name = "passive_perception") val passivePerception: Int?

)

@JsonClass(generateAdapter = true)
data class SpecialAbility(
    val name: String,
    val desc: String,

    @Json(name = "spellcasting") val creatureSpellcasting: CreatureSpellcasting? = null,
    val damage: List<DamageInfo>? = null
)

@JsonClass(generateAdapter = true)
data class CreatureSpellcasting(
    val level: Int,
    val ability: CreatureReferenceItem,
    val dc: Int,
    val modifier: Int,
    @Json(name = "components_required") val componentsRequired: List<String>?,
    val school: String?,
    val slots: Map<String, Int>?,
    val spells: List<SpellReference>?
)

@JsonClass(generateAdapter = true)
data class SpellReference(
    val name: String,
    val level: Int,
    val url: String
)

@JsonClass(generateAdapter = true)
data class Action(
    val name: String,
    val desc: String,
    @Json(name = "attack_bonus") val attackBonus: Int? = null,
    val damage: List<DamageInfo>? = null,
    val actions: List<MiscAction>? = null
)

@JsonClass(generateAdapter = true)
data class MiscAction(
    @Json(name = "action_name") val actionName: String,
    val count: Int,
    val type: String
)

@JsonClass(generateAdapter = true)
data class LegendaryAction(
    val name: String,
    val desc: String
)

@JsonClass(generateAdapter = true)
data class Reaction(
    val name: String,
    val desc: String
)

@JsonClass(generateAdapter = true)
data class DamageInfo(
    val choose: Int? = null,
    val type: String? = null,
    val from: OptionSet? = null
)

@JsonClass(generateAdapter = true)
data class OptionSet(
    @Json(name = "option_set_type") val optionSetType: String,
    val options: List<CreatureOption>?
)

@JsonClass(generateAdapter = true)
data class CreatureOption(
    @Json(name = "option_type") val optionType: String,
    @Json(name = "damage_type") val damageType: CreatureReferenceItem? = null,
    @Json(name = "damage_dice") val damageDice: String? = null,
    val notes: String? = null
)

@JsonClass(generateAdapter = true)
data class CreatureReferenceItem(
    val index: String,
    val name: String,
    val url: String
)