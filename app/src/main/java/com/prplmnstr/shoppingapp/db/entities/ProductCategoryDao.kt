package com.prplmnstr.shoppingapp.db.entities

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.prplmnstr.shoppingapp.Utility.Constants
import com.prplmnstr.shoppingapp.model.ProductCategory
import com.prplmnstr.shoppingapp.model.ProductItem

@Dao
interface ProductCategoryDao {

    @Insert
    suspend fun insertCategory(productCategory: ProductCategory): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(productCategories: List<ProductCategory>): List<Long>

    @Update
    suspend fun updateCategory(productCategory: ProductCategory): Int

    @Delete
    suspend fun deleteCategory(productCategory: ProductCategory): Int

    @Query("SELECT * FROM ${Constants.CATEGORY_TABLE}")
    fun getAllCategories(): LiveData<List<ProductCategory>>
}