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

    fun getGlycemiePeriode(date: String) : Flow<List<Int>> {
        return statsDao.getGlycemiePeriode(date)
    }

    fun getSpecRapide(date: String) : Flow<List<Int>> {
        return statsDao.getSpecRapide(date)
    }
    fun getSpecLente(date: String) : Flow<List<Int>> {
        return statsDao.getSpecLente(date)
    }


    fun getSpecCalories(date: String) : Float {
        return statsDao.getSpecCalories(date)
    }

    fun getSpecGlucides(date: String) : Float {
        return statsDao.getSpecGlucides(date)
    }

    fun getSpecPoids(date: String) : Float {
        return statsDao.getSpecPoids(date)
    }

}