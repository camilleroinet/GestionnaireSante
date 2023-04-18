package com.example.gestionnairesante.ui.diabete.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairesante.database.dao.glycemie.GlycemieRepo
import com.example.gestionnairesante.database.dao.innerDiabete.InnerDiabeteRepo

class VMDiabeteFactory(private val repo: InnerDiabeteRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VMDiabete::class.java)) {
            return VMDiabete(repo) as T
        } else {
            throw IllegalArgumentException("View Model inconnu")
        }
    }
}