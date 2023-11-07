package com.prplmnstr.shoppingapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.prplmnstr.shoppingapp.Utility.Constants
import com.prplmnstr.shoppingapp.Utility.TypeConverter
import com.prplmnstr.shoppingapp.db.entities.CartDao
import com.prplmnstr.shoppingapp.db.entities.FavoriteDao
import com.prplmnstr.shoppingapp.db.entities.ProductCategoryDao
import com.prplmnstr.shoppingapp.db.entities.ProductItemDao
import com.prplmnstr.shoppingapp.model.CartItem
import com.prplmnstr.shoppingapp.model.FavoriteItem
import com.prplmnstr.shoppingapp.model.ProductCategory
import com.prplmnstr.shoppingapp.model.ProductItem

@Database(entities = [ProductCategory::class, ProductItem::class, CartItem::class, FavoriteItem::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val productCategoryDao: ProductCategoryDao
    abstract val productItemDao: ProductItemDao
    abstract val cartDao: CartDao
    abstract val favoriteDao: FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        Constants.DATABASE_NAME
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}