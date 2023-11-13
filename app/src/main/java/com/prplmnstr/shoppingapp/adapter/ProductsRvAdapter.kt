package com.prplmnstr.shoppingapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.prplmnstr.shoppingapp.R
import com.prplmnstr.shoppingapp.databinding.ProductItemBinding
import com.prplmnstr.shoppingapp.model.ProductItem
import com.squareup.picasso.Picasso

/**
 * Adapter for RecyclerView to display a list of ProductItem objects.
 *
 * @property makeFavoriteClickListener Listener for handling the event when the user marks a product as a favorite.
 * @property removeFromFavoriteClickListener Listener for handling the event when the user removes a product from favorites.
 * @property addToCartListener Listener for handling the event when the user adds a product to the cart.
 * @property removeFromCartListener Listener for handling the event when the user removes a product from the cart.
 */

class ProductsRvAdapter(
    private val makeFavoriteClickListener: (ProductItem) -> Unit,
    private val removeFromFavoriteClickListener: (ProductItem) -> Unit,
    private val addToCartListener: (ProductItem) -> Unit,
    private val removeFromCartListener: (ProductItem) -> Unit
) : RecyclerView.Adapter<ProductsRvAdapter.ViewHolder>() {


    // List of products to be displayed in the RecyclerView
    private val products = ArrayList<ProductItem>()


    /**
     * Creates and returns a ViewHolder object based on the item view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ProductItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.product_item, parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(
            products[position],
            makeFavoriteClickListener,
            removeFromFavoriteClickListener,
            addToCartListener,
            removeFromCartListener
        )
    }

    /**
     * Set the list of products in the adapter.
     */
    fun setList(items: List<ProductItem>) {
        products.clear()
        products.addAll(items)


    }


    /**
     * Clear the list of products in the adapter.
     */
    fun clear() {
        products.clear()

    }

    class ViewHolder(val binding: ProductItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind data to the ViewHolder.
         */
        fun bind(
            product: ProductItem,
            makeFavoriteClickListener: (ProductItem) -> Unit,
            removeFromFavoriteClickListener: (ProductItem) -> Unit,
            addToCartListener: (ProductItem) -> Unit,
            removeFromCartListener: (ProductItem) -> Unit
        ) {

            // Load product image using Picasso library
            Picasso.get().load(product.icon).into(binding.productImage)
            // Set product price
            binding.price.text = "₹" + product.price.toString()

            // Set product name with ellipsis if it exceeds 15 characters
            if (product.name.length > 15) {
                binding.productName.text = product.name.substring(0, 15) + "..."
            } else {
                binding.productName.text = product.name
            }

            // Show or hide quantity and remove button based on the product's quantity in the cart
            if (product.quantityInCart > 0) {
                binding.qty.visibility = View.VISIBLE
                binding.removeButton.visibility = View.VISIBLE
            } else {
                binding.qty.visibility = View.GONE
                binding.removeButton.visibility = View.GONE
            }

            // Set product quantity in the cart
            binding.qty.text = product.quantityInCart.toString()

            // Set the state of the favorite button
            binding.favoriteImage.isChecked = product.isFavorite


            //Toggle favorite button
            binding.favoriteImage.setOnClickListener {
                if (!product.isFavorite) {
                    // Play like animation
                    binding.likeAnimation.playAnimation()
                    // Start popup animation
                    binding.favoriteImage.startAnimation(
                        AnimationUtils.loadAnimation(
                            context,
                            R.anim.popup_animation
                        )
                    )
                    product.isFavorite = true
                    makeFavoriteClickListener(product)
                } else {
                    product.isFavorite = false
                    removeFromFavoriteClickListener(product)
                }

            }

            // Add and remove buttons
            binding.addButton.setOnClickListener {
                if (product.quantityInCart == 0) {
                    binding.qty.visibility = View.VISIBLE
                    binding.removeButton.visibility = View.VISIBLE
                }
                product.quantityInCart += 1
                binding.qty.text = product.quantityInCart.toString()
                addToCartListener(product)
            }

            binding.removeButton.setOnClickListener {
                if (product.quantityInCart == 1) {
                    binding.qty.visibility = View.GONE
                    binding.removeButton.visibility = View.GONE
                }

                product.quantityInCart -= 1
                binding.qty.text = product.quantityInCart.toString()
                removeFromCartListener(product)
            }

        }

    }

    /**
     * Called when a view created by this adapter has been detached from its window.
     */
    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        // Reset like animation progress and cancel animation
        holder.binding.likeAnimation.progress = 0f
        holder.binding.likeAnimation.cancelAnimation()


    }
}
