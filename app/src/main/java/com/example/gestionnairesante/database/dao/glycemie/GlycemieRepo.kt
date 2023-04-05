package com.example.gestionnairesante.database.dao.glycemie

class GlycemieRepo (private val dao: GlycemieDao) {
    val allglycemie = dao.getAllGlycemie()
    val allValeurGlycemie = dao.getAllValeurGlycemie()



    suspend fun insertGlycemie(userdao: GlycemieData): Long{
        return dao.insertGlycemie(userdao)
    }


    suspend fun updateGlycemie(id: Int, gly: Int): Int {
        return dao.updateGlycemie(id, gly)
    }

    suspend fun deleteGlycemie(userdao: GlycemieData): Int{
        return dao.deleteGlycemie(userdao)
    }
    suspend fun deleteAllGlycemie(): Int{
        return dao.deleteAllGlycemie()
    }

    fun getGlycemieToUpdate(id: Int) : GlycemieData{
        return dao.getGlycemieToUpdate(id)
    }

}