package com.example.gestionnairesante.database.dao.glycemie

class GlycemieRepo (private val dao: GlycemieDao) {
    val allglycemie = dao.getAllGlycemie()
    val allValeurGlycemie = dao.getAllValeurGlycemie()

    suspend fun insertGlycemie(data: GlycemieData) {
        return dao.insertGlycemie(data)
    }

    suspend fun deleteGlycemie(data: Int) {
        return dao.deleteGlycemie(data)
    }

    suspend fun updateGlycemie(id: Int, gly: Int) {
        return dao.updateGlycemie(id, gly)
    }

    fun getLastGlycemie(): Int {
        return dao.getLastGlycemie()
    }
}