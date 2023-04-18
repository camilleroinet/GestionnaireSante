package com.example.gestionnairesante.database.dao.periode

import com.example.gestionnairesante.database.dao.glycemie.GlycemieDao
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData

class PeriodeRepo (private val dao: PeriodeDao) {
    val allPeriode = dao.getAllPeriode()

    suspend fun insertPeriode(data: PeriodeData): Long{
        return dao.insertPeriode(data)
    }

    suspend fun deletePeriode(data: Int): Int{
        return dao.deletePeriode(data)
    }

    // Permet la recuperation de l'objet qui va etre update
    fun getPeriodeToUpdate(id: Int) : PeriodeData {
        return dao.getPeriodeToUpdate(id)
    }

    suspend fun updatePeriode(id: Int, date: String, heure: String): Int {
        return dao.updatePeriode(id, date, heure)
    }

    fun getLastPeiode(): Int {
        return dao.getLastPeriode()
    }
}