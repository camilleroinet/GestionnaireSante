package com.example.gestionnairesante.database.viewmodels.insuline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairesante.database.dao.insuline.InsulineRepo

class VMInsulineFactory(private val repo: InsulineRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VMInsuline::class.java)) {
            return VMInsuline(repo) as T
        } else {
            throw IllegalArgumentException("View Model inconnu")
        }
    }

}