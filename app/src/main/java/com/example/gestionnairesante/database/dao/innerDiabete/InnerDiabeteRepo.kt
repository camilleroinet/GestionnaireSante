package com.example.gestionnairesante.database.dao.innerDiabete

import com.example.gestionnairesante.database.dao.glycemie.GlycemieDao
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import com.example.gestionnairesante.database.dao.insuline.InsulineDao
import com.example.gestionnairesante.database.dao.insuline.InsulineData
import com.example.gestionnairesante.database.dao.periode.PeriodeDao
import com.example.gestionnairesante.database.dao.periode.PeriodeData

class InnerDiabeteRepo(
    private var glycemieDao: GlycemieDao,
    private var periodeDao: PeriodeDao,
    private var insulineDao: InsulineDao,
    private var innerDiabeteDao: InnerDiabeteDao
) {

    val allGlycemie = glycemieDao.getAllValeurGlycemie()
    val allPeriode = periodeDao.getAllPeriode()
//    val allInsuline = insulineDao.getAllInsuline()


    val innerGlycemie = innerDiabeteDao.getAllInner()
    val innerPeriodeGlycemie = innerDiabeteDao.getAllValeurs()


    suspend fun insertGlycemie(gly: GlycemieData) : Long{
        return glycemieDao.insertGlycemie(gly)
    }

    suspend fun insertPeriode(per: PeriodeData) : Long{
        return periodeDao.insertPeriode(per)
    }

    suspend fun insertInsuline(insuline: InsulineData) : Long{
        return insulineDao.insertInsuline(insuline)
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

    fun getLastInsuline() : Int {
        return insulineDao.getLastInsuline()
    }

    suspend fun deleteGlycemie(glycemie: Int) : Int {
        return glycemieDao.deleteGlycemie(glycemie)
    }
    suspend fun deletePeriode(periode: Int) : Int {
        return periodeDao.deletePeriode(periode)
    }

    suspend fun deleteInsuline(insuline: Int) : Int {
        return insulineDao.deleteInsuline(insuline)
    }

}