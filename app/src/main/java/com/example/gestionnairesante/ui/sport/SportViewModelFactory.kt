package com.example.gestionnairesante.ui.sport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairesante.database.dao.innerDiabete.InnerDiabeteRepo

class SportViewModelFactory (private val repo: InnerDiabeteRepo) : ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom((SportViewModel::class.java))){
            return SportViewModel(repo) as T
        }else{
            throw IllegalArgumentException("ViewModel inconnu")
        }
    }
}