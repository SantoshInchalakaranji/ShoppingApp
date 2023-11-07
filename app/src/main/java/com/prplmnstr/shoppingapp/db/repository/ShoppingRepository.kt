package com.prplmnstr.shoppingapp.db.repository

import com.prplmnstr.shoppingapp.db.entities.CartDao
import com.prplmnstr.shoppingapp.db.entities.FavoriteDao
import com.prplmnstr.shoppingapp.db.entities.ProductCategoryDao
import com.prplmnstr.shoppingapp.db.entities.ProductItemDao
import com.prplmnstr.shoppingapp.model.ProductCategory
import com.prplmnstr.shoppingapp.model.ProductItem


//@ActivityRetainedScoped
class ShoppingRepository  (
    private val productCategoryDao: ProductCategoryDao,
    private val productItemDao: ProductItemDao,
    private val cartDao: CartDao,
    private val favoriteDao: FavoriteDao
) {


        val categories = productCategoryDao.getAllCategories()
        fun productItemsByCategory(productCategory: ProductCategory) = productItemDao.getAllProductsByCategory(productCategory.id)
        val cartItems =  cartDao.getAllProducts()
        val favoriteItems =  favoriteDao.getAllProducts()





}