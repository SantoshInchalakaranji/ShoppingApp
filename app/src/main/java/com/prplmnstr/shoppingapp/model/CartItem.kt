package com.prplmnstr.shoppingapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prplmnstr.shoppingapp.Utility.Constants

@Entity(tableName = Constants.CART_TABLE, primaryKeys = ["id", "categoryId"])
data class CartItem(
    val id: Int,
    val categoryId:Int,
    val name: String,
    val icon: String,
    val price: Double,
    var isFavorite:Boolean = false,
    var quantityInCart: Int = 0
)
