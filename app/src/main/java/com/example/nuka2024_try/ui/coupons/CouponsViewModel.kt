package com.example.nuka2024_try.ui.stores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CouponsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is coupons Fragment"
    }
    val text: LiveData<String> = _text
}