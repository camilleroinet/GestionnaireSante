package com.example.gestionnairesante.database.dao.poids

import kotlinx.coroutines.flow.Flow

class PoidsRepo (private val dao: PoidsDao){
    val allValeurPoids = dao.getAllValeurPoids()
    val allPoids = dao.getAllPoids()

    suspend fun insertPoids(userdao: PoidsData) : Long{
        return dao.insertPoids(userdao)
    }

    fun getLastPoids(): Float {
        return dao.getLastPoids()
    }



}