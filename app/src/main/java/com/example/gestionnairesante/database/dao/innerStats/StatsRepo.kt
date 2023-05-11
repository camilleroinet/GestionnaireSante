package com.example.gestionnairesante.database.dao.innerStats

import kotlinx.coroutines.flow.Flow


class StatsRepo(
    private var statsDao : StatsDao

) {

    // Liste des gmlycemies en fonction de la date
    val allGlycemie = statsDao.getAllGlycemie()


    fun getSpecGlycemie(date: String) : Flow<List<Int>> {
        return statsDao.getSpecGlycemie(date)
    }
    fun getSpecRapide(date: String) : Flow<List<Int>> {
        return statsDao.getSpecRapide(date)
    }
    fun getSpecLente(date: String) : Flow<List<Int>> {
        return statsDao.getSpecLente(date)
    }

    fun getSpecPoids(date: String) : Flow<List<Float>> {
        return statsDao.getSpecPoids(date)
    }

}