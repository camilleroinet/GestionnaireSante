package com.example.gestionnairesante.database.dao.insuline

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ParamStyloDao {

    @Insert
    suspend fun insertStylo(data: ParamStyloData)

    @Query("SELECT COUNT(*) From stylo")
    fun getAllStylo() : Int

    @Query("UPDATE stylo SET quantite = :quantite, maxquantite = :max, purge = :purge WHERE id_stylo = :id")
    suspend fun updateStylo(id: Int, quantite: Int, max: Int, purge: Int)

}