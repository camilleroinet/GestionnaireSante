package com.example.gestionnairesante.database.dao.insuline

class ParamStyloRepo(private val dao: ParamStyloDao) {

    val nbStylo = dao.getAllStylo()


    suspend fun insertStylo(data: ParamStyloData) {
        return dao.insertStylo(data)
    }

    suspend fun updateStylo(id: Int, quantite: Int, maxquantite: Int, purge: Int){
        return dao.updateStylo(id, quantite, maxquantite, purge)
    }
}