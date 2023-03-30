package com.example.gestionnairesante.database.dao.poids

import com.example.gestionnairesante.database.dao.glycemie.GlycemieData

class PoidsRepo (private val dao: PoidsDao){
    val allValeurPoids = dao.getAllValeurPoids()
    val allPoids = dao.getAllPoids()
    val allValeurPoidsDesc = dao.getAllValeurPoidsDesc()

    suspend fun insertPoids(userdao: PoidsData) : Long{
        return dao.insertPoids(userdao)
    }

    suspend fun deletePoids(userdao: PoidsData): Int{
        return dao.deletePoids(userdao)
    }
    suspend fun deleteAllPoids(id : Int): Int{
        return dao.deleteAllPoids(id)
    }
}