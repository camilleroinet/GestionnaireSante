package com.example.gestionnairesante.database.dao.poids

class PoidsRepo(private val dao: PoidsDao) {
    val allValeurPoids = dao.getAllValeurPoids()
    val allPoids = dao.getAllPoids()
    val allValeurPoidsDesc = dao.getAllValeurPoidsDesc()

    suspend fun insertPoids(data: PoidsData) {
        return dao.insertPoids(data)
    }

    suspend fun updatePoids(id: Int, poids: Float) {
        return dao.updatePoids(id, poids)
    }

    suspend fun deletePoids(data: PoidsData)  {
        return dao.deletePoids(data)
    }

    suspend fun deleteAllPoids(id: Int) {
        return dao.deleteAllPoids(id)
    }

    fun getPoidsToUpdate(id: Int) : PoidsData {
        return dao.getPoidsToUpdate(id)
    }
}