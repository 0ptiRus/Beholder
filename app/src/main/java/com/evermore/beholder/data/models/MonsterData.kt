package com.evermore.beholder.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Monster(
    val index: String,
    val name: String,
    val desc: String, // Оставим не-nullable, если всегда есть
    val size: String,
    val type: String,
    val subtype: String?, // Может быть null
    val alignment: String,
    @Json(name = "armor_class") val armorClass: List<ArmorClass>?, // Сделали nullable!
    @Json(name = "hit_points") val hitPoints: Int,
    @Json(name = "hit_dice") val hitDice: String,
    @Json(name = "hit_points_roll") val hitPointsRoll: String,
    val speed: Speed?, // Сделали nullable, если Speed может отсутствовать
    val strength: Int,
    val dexterity: Int,
    val constitution: Int,
    val intelligence: Int,
    val wisdom: Int,
    val charisma: Int,
    val proficiencies: List<CreatureProficiency>?, // Сделали nullable
    @Json(name = "damage_vulnerabilities") val damageVulnerabilities: List<String>?, // Должно быть List<String>, и может быть null
    @Json(name = "damage_resistances") val damageResistances: List<String>?, // List<String>, может быть null
    @Json(name = "damage_immunities") val damageImmunities: List<String>?, // List<String>, может быть null
    @Json(name = "condition_immunities") val conditionImmunities: List<String>?, // List<String>, может быть null
    val senses: Senses?, // Сделали nullable
    val languages: String?, // Сделали nullable
    @Json(name = "challenge_rating") val challengeRating: Double,
    @Json(name = "proficiency_bonus") val proficiencyBonus: Int,
    val xp: Int,
    @Json(name = "special_abilities") val specialAbilities: List<SpecialAbility>?, // Сделали nullable
    val actions: List<Action>?, // Сделали nullable
    val url: String,
    @Json(name = "updated_at") val updatedAt: String,
    val image: String?,
    val forms: List<Any>?, // Сделали nullable
    @Json(name = "legendary_actions") val legendaryActions: List<LegendaryAction>?,
    val reactions: List<Reaction>?
)

@JsonClass(generateAdapter = true)
data class ArmorClass(
    val type: String,
    val value: Int,
    val spell: CreatureReferenceItem? = null // spell может отсутствовать для других типов AC
)

@JsonClass(generateAdapter = true)
data class Speed(
    val walk: String?, // walk может быть null в некоторых данных
    @Json(name = "fly") val fly: String? = null, // Добавьте другие типы скорости с Json-аннотациями, если они есть
    @Json(name = "swim") val swim: String? = null
)

@JsonClass(generateAdapter = true)
data class CreatureProficiency(
    val value: Int,
    val proficiency: CreatureReferenceItem
)

@JsonClass(generateAdapter = true)
data class Senses(
    @Json(name = "passive_perception") val passivePerception: Int? // Passive perception может быть null
    // Можно добавить другие чувства, если они появятся
)

@JsonClass(generateAdapter = true)
data class SpecialAbility(
    val name: String,
    val desc: String,
    // Если `spellcasting` в JSON, но в Kotlin называется `creatureSpellcasting`
    @Json(name = "spellcasting") val creatureSpellcasting: CreatureSpellcasting? = null, // Сделали nullable
    val damage: List<DamageInfo>? = null // Может быть null
)

@JsonClass(generateAdapter = true)
data class CreatureSpellcasting(
    val level: Int,
    val ability: CreatureReferenceItem,
    val dc: Int,
    val modifier: Int,
    @Json(name = "components_required") val componentsRequired: List<String>?, // Сделали nullable
    val school: String?, // D&D Beyond API иногда возвращает "school" для класса, не школы магии
    val slots: Map<String, Int>?, // Сделали nullable, если слоты могут отсутствовать
    val spells: List<SpellReference>? // Сделали nullable
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
    val damage: List<DamageInfo>? = null, // Сделали nullable
    val actions: List<Action>? = null // Вложенные действия, сделали nullable
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
    val options: List<CreatureOption>? // Сделали nullable
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