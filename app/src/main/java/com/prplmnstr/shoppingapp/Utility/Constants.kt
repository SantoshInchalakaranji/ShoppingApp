package com.prplmnstr.shoppingapp.Utility

import java.text.DecimalFormat

class Constants {
    companion object {

        const val DATABASE_NAME = "shopping_database"
        const val CATEGORY_TABLE = "category"
        const val FAVORITE_TABLE = "favorite"
        const val CART_TABLE = "cart"
        const val PRODUCT_TABLE = "product"
        const val JSON_FILE = "shopping.json"

        val df = DecimalFormat("#.##") // Format to two decimal places
    }
}