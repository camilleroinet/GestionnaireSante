package com.example.gestionnairesante.database.dao.innerPlat

import com.example.gestionnairesante.database.dao.innerRepas.InnerPeriodeRepasDao
import com.example.gestionnairesante.database.dao.innerRepas.InnerPeriodeRepasData
import com.example.gestionnairesante.database.dao.repas.RepasDao
import com.example.gestionnairesante.database.dao.plats.PlatDao
import com.example.gestionnairesante.database.dao.plats.PlatData
import kotlinx.coroutines.flow.Flow

class InnerPlatMenuRepo(
    private var platDao: PlatDao,
    private var repasDao: RepasDao,
    private var innerPlatMenuDao: InnerPlatMenuDao,
    private var innerMenuDao: InnerPeriodeRepasDao

) {

    val allPlat = platDao.getAllPlat()
    val allMenu = repasDao.getAllMenu()
    val allInner = innerMenuDao.getAllInner()
    val innerPeriodeMenu = innerMenuDao.getAllValeurs()

    suspend fun insertPlat(plat: PlatData) {
        return platDao.insertPlat(plat)
    }

    suspend fun insertMenu(nom: String, nbPlat: Int, gly: Int, cal:Int) {
        return repasDao.insertMenu(nom, nbPlat, gly, cal)
    }



    suspend fun insertInnerPlatMenu(data: InnerPlatMenuData) {
        return innerPlatMenuDao.insertInnerPlatMenu(data)
    }
    suspend fun insertInnerPeriodeMenu(data: InnerPeriodeRepasData) {
        return innerMenuDao.insertInnerPeriodeMenu(data)
    }



/*    fun getInnerPlat(id: Int): Flow<List<PlatInner>> {
        //return innerPlatMenuDao.getInnerPlat(id)
    }*/

    fun getLastPlat() : Int {
        return platDao.getLastPlat()
    }

    fun getLastMenu() : Int {
        return repasDao.getLastMenu()
    }

    fun getPlatInMenu(): Flow<List<PlatInner>> {
        return innerPlatMenuDao.getPlatInMenu()
    }
    fun getLastMenuInCurrent(): Int {
        return innerPlatMenuDao.getLastMenuInCurrent()
    }



    suspend fun deletePlat(id: Int) {
        return platDao.deletePlat(id)
    }

    suspend fun deletePlatObj(data: PlatData) {
        return platDao.deletePlatObj(data)
    }


    suspend fun deleteMenu(id: Int) {
        return repasDao.deleteMenu(id)
    }

    suspend fun updatePlat(id: Int, nom: String, glu : Int, cal: Int){
        return platDao.updatePlat(id, nom, glu, cal)
    }

    suspend fun updateMenu(id: Int, totalPlat: Int, totalGly: Int, totalCal: Int){
        return repasDao.updateMenu(id, totalPlat, totalGly, totalCal)
    }

    suspend fun deletePlatInCurrent(id: Int) {
        return innerPlatMenuDao.deletePlatInCurrent(id)
    }
}