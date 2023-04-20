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

    suspend fun deletePoids(id: Int) {
        return dao.deletePoids(id)
    }

    fun getPoidsToUpdate(id: Int): PoidsData {
        return dao.getPoidsToUpdate(id)
    }
}