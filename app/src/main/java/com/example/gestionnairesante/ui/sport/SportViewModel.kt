package com.example.gestionnairesante.ui.sport

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SportViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is sport Fragment"
    }
    val text: LiveData<String> = _text
}