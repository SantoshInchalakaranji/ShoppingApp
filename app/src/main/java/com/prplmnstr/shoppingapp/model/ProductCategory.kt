package com.prplmnstr.shoppingapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prplmnstr.shoppingapp.Utility.Constants

@Entity(tableName = Constants.CATEGORY_TABLE)
data class ProductCategory(
    @PrimaryKey
    val id: Int,
    val name: String,
)
