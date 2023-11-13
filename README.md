# Shopping App

## Overview

This Android application is a shopping app that provides users with categorized shopping options. It utilizes local data from a JSON file, stored using the Room database for offline access. The app follows the MVVM architecture, clean code practices, and includes efficient animations for a smooth user experience.

## Features

1. **Categories Menu and Listing**: Supports expand and collapse behavior, enhancing user navigation.

2. **Favorite Items**: Users can mark or unmark their favorite items, with animated icon feedback.

3. **Shopping Cart**: Users can add or remove multiple items from the cart, with real-time reflection across the app.

4. **Offline Mode**: Utilizes Room database to persist product data, favorite items, and cart items for offline access.

5. **UI/UX Enhancements**: Gradient-themed toolbar, appropriate use of Android UI components, and animations for visual appeal.

## Project Structure

```
app
|-- src
    |-- main
        |-- java/com/prplmnstr/shoppingapp
            |-- adapter
                |-- CartItemAdapter
                |-- FavoriteItemAdapter
                |-- ProductsRvAdapter
            |-- db
                |-- entities
                    |-- CartDao
                    |-- FavoriteDao
                    |-- ProductCategoryDao
                    |-- ProductItemDao
                |-- AppDatabase
            |-- model
                |-- CartItem
                |-- FavoriteItem
                |-- ProductCategory
                |-- ProductItem
            |-- repository
                |-- ShoppingRepository
            |-- utility
                |-- json
                    |-- Category
                    |-- Item
                    |-- SourceDataFromJson
                |-- Constants
                |-- DatabaseInitializer
                |-- Event
            |-- view
                |-- cart
                    |-- CartFragment
                |-- favorite
                    |-- FavoriteFragment
                |-- home
                    |-- HomeFragment
                |-- splash
                    |-- SplashFragment
            |-- viewmodel
                |-- ShoppingViewModel
                |-- ShoppingViewModelFactory
        |-- res
        |-- AndroidManifest.xml
```

## Requirements

- Android Studio 4.1.2 or higher
- Kotlin 1.4.32 or higher

## Installation

1. Clone the repository: `git clone https://github.com/SantoshInchalakaranji/ShoppingApp'
2. Open the project in Android Studio.
3. Build and run the app on an emulator or a physical device.

## Usage

- Navigate through categories, mark favorites, and manage your shopping cart.
- Enjoy a visually appealing and user-friendly shopping experience.

## Possible Upgrades

The current version of the Shopping App has successfully met the specified requirements and provides a smooth user experience. However, there are opportunities for further enhancements and optimizations:

1. **Recycler Efficiency Improvement:**

   - Consider upgrading the RecyclerView implementation with DiffUtil for more efficient item updates. This can optimize UI performance during dynamic data changes.
   - Explore pagination techniques to load and display large datasets more effectively, providing a better user experience.

2. **Dependency Injection:**
   - Implement a dependency injection framework, such as Dagger or Koin, to enhance code modularity and manage dependencies more efficiently.
   - Dependency injection can promote cleaner architecture and facilitate unit testing.
