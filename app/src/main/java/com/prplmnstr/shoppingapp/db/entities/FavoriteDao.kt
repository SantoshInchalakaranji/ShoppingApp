package com.prplmnstr.shoppingapp.db.entities

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.prplmnstr.shoppingapp.Utility.Constants
import com.prplmnstr.shoppingapp.model.CartItem
import com.prplmnstr.shoppingapp.model.FavoriteItem

@Dao
interface FavoriteDao {

    @Insert
    suspend fun insertFavoriteItem(favoriteItem: FavoriteItem): Long

    @Update
    suspend fun updateFavoriteItem(favoriteItem: FavoriteItem): Int

    @Delete
    suspend fun deleteFavoriteItem(favoriteItem: FavoriteItem): Int

    @Query("DELETE FROM ${Constants.FAVORITE_TABLE} WHERE id IN (:itemIds)")
    suspend fun deleteFavoriteItems(itemIds: List<Int>): Int

    @Query("SELECT * FROM ${Constants.FAVORITE_TABLE}")
    fun getAllProducts(): LiveData<List<FavoriteItem>>
}