package com.evermore.beholder.data.models

sealed class RaceDetailItem {
    data class Header(val title: String) : RaceDetailItem()
    data class CollapsibleText(
        val stringResId: Int,
        val content: String,
        var isExpanded: Boolean = true,
    ) : RaceDetailItem()

    // Возможно, вам понадобится другой тип элемента для отображения списка опций (например, бонусов характеристик или выбора владения)
    data class OptionsList(
        val stringResId: Int,
        val header: String,
        val options: List<OptionDisplayData>, // Новый класс для отображения опций
        val chooseCount: Int,
        var isExpanded: Boolean = true
    ) : RaceDetailItem()
}

data class OptionDisplayData(
    val name: String,
    val description: String?, // Дополнительное описание, если есть
)