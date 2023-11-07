package com.prplmnstr.shoppingapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prplmnstr.shoppingapp.db.repository.ShoppingRepository
import java.lang.IllegalArgumentException


class ShoppingViewModelFactory(
    private val repository: ShoppingRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingViewModel::class.java)) {
            return ShoppingViewModel(repository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}