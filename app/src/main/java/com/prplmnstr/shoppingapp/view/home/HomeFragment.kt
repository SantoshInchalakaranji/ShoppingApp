package com.prplmnstr.shoppingapp.view.home

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prplmnstr.shoppingapp.R
import com.prplmnstr.shoppingapp.adapter.ProductsRvAdapter
import com.prplmnstr.shoppingapp.databinding.FragmentHomeBinding
import com.prplmnstr.shoppingapp.model.CartItem
import com.prplmnstr.shoppingapp.model.FavoriteItem
import com.prplmnstr.shoppingapp.model.ProductCategory
import com.prplmnstr.shoppingapp.model.ProductItem
import com.prplmnstr.shoppingapp.viewmodel.ShoppingViewModel

class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding
    private val shoppingViewModel: ShoppingViewModel by activityViewModels()

    //3 rv adapters for 3 recyclerviews
    private lateinit var rvAdapter1: ProductsRvAdapter
    private lateinit var rvAdapter2: ProductsRvAdapter
    private lateinit var rvAdapter3: ProductsRvAdapter

    //3 spinner adapters for 3 title spinners of category
    private lateinit var spinnerAdapter1: ArrayAdapter<String>
    private lateinit var spinnerAdapter2: ArrayAdapter<String>
    private lateinit var spinnerAdapter3: ArrayAdapter<String>

    val categoryNameList = mutableListOf<String>()
    private lateinit var loader: Dialog



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //To set gradient background to  statusBar and toolbar programmatically.



        val customToolbar = binding.customToolbar
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // Set the custom toolbar as the action bar
        (requireActivity() as AppCompatActivity).setSupportActionBar(customToolbar)

        //Hide default title text
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)


        // Set up data binding
        binding.viewModel = shoppingViewModel
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize loader and execute delayed functions
        initializeLoader()
        displayDialogFor300ms()

        // Set up FAB animation
        setFabAnimation()

        // Set up click listeners
        binding.fab.setOnClickListener {
            if (binding.fab.isExtended) {
                //categoryFabAnimationUtility.expandLayoutAndShowBlurEffect()
                binding.fab.shrink()
            } else {
                // categoryFabAnimationUtility.collapseLayoutAndHideBlurEffect()
                binding.fab.extend()
            }
        }

        binding.blurView.setOnClickListener {
            binding.fab.extend()
        }

        binding.cartImage.setOnClickListener {

            findNavController().navigate(R.id.action_homeFragment_to_cartFragment)

        }

        binding.favoriteImage.setOnClickListener {

            findNavController().navigate(R.id.action_homeFragment_to_favoriteFragment)

        }

        // Observe status messages from ViewModel
        shoppingViewModel.message.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        })

    }

    /**
     * Set up the FAB animation listeners.
     */
    private fun setFabAnimation() {
        // Add listener for FAB extend animation
        binding.fab.addOnExtendAnimationListener(object : AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                binding.fab.setIconResource(R.drawable.category)
                binding.fab.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.black_gradient_1)
                binding.hiddenLayout.visibility = View.GONE
                binding.blurView.visibility = View.GONE
            }

            override fun onAnimationEnd(animation: Animator) {

            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }

        })
        // Add listener for FAB shrink animation
        binding.fab.addOnShrinkAnimationListener(object : AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                binding.fab.setIconResource(R.drawable.close)
                binding.fab.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.orange)
                binding.hiddenLayout.visibility = View.VISIBLE
                binding.blurView.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator) {

            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
    }



    private fun displayDialogFor300ms() {

        loader.show()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            executeDelayedFunctions()
            loader.dismiss()
        }, 500)
    }

    private fun executeDelayedFunctions() {

        initRecyclerViews()
        initializeSpinners()
        loadCategories()
        updateCartItemNumber()
        displaySpinners()
    }

    private fun displaySpinners() {
        binding.nestedScrollView.visibility = View.VISIBLE
    }

    /**
     * Initialize the loader dialog for displaying a loading indicator.
     */

    private fun initializeLoader() {
        loader = Dialog(requireContext())
        loader.setContentView(R.layout.loader)
        loader.window?.setBackgroundDrawableResource(R.color.transparent)
        loader.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        loader.setCancelable(false)
        loader.window?.attributes?.windowAnimations = R.style.loader_animation
    }

    /**
     * Initialize the RecyclerViews for displaying products.
     */
    private fun initRecyclerViews() {

        //rv 1
        binding.recyclerView1.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvAdapter1 =
            ProductsRvAdapter({ selectedProduct: ProductItem -> addToFavorite(selectedProduct) },
                { selectedProduct: ProductItem -> removeFromFavorite(selectedProduct) },
                { selectedProduct: ProductItem -> addToCart(selectedProduct) },
                { selectedProduct: ProductItem -> removeFromCart(selectedProduct) })
        binding.recyclerView1.adapter = rvAdapter1


        //rv2
        binding.recyclerView2.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvAdapter2 =
            ProductsRvAdapter({ selectedProduct: ProductItem -> addToFavorite(selectedProduct) },
                { selectedProduct: ProductItem -> removeFromFavorite(selectedProduct) },
                { selectedProduct: ProductItem -> addToCart(selectedProduct) },
                { selectedProduct: ProductItem -> removeFromCart(selectedProduct) })
        binding.recyclerView2.adapter = rvAdapter2


        //rv3
        binding.recyclerView3.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvAdapter3 =
            ProductsRvAdapter({ selectedProduct: ProductItem -> addToFavorite(selectedProduct) },
                { selectedProduct: ProductItem -> removeFromFavorite(selectedProduct) },
                { selectedProduct: ProductItem -> addToCart(selectedProduct) },
                { selectedProduct: ProductItem -> removeFromCart(selectedProduct) })
        binding.recyclerView3.adapter = rvAdapter3
    }


    /**
     * Show the number of items in cart at the corner textview
     */
    private fun updateCartItemNumber() {
        shoppingViewModel.cartItems.observe(viewLifecycleOwner, Observer { cartItems ->
            if (cartItems.isNullOrEmpty()) {
                binding.cartItemCountTv.visibility = View.GONE
            } else {
                if (binding.cartItemCountTv.visibility == View.GONE || binding.cartItemCountTv.visibility == View.INVISIBLE) {
                    binding.cartItemCountTv.visibility = View.VISIBLE
                    binding.cartItemCountTv.startAnimation(
                        AnimationUtils.loadAnimation(
                            context, R.anim.popup_animation
                        )
                    )
                }

                binding.cartItemCountTv.text = cartItems.size.toString()
            }
        })
    }


    /**
     * Add a product to the cart.
     * @param selectedProduct The selected product item.
     */
    private fun addToCart(selectedProduct: ProductItem) {
        val cartItem = CartItem.fromProductItem(selectedProduct)
        if (selectedProduct.quantityInCart == 1) {
            shoppingViewModel.addItemToCart(cartItem)
        } else {
            shoppingViewModel.updateItemInCart(cartItem)
        }
        shoppingViewModel.updateProductItem(selectedProduct)
        ifItemPresentInFavoriteThenUpdateInFavorite(selectedProduct)
    }


    /**
     * Remove a product from the cart.
     * @param selectedProduct The selected product item.
     */
    private fun removeFromCart(selectedProduct: ProductItem) {
        val cartItem = CartItem.fromProductItem(selectedProduct)

        if (selectedProduct.quantityInCart == 0) {
            shoppingViewModel.removeItemFromCart(cartItem)
        } else {
            shoppingViewModel.updateItemInCart(cartItem)
        }
        shoppingViewModel.updateProductItem(selectedProduct)
        ifItemPresentInFavoriteThenUpdateInFavorite(selectedProduct)

    }

    /**
     * Add a product to favorites.
     * @param selectedProduct The selected product item.
     */
    private fun addToFavorite(selectedProduct: ProductItem) {
        val favoriteItem = FavoriteItem.fromProductItem(selectedProduct)
        shoppingViewModel.addItemToFavorite(favoriteItem)
        shoppingViewModel.updateProductItem(selectedProduct)
        ifItemPresentInCartThenUpdateInCart(selectedProduct)
    }

    /**
     * Remove a product from favorites.
     * @param selectedProduct The selected product item.
     */
    private fun removeFromFavorite(selectedProduct: ProductItem) {
        val favoriteItem = FavoriteItem.fromProductItem(selectedProduct)
        shoppingViewModel.removeItemFromFavorite(favoriteItem)
        shoppingViewModel.updateProductItem(selectedProduct)
        ifItemPresentInCartThenUpdateInCart(selectedProduct)
    }


    /**
     * If the item is present in favorites, update it.
     * @param selectedProduct The selected product item.
     */
    private fun ifItemPresentInFavoriteThenUpdateInFavorite(selectedProduct: ProductItem) {
        if (selectedProduct.isFavorite) {
            val favoriteItem = FavoriteItem.fromProductItem(selectedProduct)
            shoppingViewModel.updateFavoriteItem(favoriteItem)
        }
    }

    /**
     * If the item is present in the cart, update it.
     * @param selectedProduct The selected product item.
     */
    private fun ifItemPresentInCartThenUpdateInCart(selectedProduct: ProductItem) {
        if (selectedProduct.quantityInCart > 0) {
            val cartItem = CartItem.fromProductItem(selectedProduct)
            shoppingViewModel.updateItemInCart(cartItem)
        }
    }


    /**
     * Load product categories and set up spinners.
     */
    private fun loadCategories() {
        shoppingViewModel.categories.observe(viewLifecycleOwner, Observer { categories ->
            if (categories.isNotEmpty()) {
                for (category in categories) {
                    categoryNameList.add(category.name)
                }

                //fab
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_expandable_list_item_1,
                    categoryNameList
                )
                binding.categoriesList.adapter = adapter

                setTitleSpinner1()
                setTitleSpinner2()
                setTitleSpinner3()
            } else {
                Toast.makeText(requireContext(), "empty", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setTitleSpinner1() {
        spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1)
        binding.productTitleSpinner1.adapter = spinnerAdapter1

        //TO set initial selection
        binding.productTitleSpinner1.setSelection(0)


        var lastSelectedPosition: Int = -1 // Initially set to an invalid position

        binding.productTitleSpinner1.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?, view: View, i: Int, l: Long
                ) {
                    view.let {
                        if (isSelectedCategoryIsPresentInOtherRV(
                                binding.productTitleSpinner2, binding.productTitleSpinner3, i
                            )
                        ) {
                            Toast.makeText(
                                requireContext(),
                                "Selcted Category already present.",
                                Toast.LENGTH_SHORT
                            ).show()

                            if (lastSelectedPosition != -1) {
                                binding.productTitleSpinner1.setSelection(lastSelectedPosition)
                            }

                        } else {
                            lastSelectedPosition =
                                i // Store the current selection as last selected position
                            loadProductsInRecyclerView(
                                shoppingViewModel.categories.value!![i],
                                rvAdapter1,
                                binding.recyclerView1
                            )
                        }

                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
    }

    private fun isSelectedCategoryIsPresentInOtherRV(
        spinner1: Spinner, spinner2: Spinner, i: Int
    ): Boolean {

        return spinner1.selectedItemId == i.toLong() || spinner2.selectedItemId == i.toLong()
    }

    private fun setTitleSpinner2() {
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1)
        binding.productTitleSpinner2.adapter = spinnerAdapter2

        //set initial selection
        binding.productTitleSpinner2.setSelection(1)

        var lastSelectedPosition: Int = -1 // Initially set to an invalid position

        binding.productTitleSpinner2.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?, view: View, i: Int, l: Long
                ) {
                    view.let {
                        if (isSelectedCategoryIsPresentInOtherRV(
                                binding.productTitleSpinner3, binding.productTitleSpinner1, i
                            )
                        ) {
                            Toast.makeText(
                                requireContext(),
                                "Selcted Category Already Present.",
                                Toast.LENGTH_SHORT
                            ).show()

                            if (lastSelectedPosition != -1) {
                                binding.productTitleSpinner2.setSelection(lastSelectedPosition)
                            }

                        } else {
                            lastSelectedPosition =
                                i // Store the current selection as last selected position
                            loadProductsInRecyclerView(
                                shoppingViewModel.categories.value!![i],
                                rvAdapter2,
                                binding.recyclerView2
                            )
                        }

                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
    }

    private fun setTitleSpinner3() {
        spinnerAdapter3.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1)
        binding.productTitleSpinner3.adapter = spinnerAdapter3

        //to set initial selection
        binding.productTitleSpinner3.setSelection(2)

        var lastSelectedPosition: Int = -1 // Initially set to an invalid position

        //load related category products on spinner selection
        binding.productTitleSpinner3.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?, view: View, i: Int, l: Long
                ) {
                    view.let {
                        if (isSelectedCategoryIsPresentInOtherRV(
                                binding.productTitleSpinner1, binding.productTitleSpinner2, i
                            )
                        ) {
                            Toast.makeText(
                                requireContext(),
                                "Selcted Category Already Present.",
                                Toast.LENGTH_SHORT
                            ).show()

                            if (lastSelectedPosition != -1) {
                                binding.productTitleSpinner3.setSelection(lastSelectedPosition)
                            }

                        } else {
                            lastSelectedPosition =
                                i // Store the current selection as last selected position
                            loadProductsInRecyclerView(
                                shoppingViewModel.categories.value!![i],
                                rvAdapter3,
                                binding.recyclerView3
                            )
                        }
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
    }

    /**
     * Set up and display spinners for selecting product categories.
     */
    private fun initializeSpinners() {
        // set spinner
        spinnerAdapter1 = ArrayAdapter<String>(
            requireContext(), R.layout.spinner_item, categoryNameList
        )
        spinnerAdapter2 = ArrayAdapter<String>(
            requireContext(), R.layout.spinner_item, categoryNameList
        )

        spinnerAdapter3 = ArrayAdapter<String>(
            requireContext(), R.layout.spinner_item, categoryNameList
        )
    }


    /**
     * Load products in the RecyclerView based on the selected category.
     * @param productCategory The selected product category.
     * @param rvAdapter The RecyclerView adapter.
     * @param recyclerView The RecyclerView.
     */
    private fun loadProductsInRecyclerView(
        productCategory: ProductCategory, rvAdapter: ProductsRvAdapter, recyclerView: RecyclerView
    ) {
        shoppingViewModel.loadProductsByCategory(productCategory)
            .observeOnce(viewLifecycleOwner) { productItems ->
                if (!productItems.isNullOrEmpty()) {

                    rvAdapter.setList(productItems)
                    rvAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Empty recycler1Products", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(value: T) {
                removeObserver(this)
                observer(value)
            }
        })
    }

}




