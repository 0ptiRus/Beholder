package com.evermore.beholder.presentation.models

interface CollapsibleTextDisplayable {
    val stringResId: Int?
    val content: String
    var isExpanded: Boolean
}