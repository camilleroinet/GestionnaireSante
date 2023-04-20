package com.example.gestionnairesante.ui.repas.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairesante.database.dao.innerPlat.InnerPlatMenuRepo

class VmRepasFactory (private val repo: InnerPlatMenuRepo) : ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom((VmRepas::class.java))){
            return VmRepas(repo) as T
        }else{
            throw IllegalArgumentException("ViewModel inconnu")
        }
    }
}