package com.evermore.beholder.data.dto


data class BestiaryResponse(
    val count: Int,
    val results: List<MonsterResult>
)
data class MonsterResult(
    val index: String,
    val name: String,
    val url: String,

)