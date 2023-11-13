package com.prplmnstr.shoppingapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prplmnstr.shoppingapp.Utility.Constants
import com.prplmnstr.shoppingapp.Utility.Event
import com.prplmnstr.shoppingapp.model.CartItem
import com.prplmnstr.shoppingapp.model.FavoriteItem
import com.prplmnstr.shoppingapp.model.ProductCategory
import com.prplmnstr.shoppingapp.model.ProductItem
import com.prplmnstr.shoppingapp.repository.ShoppingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for handling business logic related to shopping functionality.
 *
 * @property repository The repository responsible for data operations.
 * @property application The application context.
 */

class ShoppingViewModel(
    private val repository: ShoppingRepository,
    private val application: Application
) : ViewModel() {


    // took discount as constant as of now this can be read stored and read from server or database
    val discount = 40.0

    // LiveData for displaying status messages
    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage


    // LiveData for categories, favorite items, and cart items
    val categories = repository.categories
    val favoriteItems = repository.favoriteItems
    val cartItems = repository.cartItems


    /**
     * Load products by a specific category.
     *
     * @param productCategory Product category to filter products.
     * @return LiveData containing a list of ProductItem.
     */
    fun loadProductsByCategory(productCategory: ProductCategory): LiveData<List<ProductItem>> {
        return repository.getProductItemsByCategory(productCategory)

    }

    /**
     * Add a single item to the cart.
     *
     * @param cartItem CartItem to be added.
     */
    fun addItemToCart(cartItem: CartItem) {

        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.addItemToCart(cartItem)
            withContext(Dispatchers.Main) {
                if (result > -1) {
                    statusMessage.value = Event("Item Added To Cart.")

                } else {
                    statusMessage.value = Event("Error whileAdding!")
                }
            }
        }
    }

    /**
     * Remove a single item from the cart.
     *
     * @param cartItem CartItem to be removed.
     */
    fun removeItemFromCart(cartItem: CartItem) {

        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.removeItemFromCart(cartItem)
            withContext(Dispatchers.Main) {
                if (result > -1) {
                    statusMessage.value = Event("Item Removed From Cart.")

                } else {
                    statusMessage.value = Event("Error whileAdding!")
                }
            }
        }
    }

    /**
     * Remove multiple items from the cart.
     *
     * @param cartItems List of CartItem to be removed.
     */
    fun removeItemsFromCart(cartItems: List<CartItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.removeItemsFromCart(cartItems)

        }
    }

    fun addItemToFavorite(favoriteItem: FavoriteItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.addItemToFavorite(favoriteItem)
            withContext(Dispatchers.Main) {
                if (result > -1) {
                    statusMessage.value = Event("Item Added To Favorite.")

                } else {
                    statusMessage.value = Event("Error whileAdding!")
                }
            }
        }
    }

    // Other methods for managing favorite items and updating items in cart...

    fun removeItemFromFavorite(favoriteItem: FavoriteItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.removeItemFromFavorite(favoriteItem)
            withContext(Dispatchers.Main) {
                if (result > -1) {
                    statusMessage.value = Event("Item Removed From Favorite.")

                } else {
                    statusMessage.value = Event("Error whileAdding!")
                }
            }
        }
    }

    fun removeItemsFromFavorite(favoriteItems: List<FavoriteItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.removeItemsFromFavorite(favoriteItems)

        }
    }


    fun updateProductItem(productItem: ProductItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.updateProduct(productItem)
            //use result to show message
        }
    }

    fun updateFavoriteItem(favoriteItem: FavoriteItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.updateItemInFavorite(favoriteItem)
            //use result to show message
        }
    }

    fun updateItemInCart(cartItem: CartItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.updateItemInCart(cartItem)
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


    // Calculate subtotal for a single CartItem
    fun calculateSubtotal(cartItem: CartItem): Double {
        return cartItem.price * cartItem.quantityInCart
    }

    // Calculate subtotal for each item in the list of CartItem
    fun calculateSubtotalForEachItem(cartItems: List<CartItem>): List<Double> {
        return cartItems.map { calculateSubtotal(it) }
    }

    // Calculate overall total of all items in the CartItem list
    fun calculateOverallTotal(): String {
        return "₹" + Constants.df.format(calculateSubtotalForEachItem(cartItems.value!!).sum())
            .toString()
    }

    fun calculateFinalAmount(): String {
        val total = (calculateSubtotalForEachItem(cartItems.value!!).sum()) - discount
        return "₹" + Constants.df.format(total).toString()
    }


}