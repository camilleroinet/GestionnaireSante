package com.example.gestionnairesante.database.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairesante.database.dao.poids.PoidsRepo

class VMPoidsFactory (private val repo: PoidsRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) :T{
        if (modelClass.isAssignableFrom(VMPoids::class.java)){
            return VMPoids(repo) as T
        }else{
            throw IllegalArgumentException("View Model inconnu")
        }
    }

}