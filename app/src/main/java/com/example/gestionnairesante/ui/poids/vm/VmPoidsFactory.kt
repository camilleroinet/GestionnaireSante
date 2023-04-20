package com.example.gestionnairesante.ui.poids.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairesante.database.dao.innerPoids.InnerPoidsRepo

class VmPoidsFactory (private val repo: InnerPoidsRepo) : ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom((VmPoids::class.java))){
            return VmPoids(repo)as T
        }else{
            throw IllegalArgumentException("ViewModel inconnu")
        }
    }
}