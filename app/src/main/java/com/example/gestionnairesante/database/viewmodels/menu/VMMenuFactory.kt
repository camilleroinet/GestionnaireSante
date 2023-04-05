package com.example.gestionnairesante.database.viewmodels.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairesante.database.dao.menu.MenuRepo

class VMMenuFactory(private val repo: MenuRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VMMenu::class.java)) {
            return VMMenu(repo) as T
        } else {
            throw IllegalArgumentException("View Model inconnu")
        }
    }

}