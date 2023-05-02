package com.example.gestionnairesante.database.dao.innerMenu

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gestionnairesante.database.dao.glycemie.GlycemieDao
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import com.example.gestionnairesante.database.dao.innerDiabete.DataInner
import com.example.gestionnairesante.database.dao.innerDiabete.InnerDiabeteDao
import com.example.gestionnairesante.database.dao.innerDiabete.InnerDiabeteData
import com.example.gestionnairesante.database.dao.insuline.InsulineDao
import com.example.gestionnairesante.database.dao.insuline.InsulineData
import com.example.gestionnairesante.database.dao.insuline.ParamStyloDao
import com.example.gestionnairesante.database.dao.insuline.ParamStyloData
import com.example.gestionnairesante.database.dao.menu.MenuDao
import com.example.gestionnairesante.database.dao.menu.MenuData
import com.example.gestionnairesante.database.dao.periode.PeriodeDao
import com.example.gestionnairesante.database.dao.periode.PeriodeData
import kotlinx.coroutines.flow.Flow

class InnerDiabeteRepo(
    private var menuDao: MenuDao,
    private var periodeDao: PeriodeDao,
    private var innerMenuDao: InnerMenuDao

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

    suspend fun insertMenuInner(iMenu: InnerMenuData) {
        return innerMenuDao.insertInnerMenu(iMenu)
    }

    suspend fun deleteMenu(menu: Int) {
        return menuDao.deleteMenu(menu)
    }

    suspend fun deletePeriode(periode: Int) {
        return periodeDao.deletePeriode(periode)
    }

    suspend fun updatePeriode(id: Int, date: String, heure: String, periode: String) {
        return periodeDao.updatePeriode(id, date, heure, periode)
    }


}