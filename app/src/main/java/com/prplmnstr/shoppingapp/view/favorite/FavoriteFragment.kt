package com.prplmnstr.shoppingapp.view.favorite

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
import com.prplmnstr.shoppingapp.adapter.FavoriteItemAdapter
import com.prplmnstr.shoppingapp.databinding.FragmentFavoriteBinding
import com.prplmnstr.shoppingapp.model.CartItem
import com.prplmnstr.shoppingapp.model.FavoriteItem
import com.prplmnstr.shoppingapp.model.ProductItem
import com.prplmnstr.shoppingapp.viewmodel.ShoppingViewModel


class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var rvAdapter: FavoriteItemAdapter
    private val shoppingViewModel: ShoppingViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        // Clear FLAG_TRANSLUCENT_STATUS flag to enable drawing behind the status bar
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Observe messages from the ViewModel and show Toast
        shoppingViewModel.message.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        })


        // Navigate to the home fragment when the "Back" button or "Empty Favorites" button is clicked
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_favoriteFragment_to_homeFragment)
        }

        binding.emptyFavoriteButton.setOnClickListener {
            findNavController().navigate(R.id.action_favoriteFragment_to_homeFragment)
        }

        // Initialize RecyclerView and load favorite items
        initRecyclerView()
        loadFavoriteItems()

    }


    /**
     * Load favorite items from the ViewModel and update the UI.
     */
    private fun loadFavoriteItems() {
        shoppingViewModel.favoriteItems.observe(viewLifecycleOwner, Observer { favoriteItems ->
            if (favoriteItems.isNullOrEmpty()) {
                binding.recyclerView.visibility = View.GONE
                binding.emptyFavoriteLayout.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.emptyFavoriteLayout.visibility = View.GONE
                rvAdapter.setList(shoppingViewModel.favoriteItems.value!!)
                rvAdapter.notifyDataSetChanged()

            }
        })

    }

    private fun initRecyclerView() {

        // Initialize RecyclerView with FavoriteItemAdapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvAdapter =
            FavoriteItemAdapter(
                requireActivity(),
                { selectedProduct: FavoriteItem -> removeFromFavorites(selectedProduct) },
                { selectedProducts: List<FavoriteItem> ->
                    removeMultipleFromFavorites(
                        selectedProducts
                    )
                },
                { selectedProduct: FavoriteItem -> addToCart(selectedProduct) },
                { selectedProduct: FavoriteItem -> removeFromCart(selectedProduct) })
        binding.recyclerView.adapter = rvAdapter
    }

    /**
     * Handle removing multiple items from favorites.
     */
    private fun removeMultipleFromFavorites(selectedProducts: List<FavoriteItem>) {
        val proudcutsToUpdate = arrayListOf<FavoriteItem>()
        proudcutsToUpdate.addAll(selectedProducts)
        var message: String
        if (selectedProducts.size == 1) {
            message = "Item removed from favorite"
        } else {
            message = "Items removed from favorite"

        }

        for (item in selectedProducts) {

            item.isFavorite = false
            updateProductItem(item)
            ifItemPresentInCartThenUpdateInCart(item)
            rvAdapter.removeItem(item)
        }

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            shoppingViewModel.removeItemsFromFavorite(proudcutsToUpdate)
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

    /**
     * Handle removing a favorite item and updating the corresponding cart item.
     */
    private fun removeFromCart(selectedProduct: FavoriteItem) {
        if (selectedProduct.quantityInCart == 0) {

            shoppingViewModel.removeItemFromCart(CartItem.fromFavoriteItem(selectedProduct))
        } else {
            shoppingViewModel.updateItemInCart(CartItem.fromFavoriteItem(selectedProduct))
        }
        updateProductItem(selectedProduct)

    }

    /**
     * Handle adding a favorite item to the cart and updating the corresponding product item.
     */
    private fun addToCart(selectedProduct: FavoriteItem) {

        if (selectedProduct.quantityInCart == 1) {
            shoppingViewModel.addItemToCart(CartItem.fromFavoriteItem(selectedProduct))
        } else {
            shoppingViewModel.updateItemInCart(CartItem.fromFavoriteItem(selectedProduct))
        }

        updateProductItem(selectedProduct)
    }

    /**
     * Handle removing a favorite item and updating the corresponding product item.
     */
    private fun removeFromFavorites(selectedProduct: FavoriteItem) {

        rvAdapter.removeItem(selectedProduct)

        // delayed the live data so that remove animation should complete
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            shoppingViewModel.removeItemFromFavorite(selectedProduct)
        }, 500)

        updateProductItem(selectedProduct)
        ifItemPresentInCartThenUpdateInCart(selectedProduct)

    }


    /**
     * Update the corresponding product item when adding or removing a favorite item.
     */
    private fun updateProductItem(selectedProduct: FavoriteItem) {
        val productItem = ProductItem.fromFavoriteItem(selectedProduct)
        shoppingViewModel.updateProductItem(productItem)
    }


    /**
     * If the item is present in the cart, update it.
     */
    private fun ifItemPresentInCartThenUpdateInCart(selectedProduct: FavoriteItem) {
        if (selectedProduct.quantityInCart > 0) {
            val cartItem = CartItem.fromFavoriteItem(selectedProduct)
            shoppingViewModel.updateItemInCart(cartItem)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Handle back press to navigate to the home fragment
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_favoriteFragment_to_homeFragment)

        }


    }


}