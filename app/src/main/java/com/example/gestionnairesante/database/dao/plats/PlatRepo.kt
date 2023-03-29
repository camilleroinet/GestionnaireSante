package com.example.gestionnairesante.database.dao.plats

class PlatRepo (private val dao: PlatDao){
    val allPlat = dao.getAllPlat()

    suspend fun insertPlat(userdao: PlatData): Long{
        return dao.insertPlat(userdao)
    }

    suspend fun updatePlat(userdao: PlatData): Int{
        return dao.updatePlat(userdao)
    }

    suspend fun deletePlat(userdao: PlatData): Int{
        return dao.deletePlat(userdao)
    }
    suspend fun deleteAllPlat(): Int{
        return dao.deleteAllPlat()
    }

}