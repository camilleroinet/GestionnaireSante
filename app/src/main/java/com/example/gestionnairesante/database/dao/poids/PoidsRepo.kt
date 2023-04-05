package com.example.gestionnairesante.database.dao.poids

class PoidsRepo(private val dao: PoidsDao) {
    val allValeurPoids = dao.getAllValeurPoids()
    val allPoids = dao.getAllPoids()
    val allValeurPoidsDesc = dao.getAllValeurPoidsDesc()

    suspend fun insertPoids(data: PoidsData): Long {
        return dao.insertPoids(data)
    }

    suspend fun updatePoids(id: Int, poids: Float): Int {
        return dao.updatePoids(id, poids)
    }

    suspend fun deletePoids(data: PoidsData): Int {
        return dao.deletePoids(data)
    }

    suspend fun deleteAllPoids(id: Int): Int {
        return dao.deleteAllPoids(id)
    }

    fun getPoidsToUpdate(id: Int) : PoidsData {
        return dao.getPoidsToUpdate(id)
    }
}