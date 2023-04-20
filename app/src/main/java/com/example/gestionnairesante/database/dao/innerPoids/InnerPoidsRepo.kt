package com.example.gestionnairesante.database.dao.innerPoids

import com.example.gestionnairesante.database.dao.periode.PeriodeDao
import com.example.gestionnairesante.database.dao.periode.PeriodeData
import com.example.gestionnairesante.database.dao.poids.PoidsDao
import com.example.gestionnairesante.database.dao.poids.PoidsData

class InnerPoidsRepo(
    private var poidsDao: PoidsDao,
    private var periodeDao: PeriodeDao,
    private var innerPoidsDao: InnerPoidsDao
) {
    val allPoids = poidsDao.getAllPoids()
    val allValeurPoids = poidsDao.getAllValeurPoids()
    val innerPoidsPeriode = innerPoidsDao.getAllPoids()

    suspend fun insertPoids(poids: PoidsData) {
        return poidsDao.insertPoids(poids)
    }

    suspend fun insertPeriode(periode: PeriodeData) {
        return periodeDao.insertPeriode(periode)
    }

    suspend fun insertPoidsInner(data: InnerPoidsData) {
        return innerPoidsDao.insertInnerPoids(data)
    }

    fun getLastPoids(): Int {
        return poidsDao.getLastPoids()
    }

    fun getLastPeriode(): Int {
        return periodeDao.getLastPeriode()
    }

    suspend fun deletePoids(id: Int) {
        return poidsDao.deletePoids(id)
    }

    suspend fun deletePeriode(id: Int) {
        return periodeDao.deletePeriode(id)
    }

    suspend fun updatePoids(id: Int, poids: Float) {
        return poidsDao.updatePoids(id, poids)
    }

    suspend fun updatePeriode(id: Int, date: String, heure: String, periode: String) {
        return periodeDao.updatePeriode(id, date, heure, periode)
    }
}