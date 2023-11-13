package com.prplmnstr.shoppingapp.adapter


import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.prplmnstr.shoppingapp.R
import com.prplmnstr.shoppingapp.Utility.Constants
import com.prplmnstr.shoppingapp.databinding.CartItemBinding
import com.prplmnstr.shoppingapp.model.CartItem
import com.squareup.picasso.Picasso

/**
 * Adapter for RecyclerView to display a list of CartItem objects in the shopping cart.
 *
 * @property requireActivity Reference to the hosting FragmentActivity.
 * @property addToCartListener Listener for handling the event when the user adds a product to the cart.
 * @property removeFromCartListener Listener for handling the event when the user removes a product from the cart.
 * @property removeMultipleFromCartClickListener Listener for handling the event when the user removes multiple products from the cart.
 */

class CartItemAdapter(

    val requireActivity: FragmentActivity,
    private val addToCartListener: (CartItem) -> Unit,
    private val removeFromCartListener: (CartItem) -> Unit,
    private val removeMultipleFromCartClickListener: (List<CartItem>) -> Unit,
) : RecyclerView.Adapter<CartItemAdapter.ViewHolder>(), ActionMode.Callback {


    // Indicates whether the multi-selection mode is active
    private var multiSelection = false

    // ActionMode instance for contextual actions
    private lateinit var mActionMode: ActionMode

    // List of selected CartItems in multi-selection mode
    private var selectedProducts = mutableListOf<CartItem>()

    // List of ViewHolder instances for all items in the cart
    private var myViewHolders = arrayListOf<ViewHolder>()

    // List of CartItems to be displayed in the RecyclerView
    private val products = ArrayList<CartItem>()


    /**
     * Creates and returns a ViewHolder object based on the item view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: CartItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.cart_item, parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Add the ViewHolder to the list ,
        myViewHolders.add(holder)

        // Bind data to the ViewHolder
        holder.bind(products[position], addToCartListener, removeFromCartListener)

        /** long click **/
        holder.binding.parentCard.setOnLongClickListener {
            if (!multiSelection) {
                // Start multi-selection mode
                multiSelection = true
                requireActivity.startActionMode(this)
                // Apply selection to the clicked item
                applySelection(holder, products[position])
                true
            } else {
                // End multi-selection mode if already active
                multiSelection = false
                false
            }
        }
        holder.binding.parentCard.setOnClickListener {
            if (multiSelection) {
                // Apply selection to the clicked item
                applySelection(holder, products[position])
            }
        }

    }

    /**
     * Set the list of CartItems in the adapter.
     */
    fun setList(items: List<CartItem>) {
        products.clear()
        products.addAll(items)
    }

    /**
     * Remove a specific CartItem from the adapter.
     */
    fun removeItem(cartItem: CartItem) {
        val position = products.indexOf(cartItem)
        products.remove(cartItem)
        notifyItemRemoved(position)
    }


    /**
     * ViewHolder class for the adapter.
     */
    class ViewHolder(val binding: CartItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind data to the ViewHolder.
         */
        fun bind(
            product: CartItem,
            addToCartListener: (CartItem) -> Unit,
            removeFromCartListener: (CartItem) -> Unit
        ) {

            // Load product image using Picasso library
            Picasso.get().load(product.icon).into(binding.productImage)

            // Set product price
            binding.productPrice.text = "₹" + product.price.toString() + "/"

            // Set product name with ellipsis if it exceeds 15 characters
            if (product.name.length > 15) {
                binding.productName.text = product.name.substring(0, 15) + "..."
            } else {
                binding.productName.text = product.name
            }

            // Set product quantity in the cart
            binding.qty.text = product.quantityInCart.toString()

            // Set total price for the product
            binding.totalPrice.text =
                "₹" + Constants.df.format(calculateSubtotal(product)).toString()


            //Add button and minus button
            binding.addButton.setOnClickListener {
                product.quantityInCart += 1
                binding.qty.text = product.quantityInCart.toString()
                addToCartListener(product)
            }

            binding.removeButton.setOnClickListener {
                product.quantityInCart -= 1
                binding.qty.text = product.quantityInCart.toString()
                removeFromCartListener(product)
            }


        }

        /**
         * Calculate the subtotal for a specific CartItem.
         */
        fun calculateSubtotal(cartItem: CartItem): Double {
            return cartItem.price * cartItem.quantityInCart
        }
    }

    /**
     * Apply selection to a specific item during multi-selection mode.
     */
    private fun applySelection(holder: ViewHolder, cartItem: CartItem) {
        if (selectedProducts.contains(cartItem)) {
            // Remove the item from the selection
            selectedProducts.remove(cartItem)
            changeItemStyle(holder, R.color.white, R.color.transparent)
        } else {
            // Add the item to the selection
            selectedProducts.add(cartItem)
            changeItemStyle(holder, R.color.card_selection_color, R.color.orange)
        }
        // Update the ActionMode title
        applyActionModeTitle()
    }

    /**
     * Change the visual style of an item based on its selection state.
     */
    private fun changeItemStyle(holder: ViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.binding.parentCard.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )
        holder.binding.parentCard.strokeColor = ContextCompat.getColor(requireActivity, strokeColor)
    }

    /**
     * Update the ActionMode title based on the number of selected items.
     */
    private fun applyActionModeTitle() {
        when (selectedProducts.size) {
            0 -> {
                // Finish the ActionMode if no items are selected
                mActionMode.finish()
            }

            1 -> {
                // Set the title for a single selected item
                mActionMode.title = "${selectedProducts.size} item selected"
            }

            else -> {
                // Set the title for multiple selected items
                mActionMode.title = "${selectedProducts.size} items selected"
            }
        }

    }


    /**
     * Called when the ActionMode is created.
     */
    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        // Initialize the ActionMode menu
        mode?.menuInflater?.inflate(R.menu.cart_contextual_menu, menu)
        mActionMode = mode!!
        // Add color to the status bar during multi-selection mode
        addColorToStatusBar(R.color.orange)
        return true
    }


    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    /**
     * Called when the ActionMode delete icon is prepared.
     */
    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete_item) {
            // Handle the deletion of selected items
            removeMultipleFromCartClickListener(selectedProducts)
            // End multi-selection mode
            multiSelection = false
            selectedProducts.clear()
            mode?.finish()
        }
        return true
    }

    /**
     * Called when the ActionMode is destroyed.
     */
    override fun onDestroyActionMode(mode: ActionMode?) {
        // End multi-selection mode
        multiSelection = false
        selectedProducts.clear()
        // Reset the visual style of all items in the cart
        myViewHolders.forEach { holder ->
            changeItemStyle(holder, R.color.white, R.color.transparent)
        }
        // Reset the status bar color
        addColorToStatusBar(R.color.transparent)

    }

    /**
     * Add color to the status bar based on the provided color resource.
     */
    private fun addColorToStatusBar(color: Int) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
        }, 200)

    }



}