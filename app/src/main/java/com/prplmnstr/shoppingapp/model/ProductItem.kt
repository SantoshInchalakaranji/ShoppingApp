package com.prplmnstr.shoppingapp.model

import androidx.room.Entity
import com.prplmnstr.shoppingapp.Utility.Constants

@Entity(tableName = Constants.PRODUCT_TABLE, primaryKeys = ["id", "categoryId"])
data class ProductItem(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val icon: String,
    val price: Double,
    var isFavorite: Boolean = false,
    var quantityInCart: Int = 0
) {
    companion object {
        fun fromCartItem(cartItem: CartItem): ProductItem {
            return ProductItem(
                cartItem.id,
                cartItem.categoryId,
                cartItem.name,
                cartItem.icon,
                cartItem.price,
                cartItem.isFavorite,
                cartItem.quantityInCart
            )
        }

        fun fromFavoriteItem(favoriteItem: FavoriteItem): ProductItem {
            return ProductItem(
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
