package com.example.gestionnairesante.ui.accueil.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairesante.database.dao.innerStats.StatsRepo

class VmAccueilFactory(private val repo: StatsRepo) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T> ): T {
        if(modelClass.isAssignableFrom(VmAccueil::class.java)) {
            return VmAccueil(repo) as T
        } else {
            throw IllegalArgumentException("ViewModel inconnu")
        }
    }
}