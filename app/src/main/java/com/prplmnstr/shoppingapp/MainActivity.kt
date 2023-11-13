package com.prplmnstr.shoppingapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.prplmnstr.shoppingapp.Utility.DatabaseInitializer
import com.prplmnstr.shoppingapp.databinding.ActivityMainBinding
import com.prplmnstr.shoppingapp.db.AppDatabase
import com.prplmnstr.shoppingapp.repository.ShoppingRepository
import com.prplmnstr.shoppingapp.viewmodel.ShoppingViewModel
import com.prplmnstr.shoppingapp.viewmodel.ShoppingViewModelFactory

/**
 * Main entry point for the Shopping App.
 * This activity serves as the host for the navigation graph and initializes essential components.
 */
class MainActivity : AppCompatActivity() {


    // View Binding instance for the activity
    private lateinit var binding: ActivityMainBinding

    // Navigation Controller for managing navigation within the app
    private lateinit var navController: NavController

    // ViewModel instance for handling data and UI logic
    lateinit var shoppingViewModel: ShoppingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize and populate the database with initial data from JSON file
        val databaseInitializer = DatabaseInitializer(AppDatabase.getInstance(this))
        databaseInitializer.initDataFromJsonFile(this)

        // Retrieve DAO instances from the AppDatabase
        val productCategoryDao = AppDatabase.getInstance(application).productCategoryDao
        val ProductItemDao = AppDatabase.getInstance(application).productItemDao
        val favoriteDao = AppDatabase.getInstance(application).favoriteDao
        val cartDao = AppDatabase.getInstance(application).cartDao

        // Create a ShoppingRepository instance with DAO instances
        val shoppingRepository =
            ShoppingRepository(productCategoryDao, ProductItemDao, cartDao, favoriteDao)
        val factory = ShoppingViewModelFactory(shoppingRepository, application)

        // Initialize the ShoppingViewModel using the ViewModelProvider with the factory
        shoppingViewModel = ViewModelProvider(this, factory)[ShoppingViewModel::class.java]

        // Initialize the Navigation Controller with the navHostFragment
        navController = findNavController(R.id.navHostFragment)
    }
}