package com.example.gestionnairesante.database.dao.insuline

class InsulineRepo(private val dao: InsulineDao) {
    val allInsuline = dao.getAllInsuline()
    val allRapide = dao.getAllRapide()
    val allLente = dao.getAllLente()

    suspend fun insertInsuline(userdao: InsulineData): Long {
        return dao.insertInsuline(userdao)
    }

    fun insulineUpdate(id: Int, rapide: Int, lente: Int) : Int {
        return dao.insulineUpdate(id, rapide, lente)
    }

    fun getInsulineToUpadte(id: Int) : InsulineData {
        return dao.getInsulineToUdpate(id)
    }
    suspend fun deleteInsuline(data: InsulineData): Int {
        return dao.deleteInsuline(data)
    }


}