package com.example.gestionnairesante.database.dao.innerDiabete

import com.example.gestionnairesante.database.dao.glycemie.GlycemieDao
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import com.example.gestionnairesante.database.dao.insuline.InsulineDao
import com.example.gestionnairesante.database.dao.insuline.InsulineData
import com.example.gestionnairesante.database.dao.insuline.ParamStyloDao
import com.example.gestionnairesante.database.dao.insuline.ParamStyloData
import com.example.gestionnairesante.database.dao.periode.PeriodeDao
import com.example.gestionnairesante.database.dao.periode.PeriodeData

class InnerDiabeteRepo(
    private var glycemieDao: GlycemieDao,
    private var periodeDao: PeriodeDao,
    private var insulineDao: InsulineDao,
    private var innerDiabeteDao: InnerDiabeteDao,
    private var styloDao : ParamStyloDao

) {

    val allGlycemie = innerDiabeteDao.getAllGlycemieByDateASC()
    val allGlycemieDESC = innerDiabeteDao.getAllValeurs()

    val innerPeriodeGlycemie = innerDiabeteDao.getAllValeurs()
    val allInner = innerDiabeteDao.getAllInner()
    val nbStylo = styloDao.getAllStylo()

    suspend fun insertStylo(data: ParamStyloData) {
        return styloDao.insertStylo(data)
    }

    suspend fun updateStylo(id: Int, quantite: Int, maxquantite  : Int, purge: Int) {
        return styloDao.updateStylo(id, quantite, maxquantite, purge)
    }

    fun getAllStylo() : Int{
        return styloDao.getAllStylo()
    }
    suspend fun insertGlycemie(gly: GlycemieData) {
        return glycemieDao.insertGlycemie(gly)
    }

    suspend fun insertPeriode(per: PeriodeData) {
        return periodeDao.insertPeriode(per)
    }

    suspend fun insertInsuline(insuline: InsulineData) {
        return insulineDao.insertInsuline(insuline)
    }

    suspend fun insertGlycemieInner(glycemie: InnerDiabeteData) {
        return innerDiabeteDao.insertInnerDiabete(glycemie)
    }

    fun getLastGlycemie(): Int {
        return glycemieDao.getLastGlycemie()
    }

    fun getLastPeriode(): Int {
        return periodeDao.getLastPeriode()
    }

    fun getLastInsuline(): Int {
        return insulineDao.getLastInsuline()
    }

    suspend fun deleteGlycemie(glycemie: Int) {
        return glycemieDao.deleteGlycemie(glycemie)
    }

    suspend fun deletePeriode(periode: Int) {
        return periodeDao.deletePeriode(periode)
    }

    suspend fun deleteInsuline(insuline: Int) {
        return insulineDao.deleteInsuline(insuline)
    }

    suspend fun deleteInner(idgly: Int, idper: Int, idins: Int) {
        return innerDiabeteDao.deleteInnerDiabete(idgly, idper, idins)
    }

    suspend fun updateGlycemie(glycemie: Int, valeur: Int) {
        return glycemieDao.updateGlycemie(glycemie, valeur)
    }

    suspend fun updateInsuline(insuline: Int, rapide: Int, lente: Int) {
        return insulineDao.insulineUpdate(insuline, rapide, lente)
    }

    suspend fun updatePeriode(id: Int, date: String, heure: String, periode: String) {
        return periodeDao.updatePeriode(id, date, heure, periode)
    }


}