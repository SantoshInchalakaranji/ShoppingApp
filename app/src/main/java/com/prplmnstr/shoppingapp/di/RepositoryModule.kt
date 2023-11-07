package com.prplmnstr.shoppingapp.di

import com.prplmnstr.shoppingapp.db.entities.CartDao
import com.prplmnstr.shoppingapp.db.entities.FavoriteDao
import com.prplmnstr.shoppingapp.db.entities.ProductCategoryDao
import com.prplmnstr.shoppingapp.db.entities.ProductItemDao
import com.prplmnstr.shoppingapp.db.repository.ShoppingRepository
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton


//@Module
//@InstallIn(SingletonComponent::class)
object RepositoryModule {
//    @Provides
//    @Singleton
    fun provideShoppingRepository(
      productCategoryDao: ProductCategoryDao,
        productItemDao: ProductItemDao,
         cartDao: CartDao,
         favoriteDao: FavoriteDao

    ): ShoppingRepository {
        return ShoppingRepository(productCategoryDao, productItemDao, cartDao, favoriteDao)
    }
}