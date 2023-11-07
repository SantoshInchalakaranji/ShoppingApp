package com.prplmnstr.shoppingapp.Utility

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.prplmnstr.shoppingapp.model.ProductItem

class TypeConverter {

    @TypeConverter
    fun fromProductItem(productItem: ProductItem): String {
        // Convert ProductItem to JSON or another suitable format
        return Gson().toJson(productItem)
    }

    @TypeConverter
    fun toProductItem(json: String): ProductItem {
        // Convert the stored JSON back to ProductItem
        return Gson().fromJson(json, ProductItem::class.java)
    }
}