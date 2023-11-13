package com.prplmnstr.shoppingapp.model

import androidx.room.Entity
import com.prplmnstr.shoppingapp.Utility.Constants


@Entity(tableName = Constants.FAVORITE_TABLE, primaryKeys = ["id", "categoryId"])
data class FavoriteItem(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val icon: String,
    val price: Double,
    var isFavorite: Boolean = false,
    var quantityInCart: Int = 0
) {
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

        fun fromCartItem(cartItem: CartItem): FavoriteItem {
            return FavoriteItem(
                cartItem.id,
                cartItem.categoryId,
                cartItem.name,
                cartItem.icon,
                cartItem.price,
                cartItem.isFavorite,
                cartItem.quantityInCart
            )
        }


        fun List<FavoriteItem>.toProductItemList(): List<ProductItem> {
            return map { favoriteItem ->
                ProductItem(
                    id = favoriteItem.id,
                    categoryId = favoriteItem.categoryId,
                    name = favoriteItem.name,
                    icon = favoriteItem.icon,
                    price = favoriteItem.price,
                    isFavorite = favoriteItem.isFavorite,
                    quantityInCart = favoriteItem.quantityInCart
                )
            }
        }

        fun List<FavoriteItem>.toCartItemlList(): List<CartItem> {
            return map { favoriteItem ->
                CartItem(
                    id = favoriteItem.id,
                    categoryId = favoriteItem.categoryId,
                    name = favoriteItem.name,
                    icon = favoriteItem.icon,
                    price = favoriteItem.price,
                    isFavorite = favoriteItem.isFavorite,
                    quantityInCart = favoriteItem.quantityInCart
                )
            }
        }

        fun isFavoriteItemInList(
            newItem: FavoriteItem,
            favoriteItemsList: List<FavoriteItem>
        ): Boolean {
            return favoriteItemsList.any { it.id == newItem.id }
        }

    }

}
