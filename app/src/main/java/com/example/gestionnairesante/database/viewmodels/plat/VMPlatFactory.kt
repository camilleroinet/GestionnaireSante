package com.example.gestionnairesante.database.viewmodels.plat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairesante.database.dao.plats.PlatRepo

class VMPlatFactory (private val repo: PlatRepo) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>) :T{
        if (modelClass.isAssignableFrom(VMPLat::class.java)){
            return VMPLat(repo) as T
        }else{
            throw IllegalArgumentException("View Model inconnu")
        }
    }
}