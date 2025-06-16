package com.evermore.beholder.presentation.models

sealed class RaceDetailItem {
    data class Header(override val title: String) : RaceDetailItem(), HeaderDisplayable
    data class CollapsibleText(
        override val stringResId: Int,
        override val content: String,
        override var isExpanded: Boolean = true,
    ) : RaceDetailItem(), CollapsibleTextDisplayable


    data class OptionsList(
        val stringResId: Int,
        val header: String,
        val options: List<OptionDisplayData>,
        val chooseCount: Int,
        var isExpanded: Boolean = true
    ) : RaceDetailItem()
}

data class OptionDisplayData(
    val name: String,
    val description: String?,
)