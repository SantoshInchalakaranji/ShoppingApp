package com.prplmnstr.shoppingapp.db.entities

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.prplmnstr.shoppingapp.Utility.Constants
import com.prplmnstr.shoppingapp.model.CartItem
import com.prplmnstr.shoppingapp.model.ProductItem

@Dao
interface CartDao {

    @Insert
    suspend fun insertCartItem(cartItem: CartItem): Long

    @Update
    suspend fun updateCartItem(cartItem: CartItem): Int

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem): Int

    @Query("DELETE FROM ${Constants.CART_TABLE} WHERE id IN (:itemIds)")
    suspend fun deleteCartItems(itemIds: List<Int>): Int

    @Query("SELECT * FROM ${Constants.CART_TABLE}")
    fun getAllProducts(): LiveData<List<CartItem>>
}