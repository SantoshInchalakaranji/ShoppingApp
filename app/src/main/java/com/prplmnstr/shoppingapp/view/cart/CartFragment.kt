package com.prplmnstr.shoppingapp.view.cart

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.prplmnstr.shoppingapp.R
import com.prplmnstr.shoppingapp.adapter.CartItemAdapter
import com.prplmnstr.shoppingapp.databinding.FragmentCartBinding
import com.prplmnstr.shoppingapp.model.CartItem
import com.prplmnstr.shoppingapp.model.FavoriteItem
import com.prplmnstr.shoppingapp.model.ProductItem
import com.prplmnstr.shoppingapp.viewmodel.ShoppingViewModel

/**
 * Fragment for managing and displaying the user's shopping cart.
 */
class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private val shoppingViewModel: ShoppingViewModel by activityViewModels()
    private lateinit var rvAdapter: CartItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)

        // Clear FLAG_TRANSLUCENT_STATUS flag to enable drawing behind the status bar
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        binding.viewModel = shoppingViewModel

        // Set the lifecycle owner
        binding.lifecycleOwner = this
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Observe messages from the ViewModel
        shoppingViewModel.message.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        })

        // Navigate to the home fragment when the "Empty Cart" button or "Back" button is clicked
        binding.emptyCartButton.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_homeFragment)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_homeFragment)
        }

        // Initialize RecyclerView and load cart items
        initRecyclerView()
        loadCartItems()
    }

    /**
     * Load cart items from the ViewModel and update the UI.
     */
    private fun loadCartItems() {
        shoppingViewModel.cartItems.observe(viewLifecycleOwner, Observer { cartItems ->
            if (cartItems.isNullOrEmpty()) {
                binding.parentLayout.visibility = View.GONE
                binding.emptyCartLayout.visibility = View.VISIBLE
            } else {
                rvAdapter.setList(shoppingViewModel.cartItems.value!!)
                rvAdapter.notifyDataSetChanged()

                binding.subtotalTv.text = shoppingViewModel.calculateOverallTotal()
                binding.totalTv.text = shoppingViewModel.calculateFinalAmount()
            }
        })

    }

    private fun initRecyclerView() {

        //rv 1
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvAdapter =
            CartItemAdapter(
                requireActivity(),
                { selectedProduct: CartItem -> addToCart(selectedProduct) },
                { selectedProduct: CartItem -> removeFromCart(selectedProduct) },
                { selectedProduct: List<CartItem> -> removeMultipleFromCart(selectedProduct) }
            )
        binding.recyclerView.adapter = rvAdapter
    }

    /**
     *  cart item related operations
     */
    private fun addToCart(selectedProduct: CartItem) {
        shoppingViewModel.updateItemInCart(selectedProduct)
        updateProductItem(selectedProduct)
        ifItemPresentInFavoriteThenUpdateInFavorite(selectedProduct)

    }

    private fun updateProductItem(selectedProduct: CartItem) {
        val productItem = ProductItem.fromCartItem(selectedProduct)
        shoppingViewModel.updateProductItem(productItem)
    }

    private fun ifItemPresentInFavoriteThenUpdateInFavorite(selectedProduct: CartItem) {
        if (selectedProduct.isFavorite) {
            val favoriteItem = FavoriteItem.fromCartItem(selectedProduct)
            shoppingViewModel.updateFavoriteItem(favoriteItem)
        }


    }

    private fun removeFromCart(selectedProduct: CartItem) {
        if (selectedProduct.quantityInCart == 0) {
            rvAdapter.removeItem(selectedProduct)

            // delayed the live data so that remove animation should complete
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                shoppingViewModel.removeItemFromCart(selectedProduct)
            }, 500)

        } else {
            shoppingViewModel.updateItemInCart(selectedProduct)
        }
        updateProductItem(selectedProduct)
        ifItemPresentInFavoriteThenUpdateInFavorite(selectedProduct)
    }



    private fun removeMultipleFromCart(selectedProducts: List<CartItem>) {
        val proudcutsToUpdate = arrayListOf<CartItem>()
        proudcutsToUpdate.addAll(selectedProducts)
        var message: String
        if (selectedProducts.size == 1) {
            message = "Item removed from favorite"
        } else {
            message = "Items removed from favorite"

        }
        for (item in selectedProducts) {

            item.quantityInCart = 0
            updateProductItem(item)
            ifItemPresentInFavoriteThenUpdateInFavorite(item)
            rvAdapter.removeItem(item)
        }

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            shoppingViewModel.removeItemsFromCart(proudcutsToUpdate)
        }, 500)

        showSnackBar(message)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            requireView(),
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_cartFragment_to_homeFragment)
        }


    }

}