package com.example.gestionnairesante.database.dao.repas

class RepasRepo(private val dao: RepasDao) {
    val allMenu = dao.getAllMenu()

    suspend fun insertMenu(nom: String, nbPlat: Int, gly: Int, cal: Int) {
        return dao.insertMenu(nom, nbPlat, gly, cal)
    }

}