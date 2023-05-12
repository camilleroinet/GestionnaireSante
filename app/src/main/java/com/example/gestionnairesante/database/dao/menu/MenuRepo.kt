package com.example.gestionnairesante.database.dao.menu

class MenuRepo(private val dao: MenuDao) {
    val allMenu = dao.getAllMenu()

    suspend fun insertMenu(nom: String, nbPlat: Int, gly: Int, cal: Int) {
        return dao.insertMenu(nom, nbPlat, gly, cal)
    }

}