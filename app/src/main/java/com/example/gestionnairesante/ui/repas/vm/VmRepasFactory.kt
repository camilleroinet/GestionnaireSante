package com.example.gestionnairesante.ui.repas.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairesante.database.dao.innerRepas.InnerPeriodeRepasRepo
import com.example.gestionnairesante.database.dao.innerPlat.InnerPlatMenuRepo
class VmRepasFactory (private val repo: InnerPlatMenuRepo, private val repo2: InnerPeriodeRepasRepo) : ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom((VmRepas::class.java))){
            return VmRepas(repo, repo2) as T
        }else{
            throw IllegalArgumentException("ViewModel inconnu")
        }
    }
}