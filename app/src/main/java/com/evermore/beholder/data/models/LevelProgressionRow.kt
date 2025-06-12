package com.evermore.beholder.data.models

data class LevelProgressionRow(
    val level: Int,
    val profBonus: Int,
    val features: String, // Combined string of features for simplicity
    val spellSlots: String
)
