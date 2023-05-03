package com.example.gestionnairesante.database.dao.innerPlat

import com.example.gestionnairesante.database.dao.innerMenu.InnerPeriodeMenuDao
import com.example.gestionnairesante.database.dao.innerMenu.InnerPeriodeMenuData
import com.example.gestionnairesante.database.dao.menu.MenuDao
import com.example.gestionnairesante.database.dao.menu.MenuData
import com.example.gestionnairesante.database.dao.plats.PlatDao
import com.example.gestionnairesante.database.dao.plats.PlatData
import kotlinx.coroutines.flow.Flow

class InnerPlatMenuRepo(
    private var platDao: PlatDao,
    private var menuDao: MenuDao,
    private var innerPlatMenuDao: InnerPlatMenuDao,
    private var innerMenuDao: InnerPeriodeMenuDao

) {

    val allPlat = platDao.getAllPlat()
    val allMenu = menuDao.getAllMenu()
    val allInner = innerMenuDao.getAllInner()
    val innerPeriodeMenu = innerMenuDao.getAllValeurs()

    suspend fun insertPlat(plat: PlatData) {
        return platDao.insertPlat(plat)
    }

    suspend fun insertMenu(menu: MenuData) {
        return menuDao.insertMenu(menu)
    }



    suspend fun insertInnerPlatMenu(data: InnerPlatMenuData) {
        return innerPlatMenuDao.insertInnerPlatMenu(data)
    }
    suspend fun insertInnerPeriodeMenu(data: InnerPeriodeMenuData) {
        return innerMenuDao.insertInnerPeriodeMenu(data)
    }



/*    fun getInnerPlat(id: Int): Flow<List<PlatInner>> {
        //return innerPlatMenuDao.getInnerPlat(id)
    }*/

    fun getLastPlat() : Int {
        return platDao.getLastPlat()
    }

    fun getLastMenu() : Int {
        return menuDao.getLastMenut()
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
        return menuDao.deleteMenu(id)
    }

    suspend fun updatePlat(id: Int, nom: String, glu : Int, cal: Int){
        return platDao.updatePlat(id, nom, glu, cal)
    }

    suspend fun updateMenu(id: Int, totalPlat: Int, totalGly: Int, totalCal: Int){
        return menuDao.updateMenu(id, totalPlat, totalGly, totalCal)
    }

    suspend fun deletePlatInCurrent(id: Int) {
        return innerPlatMenuDao.deletePlatInCurrent(id)
    }
}