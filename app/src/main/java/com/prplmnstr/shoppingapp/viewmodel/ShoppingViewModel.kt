package com.prplmnstr.shoppingapp.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prplmnstr.shoppingapp.Utility.Event
import com.prplmnstr.shoppingapp.Utility.json.Category
import com.prplmnstr.shoppingapp.db.repository.ShoppingRepository
import com.prplmnstr.shoppingapp.model.CartItem
import com.prplmnstr.shoppingapp.model.FavoriteItem
import com.prplmnstr.shoppingapp.model.ProductCategory
import com.prplmnstr.shoppingapp.model.ProductItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ShoppingViewModel (
    private val repository: ShoppingRepository,
    private val application: Application
) :  ViewModel() {



    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage



    val categories = repository.categories
    val favoriteItems = repository.favoriteItems
    val  cartItems = repository.cartItems



  fun loadProductsByCategory(productCategory: ProductCategory): LiveData<List<ProductItem>> {
      return repository.getProductItemsByCategory(productCategory)

    }

    fun addItemToCart(cartItem: CartItem){

        viewModelScope.launch(Dispatchers.IO) {
           val result =  repository.addItemToCart(cartItem)
            withContext(Dispatchers.Main) {
                if (result > -1) {
                    statusMessage.value = Event("Item Added To Cart.")
                    
                } else {
                    statusMessage.value = Event("Error whileAdding!")
                }
            }
        }
    }

    fun removeItemFromCart(cartItem: CartItem){

        viewModelScope.launch(Dispatchers.IO) {
            val result =  repository.removeItemFromCart(cartItem)
            withContext(Dispatchers.Main) {
                if (result > -1) {
                    statusMessage.value = Event("Item Removed To Cart.")

                } else {
                    statusMessage.value = Event("Error whileAdding!")
                }
            }
        }
    }

    fun addItemToFavorite(favoriteItem: FavoriteItem){

        viewModelScope.launch(Dispatchers.IO) {
            val result =  repository.addItemToFavorite(favoriteItem)
            withContext(Dispatchers.Main) {
                if (result > -1) {
                    statusMessage.value = Event("Item Added To Favorite.")

                } else {
                    statusMessage.value = Event("Error whileAdding!")
                }
            }
        }
    }

    fun removeItemFromFavorite(favoriteItem: FavoriteItem){

        viewModelScope.launch(Dispatchers.IO) {
            val result =  repository.removeItemFromFavorite(favoriteItem)

            withContext(Dispatchers.Main) {
                if (result > -1) {
                    statusMessage.value = Event("Item Removed To Favorite.")

                } else {
                    statusMessage.value = Event("Error whileAdding!")
                }
            }
        }
    }

    fun updateProductItem(productItem: ProductItem){

        viewModelScope.launch(Dispatchers.IO) {
            val result =  repository.updateProduct(productItem)
            //use result to show message
        }
    }

    fun updateItemInCart(cartItem: CartItem){

        viewModelScope.launch(Dispatchers.IO) {
            val result =  repository.updateItemInCart(cartItem)
            withContext(Dispatchers.Main) {
                if (result > -1) {
                    //if you want show toast on cart item update
                  //  statusMessage.value = Event("Cart Item Updated.")

                } else {
                   // statusMessage.value = Event("Error whileAdding!")
                }
            }
        }
    }



}