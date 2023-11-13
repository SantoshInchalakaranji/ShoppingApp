package com.prplmnstr.shoppingapp.di

import android.app.Application
import com.prplmnstr.shoppingapp.Utility.DatabaseInitializer
import com.prplmnstr.shoppingapp.db.AppDatabase
//import dagger.hilt.android.HiltAndroidApp


//@HiltAndroidApp
class MyApplication : Application()
{
    override fun onCreate() {
        super.onCreate()
        initializeDatabase()
    }

    private fun initializeDatabase() {
        val databaseInitializer = DatabaseInitializer(AppDatabase.getInstance(this))
        databaseInitializer.initDataFromJsonFile(this)
    }
}