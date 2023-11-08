package com.prplmnstr.shoppingapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialContainerTransform
import com.prplmnstr.shoppingapp.Utility.CategoryFabAnimationUtility
import com.prplmnstr.shoppingapp.adapter.ProductsRvAdapter
import com.prplmnstr.shoppingapp.databinding.FragmentHomeBinding
import com.prplmnstr.shoppingapp.model.CartItem
import com.prplmnstr.shoppingapp.model.FavoriteItem
import com.prplmnstr.shoppingapp.model.ProductCategory
import com.prplmnstr.shoppingapp.model.ProductItem
import com.prplmnstr.shoppingapp.viewmodel.ShoppingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //To set gradient background to  statusBar and toolbar programmatically.
        val window: Window = requireActivity().window
        window.setBackgroundDrawableResource(R.drawable.toolbar_gradient_bg)
        val customToolbar = binding.customToolbar


        // Set the custom toolbar as the action bar
        (requireActivity() as AppCompatActivity).setSupportActionBar(customToolbar)

        //Hide default title text
        (requireActivity() as AppCompatActivity).getSupportActionBar()
            ?.setDisplayShowTitleEnabled(false);


        binding.viewModel = shoppingViewModel

        // Set the lifecycle owner
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryFabAnimationUtility = CategoryFabAnimationUtility(
            binding,
            MaterialContainerTransform(), requireContext(), view
        )
        binding.fab.setOnClickListener {
            if (binding.fab.isExtended) {
                categoryFabAnimationUtility.expandLayoutAndShowBlurEffect()
            } else {
                categoryFabAnimationUtility.collapseLayoutAndHideBlurEffect()
            }
        }

        binding.blurView.setOnClickListener {
            categoryFabAnimationUtility.collapseLayoutAndHideBlurEffect()
        }

        shoppingViewModel.message.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_LONG).show()
            }
        })


        initRecyclerViews()
        initializeSpinners()
        loadCategories()


    }




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

    private fun addToCart(selectedProduct: ProductItem) {
        val cartItem = CartItem.fromProductItem(selectedProduct)
        if(selectedProduct.quantityInCart==1){
            shoppingViewModel.addItemToCart(cartItem)
        }else{
            shoppingViewModel.updateItemInCart(cartItem)
        }
        shoppingViewModel.updateProductItem(selectedProduct)
    }

    private fun removeFromCart(selectedProduct: ProductItem) {
        val cartItem = CartItem.fromProductItem(selectedProduct)

        if(selectedProduct.quantityInCart==0){
            shoppingViewModel.removeItemFromCart(cartItem)
        }else{
            shoppingViewModel.updateItemInCart(cartItem)
        }
        shoppingViewModel.updateProductItem(selectedProduct)

    }

    private fun addToFavorite(selectedProduct: ProductItem) {
        val favoriteItem = FavoriteItem.fromProductItem(selectedProduct)
        shoppingViewModel.addItemToFavorite(favoriteItem)
       // shoppingViewModel.updateProductItem(selectedProduct)
    }

    private fun removeFromFavorite(selectedProduct: ProductItem) {
        val favoriteItem = FavoriteItem.fromProductItem(selectedProduct)
        shoppingViewModel.removeItemFromFavorite(favoriteItem)
       // shoppingViewModel.updateProductItem(selectedProduct)
    }






    private fun loadCategories() {
        shoppingViewModel.categories.observe(viewLifecycleOwner, Observer { categories ->
            if (categories.isNotEmpty()) {
                for (category in categories) {
                    categoryNameList.add(category.name)
                }

              //fab
                val adapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_expandable_list_item_1, categoryNameList)
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
        spinnerAdapter1!!.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1)
        binding.productTitleSpinner1.setAdapter(spinnerAdapter1)

        //TO set initial selection
        binding.productTitleSpinner1.setSelection(0)


        var lastSelectedPosition: Int = -1 // Initially set to an invalid position

        binding.productTitleSpinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {

               if(isSelectedCategoryIsPresentInOtherRV(binding.productTitleSpinner2,binding.productTitleSpinner3,i)){
                   Toast.makeText(requireContext(), "Selcted Category already present.", Toast.LENGTH_SHORT).show()

                   if (lastSelectedPosition != -1) {
                       binding.productTitleSpinner1.setSelection(lastSelectedPosition)
                   }

               }else{
                   lastSelectedPosition = i // Store the current selection as last selected position
                   loadProductsInRecyclerView(shoppingViewModel.categories.value!![i],rvAdapter1)
               }

            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun isSelectedCategoryIsPresentInOtherRV(spinner1: Spinner,spinner2: Spinner, i: Int): Boolean {

        return spinner1.selectedItemId==i.toLong() || spinner2.selectedItemId==i.toLong()
    }

    private fun setTitleSpinner2() {
        spinnerAdapter2!!.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1)
        binding.productTitleSpinner2.setAdapter(spinnerAdapter2)

        //set initial selection
        binding.productTitleSpinner2.setSelection(1)

        var lastSelectedPosition: Int = -1 // Initially set to an invalid position

        binding.productTitleSpinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if(isSelectedCategoryIsPresentInOtherRV(binding.productTitleSpinner3,binding.productTitleSpinner1,i)){
                    Toast.makeText(requireContext(), "Selcted Category Already Present.", Toast.LENGTH_SHORT).show()

                    if (lastSelectedPosition != -1) {
                        binding.productTitleSpinner2.setSelection(lastSelectedPosition)
                    }

                }else{
                    lastSelectedPosition = i // Store the current selection as last selected position
                    loadProductsInRecyclerView(shoppingViewModel.categories.value!![i],rvAdapter2)
                }

            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }
    private fun setTitleSpinner3() {
        spinnerAdapter3!!.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1)
        binding.productTitleSpinner3.setAdapter(spinnerAdapter3)

        //to set initial selection
        binding.productTitleSpinner3.setSelection(2)

        var lastSelectedPosition: Int = -1 // Initially set to an invalid position

        //load related category products on spinner selection
        binding.productTitleSpinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if(isSelectedCategoryIsPresentInOtherRV(binding.productTitleSpinner1,binding.productTitleSpinner2,i)){
                    Toast.makeText(requireContext(), "Selcted Category Already Present.", Toast.LENGTH_SHORT).show()

                    if (lastSelectedPosition != -1) {
                        binding.productTitleSpinner3.setSelection(lastSelectedPosition)
                    }

                }else{
                    lastSelectedPosition = i // Store the current selection as last selected position
                    loadProductsInRecyclerView(shoppingViewModel.categories.value!![i],rvAdapter3)
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun initializeSpinners(){
        // set spinner
        spinnerAdapter1 = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            categoryNameList
        )
        spinnerAdapter2 = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            categoryNameList
        )

        spinnerAdapter3 = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            categoryNameList
        )
    }


    private fun loadProductsInRecyclerView(productCategory: ProductCategory, rvAdapter:ProductsRvAdapter) {

        shoppingViewModel.loadProductsByCategory(productCategory)
            .observe(viewLifecycleOwner, Observer { productItems ->
                if (!productItems.isNullOrEmpty()) {
                    rvAdapter.clear() // Clear existing list
                    rvAdapter.setList(productItems)

                    rvAdapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(requireContext(), "Empty recycler1Products", Toast.LENGTH_SHORT)
                        .show()
                }
            })



    }


}


