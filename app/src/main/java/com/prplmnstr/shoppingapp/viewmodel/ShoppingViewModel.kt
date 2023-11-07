package com.prplmnstr.shoppingapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prplmnstr.shoppingapp.Utility.Event
import com.prplmnstr.shoppingapp.db.repository.ShoppingRepository


class ShoppingViewModel (
    private val repository: ShoppingRepository,
    private val application: Application
) : ViewModel() {



    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage


}