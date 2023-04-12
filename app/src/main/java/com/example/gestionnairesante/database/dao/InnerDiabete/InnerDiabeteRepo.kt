package com.example.gestionnairesante.database.dao.InnerDiabete

import com.example.gestionnairesante.database.dao.glycemie.GlycemieDao
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import com.example.gestionnairesante.database.dao.periode.PeriodeDao
import com.example.gestionnairesante.database.dao.periode.PeriodeData

class InnerDiabeteRepo(
    private var glycemieDao : GlycemieDao,
    private var periodeDao : PeriodeDao,
    private var innerDiabeteDao: InnerDiabeteDao
) {

    val allGlycemie = glycemieDao.getAllGlycemie()
    val allPeriode = periodeDao.getAllPeriode()

    val innerGlycemie = innerDiabeteDao.getAllInner()
    val innerPeriodeGlycemie = innerDiabeteDao.getAllValeurs()

    suspend fun insertGlycemie(gly: GlycemieData) : Long{
        return glycemieDao.insertGlycemie(gly)
    }

    suspend fun insertPeriode(per: PeriodeData) : Long{
        return periodeDao.insertPeriode(per)
    }

    suspend fun insertGlycemieInner(glycemie: InnerDiabeteData) : Long {
        return innerDiabeteDao.insertInnerDiabete(glycemie)
    }

    fun getLastGlycemie() : Int {
        return glycemieDao.getLastGlycemie()
    }

    fun getLastPeriode() : Int {
        return periodeDao.getLastPeriode()
    }

}