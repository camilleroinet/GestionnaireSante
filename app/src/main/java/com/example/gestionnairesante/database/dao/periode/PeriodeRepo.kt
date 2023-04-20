package com.example.gestionnairesante.database.dao.periode

class PeriodeRepo(private val dao: PeriodeDao) {
    val allPeriode = dao.getAllPeriode()

    suspend fun insertPeriode(data: PeriodeData) {
        return dao.insertPeriode(data)
    }

    suspend fun deletePeriode(data: Int) {
        return dao.deletePeriode(data)
    }

    // Permet la recuperation de l'objet qui va etre update
    fun getPeriodeToUpdate(id: Int): PeriodeData {
        return dao.getPeriodeToUpdate(id)
    }

    suspend fun updatePeriode(id: Int, date: String, heure: String, periode: String) {
        return dao.updatePeriode(id, date, heure, periode)
    }

    fun getLastPeriode(): Int {
        return dao.getLastPeriode()
    }

}