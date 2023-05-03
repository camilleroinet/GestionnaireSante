package com.example.gestionnairesante.database.dao.innerMenu

import com.example.gestionnairesante.database.dao.menu.MenuDao
import com.example.gestionnairesante.database.dao.menu.MenuData
import com.example.gestionnairesante.database.dao.periode.PeriodeDao
import com.example.gestionnairesante.database.dao.periode.PeriodeData

class InnerPeriodeMenuRepo(
    private var menuDao: MenuDao,
    private var periodeDao: PeriodeDao,
    private var innerMenuDao: InnerPeriodeMenuDao

) {

    val allMenu = menuDao.getAllMenu()
    val allInner = innerMenuDao.getAllInner()
    val innerPeriodeMenu = innerMenuDao.getAllValeurs()

    suspend fun insertMenu(menu: MenuData) {
        return menuDao.insertMenu(menu)
    }

    suspend fun insertPeriode(per: PeriodeData) {
        return periodeDao.insertPeriode(per)
    }

    suspend fun insertInnerPeriodeMenu(data: InnerPeriodeMenuData) {
        return innerMenuDao.insertInnerPeriodeMenu(data)
    }

    suspend fun deleteMenu(menu: Int) {
        return menuDao.deleteMenu(menu)
    }

    fun getLastPeriode(): Int {
        return periodeDao.getLastPeriode()
    }

    suspend fun deletePeriode(periode: Int) {
        return periodeDao.deletePeriode(periode)
    }

    suspend fun updatePeriode(id: Int, date: String, heure: String, periode: String) {
        return periodeDao.updatePeriode(id, date, heure, periode)
    }


}