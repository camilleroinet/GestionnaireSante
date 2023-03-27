package com.example.gestionnairesante.database.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairesante.database.dao.glycemie.GlycemieRepo

class VMGlycemieFactory  (private val repo: GlycemieRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) :T{
        if (modelClass.isAssignableFrom(VMGlycemie::class.java)){
            return VMGlycemie(repo) as T
        }else{
            throw IllegalArgumentException("View Model inconnu")
        }
    }

}