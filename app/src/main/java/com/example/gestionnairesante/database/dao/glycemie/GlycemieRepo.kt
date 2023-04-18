package com.example.gestionnairesante.database.dao.glycemie

class GlycemieRepo (private val dao: GlycemieDao) {
    val allglycemie = dao.getAllGlycemie()
    val allValeurGlycemie = dao.getAllValeurGlycemie()

    suspend fun insertGlycemie(data: GlycemieData): Long{
        return dao.insertGlycemie(data)
    }

    suspend fun deleteGlycemie(data: Int): Int{
        return dao.deleteGlycemie(data)
    }

    // Permet la recuperation de l'objet qui va etre update
    fun getGlycemieToUpdate(id: Int) : GlycemieData{
        return dao.getGlycemieToUpdate(id)
    }
    suspend fun updateGlycemie(id: Int, gly: Int): Int {
        return dao.updateGlycemie(id, gly)
    }

    fun getLastGlycemie(): Int {
        return dao.getLastGlycemie()
    }
}