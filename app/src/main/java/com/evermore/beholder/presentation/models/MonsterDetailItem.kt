package com.evermore.beholder.presentation.models

sealed class MonsterDetailItem {
    data class Header(val title: String) : MonsterDetailItem()
    data class CollapsibleText(
        val stringResId: Int,
        val content: String,
        var isExpanded: Boolean = true,
    ) : MonsterDetailItem()

    // Можно добавить специальные типы для AC, Speeds, Stats и т.д.
    data class StatBlock(
        val str: Int,
        val dex: Int,
        val con: Int,
        val int: Int,
        val wis: Int,
        val cha: Int
    ) : MonsterDetailItem()

    data class Spellcasting(
        val level: Int,
        val ability: String,
        val dc: Int,
        val modifier: Int,
        val cantrips: String, // Строка со всеми заговорами
        val spellSlots: List<Pair<Int, Int>>, // Уровень и количество слотов
        val spellsByLevel: Map<Int, List<String>> // Карта: уровень -> список названий заклинаний
    ) : MonsterDetailItem()

    data class ActionItem(
        val name: String,
        val description: String,
        val attackBonus: Int? = null,
        val damageInfo: String? = null // Для форматированного текста урона
    ) : MonsterDetailItem()
}