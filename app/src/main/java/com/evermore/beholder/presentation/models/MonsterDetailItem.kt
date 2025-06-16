package com.evermore.beholder.presentation.models

sealed class MonsterDetailItem {
    data class Header(override val title: String) : MonsterDetailItem(), HeaderDisplayable
    data class CollapsibleText(
        override val stringResId: Int,
        override val content: String,
        override var isExpanded: Boolean = true,
    ) : MonsterDetailItem(), CollapsibleTextDisplayable

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
        val cantrips: String,
        val spellSlots: List<Pair<Int, Int>>,
        val spellsByLevel: Map<Int, List<String>>
    ) : MonsterDetailItem()

    data class ActionItem(
        val name: String,
        val description: String,
        val attackBonus: Int? = null,
        val damageInfo: String? = null
    ) : MonsterDetailItem()
}