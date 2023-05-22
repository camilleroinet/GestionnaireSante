package com.example.gestionnairesante.database.dao.innerRepas

import com.example.gestionnairesante.database.dao.repas.RepasDao
import com.example.gestionnairesante.database.dao.periode.PeriodeDao
import com.example.gestionnairesante.database.dao.periode.PeriodeData

class InnerPeriodeRepasRepo(
    private var repasDao: RepasDao,
    private var periodeDao: PeriodeDao,
    private var innerMenuDao: InnerPeriodeRepasDao

) {

    val allMenu = repasDao.getAllMenu()
    val allInner = innerMenuDao.getAllInner()
    val innerPeriodeMenu = innerMenuDao.getAllValeurs()

    suspend fun insertMenu(nom: String, nbPlat: Int, gly: Int, cal: Int) {
        return repasDao.insertMenu(nom,nbPlat, gly, cal)
    }

    suspend fun insertPeriode(per: PeriodeData) {
        return periodeDao.insertPeriode(per)
    }

    suspend fun insertInnerPeriodeMenu(data: InnerPeriodeRepasData) {
        return innerMenuDao.insertInnerPeriodeMenu(data)
    }

    suspend fun deleteMenu(menu: Int) {
        return repasDao.deleteMenu(menu)
    }

    fun getLastPeriode(): Int {
        return periodeDao.getLastPeriode()
    }

    fun getLastMenu(): Int {
        return repasDao.getLastMenu()
    }

    suspend fun deletePeriode(periode: Int) {
        return periodeDao.deletePeriode(periode)
    }

    suspend fun updatePeriode(id: Int, date: String, heure: String, periode: String) {
        return periodeDao.updatePeriode(id, date, heure, periode)
    }

    fun getSpecCalories(date: String) : Float {
        return repasDao.getSpecCalories(date)
    }

    fun getSpecGlucides(date: String) : Float {
        return repasDao.getSpecGlucides(date)
    }

}