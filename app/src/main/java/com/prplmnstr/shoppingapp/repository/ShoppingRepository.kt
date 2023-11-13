package com.prplmnstr.shoppingapp.repository

import com.prplmnstr.shoppingapp.db.entities.CartDao
import com.prplmnstr.shoppingapp.db.entities.FavoriteDao
import com.prplmnstr.shoppingapp.db.entities.ProductCategoryDao
import com.prplmnstr.shoppingapp.db.entities.ProductItemDao
import com.prplmnstr.shoppingapp.model.CartItem
import com.prplmnstr.shoppingapp.model.FavoriteItem
import com.prplmnstr.shoppingapp.model.ProductCategory
import com.prplmnstr.shoppingapp.model.ProductItem


//@ActivityRetainedScoped
class ShoppingRepository(
    private val productCategoryDao: ProductCategoryDao,
    private val productItemDao: ProductItemDao,
    private val cartDao: CartDao,
    private val favoriteDao: FavoriteDao
) {


    //live data to load, categories ,products, cart items and favorite items
    val categories = productCategoryDao.getAllCategories()
    fun getProductItemsByCategory(productCategory: ProductCategory) =
        productItemDao.getAllProductsByCategory(productCategory.id)
    val cartItems = cartDao.getAllProducts()
    val favoriteItems = favoriteDao.getAllProducts()


    //Cart related Operations

    suspend fun addItemToCart(cartItem: CartItem) = cartDao.insertCartItem(cartItem)
    suspend fun removeItemFromCart(cartItem: CartItem) = cartDao.deleteCartItem(cartItem)
    suspend fun removeItemsFromCart(cartItems: List<CartItem>) = cartDao.deleteCartItems(cartItems)
    suspend fun updateItemInCart(cartItem: CartItem) = cartDao.updateCartItem(cartItem)


    //Favorite related Oparations
    suspend fun addItemToFavorite(favoriteItem: FavoriteItem) =
        favoriteDao.insertFavoriteItem(favoriteItem)

    suspend fun removeItemFromFavorite(favoriteItem: FavoriteItem) =
        favoriteDao.deleteFavoriteItem(favoriteItem)

    suspend fun removeItemsFromFavorite(favoriteItems: List<FavoriteItem>) =
        favoriteDao.deleteFavoriteItems(favoriteItems)

    suspend fun updateItemInFavorite(favoriteItem: FavoriteItem) =
        favoriteDao.updateFavoriteItem(favoriteItem)


    //update product in product table
    suspend fun updateProduct(productItem: ProductItem) =
        productItemDao.updateProductItem(productItem)


}