package com.prplmnstr.shoppingapp.db.entities

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.prplmnstr.shoppingapp.Utility.Constants
import com.prplmnstr.shoppingapp.model.CartItem
import com.prplmnstr.shoppingapp.model.ProductItem

@Dao
interface CartDao {

    @Insert
    suspend fun insertCartItem(cartItem: CartItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(productItems: List<ProductItem>): List<Long>

    @Update
    suspend fun updateCartItem(cartItem: CartItem): Int

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem): Int

    @Delete
    suspend fun deleteCartItems(cartItems: List<CartItem>): Int

    @Query("SELECT * FROM ${Constants.CART_TABLE}")
    fun getAllProducts(): LiveData<List<CartItem>>
}