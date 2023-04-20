package com.example.gestionnairesante.database.dao.insuline

class InsulineRepo(private val dao: InsulineDao) {
    val allInsuline = dao.getAllInsuline()
    val allRapide = dao.getAllRapide()
    val allLente = dao.getAllLente()

    suspend fun insertInsuline(userdao: InsulineData) {
        return dao.insertInsuline(userdao)
    }

    suspend fun insulineUpdate(id: Int, rapide: Int, lente: Int) {
        return dao.insulineUpdate(id, rapide, lente)
    }

}