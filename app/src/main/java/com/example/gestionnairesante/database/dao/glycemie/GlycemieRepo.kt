package com.example.gestionnairesante.database.dao.glycemie

class GlycemieRepo (private val dao: GlycemieDao) {
    val allglycemie = dao.getAllGlycemie()
    val allValeurGlycemie = dao.getAllValeurGlycemie()



    suspend fun insertGlycemie(userdao: GlycemieData): Long{
        return dao.insertGlycemie(userdao)
    }


    suspend fun updateGlycemie(userdao: GlycemieData): Int {
        return dao.updateGlycemie(userdao)
    }

    suspend fun deleteGlycemie(userdao: GlycemieData): Int{
        return dao.deleteGlycemie(userdao)
    }
    suspend fun deleteAllGlycemie(): Int{
        return dao.deleteAllGlycemie()
    }


}