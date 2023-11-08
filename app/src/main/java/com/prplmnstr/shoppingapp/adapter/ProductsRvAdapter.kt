package com.prplmnstr.shoppingapp.adapter

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.prplmnstr.shoppingapp.R
import com.prplmnstr.shoppingapp.databinding.ProductItemBinding
import com.prplmnstr.shoppingapp.model.ProductItem
import com.squareup.picasso.Picasso


class ProductsRvAdapter(
    private val makeFavoriteClickListener: (ProductItem) -> Unit,
    private val removeFromFavoriteClickListener: (ProductItem) -> Unit,
    private val addToCartListener: (ProductItem) -> Unit,
    private val removeFromCartListener: (ProductItem) -> Unit
) : RecyclerView.Adapter<ProductsRvAdapter.ViewHolder>() {


    private val products = ArrayList<ProductItem>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ProductItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.product_item, parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position], makeFavoriteClickListener, removeFromFavoriteClickListener,addToCartListener,removeFromCartListener)
    }

    fun setList(items: List<ProductItem>) {
        products.clear()
        products.addAll(items)

    }

    fun clear() {
        products.clear()

    }

    class ViewHolder(val binding: ProductItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            product: ProductItem,
            makeFavoriteClickListener: (ProductItem) -> Unit,
            removeFromFavoriteClickListener:  (ProductItem) -> Unit,
            addToCartListener: (ProductItem) -> Unit,
            removeFromCartListener: (ProductItem) -> Unit
        ) {

            Picasso.get().load(product.icon).into(binding.productImage)
            binding.price.text = "₹"+product.price.toString()
            if(product.name.length>15){
                binding.productName.text = product.name.substring(0,15)+"..."
            }else{
                binding.productName.text = product.name
            }

            if(product.quantityInCart>0){
                binding.qty.visibility = View.VISIBLE
                binding.removeButton.visibility = View.VISIBLE
            }
            binding.qty.text = product.quantityInCart.toString()

            if(product.isFavorite){
                binding.favoriteImage.isChecked = true
            }


            //Toggle favorite button
            binding.favoriteImage.setOnClickListener {
                if(!product.isFavorite){
                    binding.likeAnimation.playAnimation()
                    product.isFavorite=true
                    makeFavoriteClickListener(product)
                }
                else{
                    product.isFavorite=false
                    removeFromFavoriteClickListener(product)
                }

            }

            //Add button and minus button
            binding.addButton.setOnClickListener {
                if(product.quantityInCart==0){
                    binding.qty.visibility = View.VISIBLE
                    binding.removeButton.visibility = View.VISIBLE
                }
                product.quantityInCart+=1
                binding.qty.text = product.quantityInCart.toString()
              //  addToCartListener(product)
            }

            binding.removeButton.setOnClickListener {
                if(product.quantityInCart==1){
                    binding.qty.visibility = View.GONE
                    binding.removeButton.visibility = View.GONE
                }

                product.quantityInCart-=1
                binding.qty.text = product.quantityInCart.toString()
              //  removeFromCartListener(product)
            }









//            binding.menuIv.setOnClickListener {
//                var popupMenu = PopupMenu(context, it)
//                val inflater: MenuInflater = popupMenu.menuInflater
//                inflater.inflate(R.menu.edit_delete_menu, popupMenu.menu)
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    popupMenu.setForceShowIcon(true)
//                }
//                popupMenu.show()
//
//                popupMenu.setOnMenuItemClickListener { menuItem ->
//                    when (menuItem.itemId) {
//                        R.id.edit -> {
//                            clickListener(invoice)
//                            true
//                        }
//
//                        R.id.delete -> {
//                            deleteListener(invoice)
//                            true
//                        }
//
//                        else -> {
//                            false
//                        }
//                    }
//                }

           // }
        }

    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        Log.i("TAG", "onViewDetachedFromWindow: ")
        holder.binding.likeAnimation.progress = 0f
        holder.binding.likeAnimation.cancelAnimation()


    }
}
