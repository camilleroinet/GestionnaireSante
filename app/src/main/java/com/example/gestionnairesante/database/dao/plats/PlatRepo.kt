package com.example.gestionnairesante.database.dao.plats

class PlatRepo(private val dao: PlatDao) {
    val allPlat = dao.getAllPlat()

    suspend fun insertPlat(userdao: PlatData): Long {
        return dao.insertPlat(userdao)
    }

    suspend fun updatePlat(id: Int, nom: String, glucide: Int, calorie: Int): Int {
        return dao.updatePlat(id, nom, glucide, calorie)
    }
    fun getPlatToUpdate(id: Int) : PlatData {
        return dao.getPlatToUpdate(id)
    }

    suspend fun deletePlat(userdao: PlatData): Int {
        return dao.deletePlat(userdao)
    }

    suspend fun deleteAllPlat(): Int {
        return dao.deleteAllPlat()
    }

}