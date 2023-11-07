package com.prplmnstr.shoppingapp.Utility

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prplmnstr.shoppingapp.Utility.json.Category
import com.prplmnstr.shoppingapp.Utility.json.Item
import com.prplmnstr.shoppingapp.Utility.json.SourceDataFromJson
import com.prplmnstr.shoppingapp.db.AppDatabase
import com.prplmnstr.shoppingapp.model.ProductCategory
import com.prplmnstr.shoppingapp.model.ProductItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream

class DatabaseInitializer(private val appDatabase: AppDatabase) {


    /**
     * Initializes the database by reading data from a JSON file in the assets folder and populating the database.
     * @param context The application context.
     */
    fun initDataFromJsonFile(context: Context) {
        if (!isDatabaseCreated(context)) {
            // If database doesn't exist, create and populate it
            val jsonString = readJsonFromAssets(context)

            jsonString?.let {

                // Parse JSON data into a data class
                val sourceDataFromJson = parseJsonToDataClass(jsonString)


                // Get all product categories and products from the parsed data
                val allCategories = getProductCategoriesFromDataSource(sourceDataFromJson)
                val allProducts = getAllProductsFromDataSource(sourceDataFromJson)

                // Insert parsed data into the database using coroutines
                CoroutineScope(Dispatchers.IO).launch {

                    appDatabase.productCategoryDao.insertAll(allCategories)
                    appDatabase.productItemDao.insertAll(allProducts)
                }

            }
        }
    }


    /**
     * Reads JSON data from the assets folder.
     * @param context The application context.
     * @return String containing JSON data.
     */
    fun readJsonFromAssets(context: Context): String {
        return try {
            context.assets.open(Constants.JSON_FILE).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            ""
        }
    }


    /**
     * Parses JSON string into SourceDataFromJson data class.
     * @param jsonString JSON data in String format.
     * @return Parsed SourceDataFromJson object.
     */
    fun parseJsonToDataClass(jsonString: String): SourceDataFromJson {
        val gson = Gson()
        return gson.fromJson(jsonString, SourceDataFromJson::class.java)
    }


    /**
     * Retrieves all products from the data source and converts them into ProductItem objects.
     * @param sourceDataFromJson Parsed JSON data.
     * @return List of ProductItem objects.
     */
    private fun getAllProductsFromDataSource(sourceDataFromJson: SourceDataFromJson): List<ProductItem> {

        val productList = mutableListOf<ProductItem>()

        for (category in sourceDataFromJson.categories) {
            for (item in category.items) {
                val productItem = ProductItem(
                    item.id,
                    category.id,
                    item.name,
                    item.icon,
                    item.price
                )

                productList.add(productItem)
            }
        }
        return productList
    }

    /**
     * Retrieves all product categories from the data source and converts them into ProductCategory objects.
     * @param sourceDataFromJson Parsed JSON data.
     * @return List of ProductCategory objects.
     */
    private fun getProductCategoriesFromDataSource(sourceDataFromJson: SourceDataFromJson): List<ProductCategory> {
        val productCategories = mutableListOf<ProductCategory>()
        for (category in sourceDataFromJson.categories) {
            val productCategory = convertCategoryToProductCategory(category)
            productCategories.add(productCategory)
        }
        return productCategories
    }

    /**
     * Checks if the database is already created.
     * @param context The application context.
     * @return Boolean indicating if the database exists.
     */
    private fun isDatabaseCreated(context: Context): Boolean {
        val databasePath = context.getDatabasePath(Constants.DATABASE_NAME)
        return databasePath.exists()
    }


    /**
     * Converts a Category object to a ProductCategory object.
     * @param category Source Category object.
     * @return ProductCategory object.
     */
    fun convertCategoryToProductCategory(category: Category): ProductCategory {
        return ProductCategory(category.id, category.name)
    }

}
