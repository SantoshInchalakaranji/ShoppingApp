package com.prplmnstr.shoppingapp.db.entities

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.prplmnstr.shoppingapp.Utility.Constants
import com.prplmnstr.shoppingapp.model.ProductCategory
import com.prplmnstr.shoppingapp.model.ProductItem

@Dao
interface ProductItemDao {

    @Insert
    suspend fun insertProductItem(productItem: ProductItem): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(productItems: List<ProductItem>): List<Long>

    @Update
    suspend fun updateProductItem(productItem: ProductItem): Int

    @Delete
    suspend fun deleteProductItem(productItem: ProductItem): Int

    @Query("SELECT * FROM ${Constants.PRODUCT_TABLE} WHERE categoryId = :categoryId")
    fun getAllProductsByCategory(categoryId: Int): LiveData<List<ProductItem>>

    @Query("SELECT * FROM ${Constants.PRODUCT_TABLE}")
    fun getAllProducts(): LiveData<List<ProductItem>>
}