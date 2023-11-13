package com.prplmnstr.shoppingapp.db.entities

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.prplmnstr.shoppingapp.Utility.Constants
import com.prplmnstr.shoppingapp.model.FavoriteItem
import com.prplmnstr.shoppingapp.model.ProductItem

@Dao
interface FavoriteDao {

    @Insert
    suspend fun insertFavoriteItem(favoriteItem: FavoriteItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(productItems: List<ProductItem>): List<Long>

    @Update
    suspend fun updateFavoriteItem(favoriteItem: FavoriteItem): Int

    @Delete
    suspend fun deleteFavoriteItem(favoriteItem: FavoriteItem): Int

    @Delete
    suspend fun deleteFavoriteItems(favoriteItems: List<FavoriteItem>): Int

    @Query("SELECT * FROM ${Constants.FAVORITE_TABLE}")
    fun getAllProducts(): LiveData<List<FavoriteItem>>
}