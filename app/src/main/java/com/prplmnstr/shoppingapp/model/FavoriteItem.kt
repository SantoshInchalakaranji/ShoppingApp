package com.prplmnstr.shoppingapp.model

import androidx.room.Entity
import com.prplmnstr.shoppingapp.Utility.Constants


@Entity(tableName = Constants.FAVORITE_TABLE, primaryKeys = ["id", "categoryId"])
data class FavoriteItem(
    val id: Int,
    val categoryId:Int,
    val name: String,
    val icon: String,
    val price: Double,
    var isFavorite:Boolean = false,
    var quantityInCart: Int = 0
){
    companion object {
        fun fromProductItem(productItem: ProductItem): FavoriteItem {
            return FavoriteItem(
                productItem.id,
                productItem.categoryId,
                productItem.name,
                productItem.icon,
                productItem.price,
                productItem.isFavorite,
                productItem.quantityInCart
            )
        }
    }

}
