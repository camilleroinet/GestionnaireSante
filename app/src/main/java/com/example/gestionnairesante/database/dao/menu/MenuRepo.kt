package com.example.gestionnairesante.database.dao.menu

class MenuRepo(private val dao: MenuDao) {
    val allMenu = dao.getAllMenu()

    suspend fun insertMenu(userdao: MenuData) {
        return dao.insertMenu(userdao)
    }

}