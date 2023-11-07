package com.prplmnstr.shoppingapp.Utility.json

data class SourceDataFromJson(
    val categories: List<Category>,
    val error: Any,
    val message: String,
    val status: Boolean
)