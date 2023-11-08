package com.prplmnstr.shoppingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.prplmnstr.shoppingapp.Utility.DatabaseInitializer
import com.prplmnstr.shoppingapp.databinding.ActivityMainBinding
import com.prplmnstr.shoppingapp.db.AppDatabase
import com.prplmnstr.shoppingapp.db.entities.ProductItemDao
import com.prplmnstr.shoppingapp.db.repository.ShoppingRepository
import com.prplmnstr.shoppingapp.viewmodel.ShoppingViewModel
import com.prplmnstr.shoppingapp.viewmodel.ShoppingViewModelFactory

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var shoppingViewModel: ShoppingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val databaseInitializer = DatabaseInitializer(AppDatabase.getInstance(this))
        databaseInitializer.initDataFromJsonFile(this)

        val productCategoryDao = AppDatabase.getInstance(application).productCategoryDao
        val ProductItemDao = AppDatabase.getInstance(application).productItemDao
        val favoriteDao = AppDatabase.getInstance(application).favoriteDao
        val cartDao = AppDatabase.getInstance(application).cartDao

        val shoppingRepository = ShoppingRepository(productCategoryDao, ProductItemDao, cartDao, favoriteDao)
        val factory = ShoppingViewModelFactory(shoppingRepository, application)

        shoppingViewModel = ViewModelProvider(this, factory)[ShoppingViewModel::class.java]






        navController = findNavController(R.id.navHostFragment)
    }
}