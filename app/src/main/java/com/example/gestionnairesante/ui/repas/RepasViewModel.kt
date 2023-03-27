package com.example.gestionnairesante.ui.repas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RepasViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is repas Fragment"
    }
    val text: LiveData<String> = _text
}