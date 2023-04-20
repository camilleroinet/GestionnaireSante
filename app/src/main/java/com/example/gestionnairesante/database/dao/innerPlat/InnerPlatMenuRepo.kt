package com.example.gestionnairesante.database.dao.innerPlat

import com.example.gestionnairesante.database.dao.menu.MenuDao
import com.example.gestionnairesante.database.dao.menu.MenuData
import com.example.gestionnairesante.database.dao.plats.PlatDao
import com.example.gestionnairesante.database.dao.plats.PlatData

class InnerPlatMenuRepo(
    private var platDao: PlatDao,
    private var menuDao: MenuDao,
    private var innerPlatMenuDao: InnerPlatMenuDao
) {

    val allPlat = platDao.getAllPlat()
    val allMenu = menuDao.getAllMenu()
    val allInnerPlatMenu = innerPlatMenuDao.getAllPlat()

    suspend fun insertPlat(plat: PlatData) {
        return platDao.insertPlat(plat)
    }

    suspend fun insertMenu(menu: MenuData) {
        return menuDao.insertMenu(menu)
    }

    suspend fun insertInnerPlatMenu(data: InnerPlatMenuData) {
        return innerPlatMenuDao.insertInnerPlatMenu(data)
    }

    fun getLastPlat() : Int {
        return platDao.getLastPlat()
    }

    fun getLastMenu() : Int {
        return menuDao.getLastMenut()
    }

    suspend fun deletePlat(id: Int) {
        return platDao.deletePlat(id)
    }

    suspend fun deleteMenu(id: Int) {
        return menuDao.deleteMenu(id)
    }

    suspend fun updatePlat(id: Int, nom: String, glu : Int, cal: Int){
        return platDao.updatePlat(id, nom, glu, cal)
    }

    suspend fun updateMenu(id: Int, nom: String){
        return menuDao.updateMenu(id, nom)
    }

}