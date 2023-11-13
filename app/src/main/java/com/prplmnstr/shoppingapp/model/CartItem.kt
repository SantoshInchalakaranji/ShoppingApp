package com.prplmnstr.shoppingapp.model

import androidx.room.Entity
import com.prplmnstr.shoppingapp.Utility.Constants

@Entity(tableName = Constants.CART_TABLE, primaryKeys = ["id", "categoryId"])
data class CartItem(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val icon: String,
    val price: Double,
    var isFavorite: Boolean = false,
    var quantityInCart: Int = 0
) {
    companion object {
        fun fromProductItem(productItem: ProductItem): CartItem {
            return CartItem(
                productItem.id,
                productItem.categoryId,
                productItem.name,
                productItem.icon,
                productItem.price,
                productItem.isFavorite,
                productItem.quantityInCart
            )
        }

        fun fromFavoriteItem(favoriteItem: FavoriteItem): CartItem {
            return CartItem(
                favoriteItem.id,
                favoriteItem.categoryId,
                favoriteItem.name,
                favoriteItem.icon,
                favoriteItem.price,
                favoriteItem.isFavorite,
                favoriteItem.quantityInCart
            )
        }
    }

}

