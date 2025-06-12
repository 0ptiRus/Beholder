package com.evermore.beholder.data.models


data class ClassData(
    val index: String,
    val name: String,
    val hit_die: Int,
    val proficiency_choices: List<ProficiencyChoice>,
    val proficiencies: List<Proficiency>,
    val saving_throws: List<Ability>,
    val starting_equipment: List<StartingEquipment>,
    val starting_equipment_options: List<EquipmentOption>,
    val spellcasting: Spellcasting?
)

data class ProficiencyChoice(
    val desc: String,
    val choose: Int,
    val type: String,
    val from: OptionSource
)

data class OptionSource(
    val option_set_type: String,
    val options: List<OptionItem>?
)

data class OptionItem(
    val option_type: String,
    val item: ReferenceItem? = null,
    val choice: ChoiceItem? = null,
    val count: Int? = null,
    val of: ReferenceItem? = null
)

data class ChoiceItem(
    val desc: String,
    val choose: Int,
    val type: String,
    val from: OptionSource
)

data class ReferenceItem(val index: String, val name: String, val url: String)

data class Proficiency(val index: String, val name: String, val url: String)

data class Ability(val index: String, val name: String, val url: String)

data class StartingEquipment(val equipment: ReferenceItem, val quantity: Int)

data class EquipmentOption(
    val desc: String,
    val choose: Int,
    val type: String,
    val from: OptionSource
)

data class Spellcasting(
    val level: Int,
    val spellcasting_ability: ReferenceItem,
    val info: List<SpellcastingInfo>
)

data class SpellcastingInfo(val name: String, val desc: List<String>)
