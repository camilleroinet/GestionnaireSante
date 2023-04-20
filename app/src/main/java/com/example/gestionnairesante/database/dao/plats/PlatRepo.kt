package com.example.gestionnairesante.database.dao.plats

class PlatRepo(private val dao: PlatDao) {
    val allPlat = dao.getAllPlat()

    suspend fun insertPlat(userdao: PlatData) {
        return dao.insertPlat(userdao)
    }

    suspend fun updatePlat(id: Int, nom: String, glucide: Int, calorie: Int) {
        return dao.updatePlat(id, nom, glucide, calorie)
    }

    suspend fun deletePlat(id: Int) {
        return dao.deletePlat(id)
    }


}