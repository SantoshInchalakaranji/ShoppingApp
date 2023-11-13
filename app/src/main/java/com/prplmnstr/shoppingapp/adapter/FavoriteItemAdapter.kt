package com.prplmnstr.shoppingapp.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.prplmnstr.shoppingapp.R
import com.prplmnstr.shoppingapp.databinding.FavoriteItemBinding
import com.prplmnstr.shoppingapp.model.FavoriteItem
import com.squareup.picasso.Picasso

/**
 * Adapter for handling the display and interactions of favorite items in a RecyclerView.
 *
 * @property requireActivity The reference to the hosting FragmentActivity.
 * @property removeFromFavoriteClickListener Callback for removing a single item from favorites.
 * @property removeMultipleFromFavoriteClickListener Callback for removing multiple items from favorites.
 * @property addToCartListener Callback for adding a favorite item to the cart.
 * @property removeFromCartListener Callback for removing a favorite item from the cart.
 */
class FavoriteItemAdapter(
    val requireActivity: FragmentActivity,
    private val removeFromFavoriteClickListener: (FavoriteItem) -> Unit,
    private val removeMultipleFromFavoriteClickListener: (List<FavoriteItem>) -> Unit,
    private val addToCartListener: (FavoriteItem) -> Unit,
    private val removeFromCartListener: (FavoriteItem) -> Unit

) : RecyclerView.Adapter<FavoriteItemAdapter.ViewHolder>(), ActionMode.Callback {


    // Flag for multi-selection mode
    private var multiSelection = false

    // ActionMode instance
    private lateinit var mActionMode: ActionMode

    // List to store selected favorite items
    private var selectedProducts = mutableListOf<FavoriteItem>()

    // List to store ViewHolders for selected items
    private var myViewHolders = arrayListOf<ViewHolder>()

    // List to store favorite items
    private val products = ArrayList<FavoriteItem>()


    /**
     * Creates a ViewHolder for the item views.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: FavoriteItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.favorite_item, parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        myViewHolders.add(holder)
        holder.bind(
            products[position],
            removeFromFavoriteClickListener,
            addToCartListener,
            removeFromCartListener
        )

        /**  Long click listener for initiating multi-selection mode **/
        holder.binding.parentCard.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, products[position])
                true
            } else {
                multiSelection = false
                false
            }

        }

        // Click listener for handling item selection in multi-selection mode
        holder.binding.parentCard.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, products[position])
            }
        }
    }

    /**
     * Sets the list of favorite items to be displayed.
     */
    fun setList(items: List<FavoriteItem>) {
        products.clear()
        products.addAll(items)
    }

    /**
     * Removes a single item from the adapter and updates the view.
     */
    fun removeItem(favoriteItem: FavoriteItem) {
        val position = products.indexOf(favoriteItem)
        products.remove(favoriteItem)
        notifyItemRemoved(position)
    }

    /**
     * ViewHolder class for holding references to the views in each item of the RecyclerView.
     */
    class ViewHolder(val binding: FavoriteItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the data to the views in the ViewHolder.
         */
        fun bind(
            product: FavoriteItem,
            removeFromFavoriteClickListener: (FavoriteItem) -> Unit,
            addToCartListener: (FavoriteItem) -> Unit,
            removeFromCartListener: (FavoriteItem) -> Unit
        ) {


            // Load product image using Picasso
            Picasso.get().load(product.icon).into(binding.productImage)

            // Set product details
            binding.productPrice.text = "₹" + product.price.toString()
            if (product.name.length > 15) {
                binding.productName.text = product.name.substring(0, 15) + "..."
            } else {
                binding.productName.text = product.name
            }

            // Set visibility of quantity-related views based on the item's quantity
            if (product.quantityInCart > 0) {
                binding.qty.visibility = View.VISIBLE
                binding.removeButton.visibility = View.VISIBLE
            } else {
                binding.qty.visibility = View.GONE
                binding.removeButton.visibility = View.GONE
            }

            // Set quantity text
            binding.qty.text = product.quantityInCart.toString()

            // Set the favorite status
            binding.favoriteImage.isChecked = true

            // favorite button clicked, remove item from favorite
            binding.favoriteImage.setOnClickListener {
                // Set the item as not favorite and remove it from favorites
                product.isFavorite = false
                removeFromFavoriteClickListener(product)

            }

            // Set click listeners for add and remove buttons
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

    private fun applySelection(holder: ViewHolder, favoriteItem: FavoriteItem) {
        if (selectedProducts.contains(favoriteItem)) {
            selectedProducts.remove(favoriteItem)
            changeItemStyle(holder, R.color.white, R.color.transparent)
        } else {
            selectedProducts.add(favoriteItem)
            changeItemStyle(holder, R.color.card_selection_color, R.color.orange)
        }
        applyActionModeTitle()
    }

    private fun changeItemStyle(holder: ViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.binding.parentCard.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )
        holder.binding.parentCard.strokeColor = ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle() {
        when (selectedProducts.size) {
            0 -> {
                mActionMode.finish()
            }

            1 -> {

                mActionMode.title = "${selectedProducts.size} item selected"
            }

            else -> {
                mActionMode.title = "${selectedProducts.size} items selected"
            }
        }

    }


    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {


        mode?.menuInflater?.inflate(R.menu.cart_contextual_menu, menu)
        mActionMode = mode!!
        addColorToStatusBar(R.color.orange)
        return true
    }

    private fun addColorToStatusBar(color: Int) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
        }, 200)

    }


    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete_item) {
            removeMultipleFromFavoriteClickListener(selectedProducts)
            multiSelection = false
            selectedProducts.clear()
            mode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        multiSelection = false
        selectedProducts.clear()
        myViewHolders.forEach { holder ->
            changeItemStyle(holder, R.color.white, R.color.transparent)

        }
        addColorToStatusBar(R.color.transparent)

    }


}