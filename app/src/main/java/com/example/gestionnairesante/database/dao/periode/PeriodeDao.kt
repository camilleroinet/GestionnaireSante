package com.example.gestionnairesante.database.dao.periode

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PeriodeDao {
    @Insert
    suspend fun insertPeriode(user: PeriodeData) : Long

    @Query("SELECT * FROM periode WHERE id_periode = :id")
    fun getPeriodeToUpdate(id: Int) : PeriodeData

    @Query("UPDATE periode SET date_periode = :date, heure_periode = :heure WHERE id_periode = :id")
    suspend fun updatePeriode(id: Int, date: String, heure: String) : Int

    @Query("DELETE FROM periode WHERE id_periode = :id")
    suspend fun deletePeriode(id: Int): Int

    @Query("SELECT * FROM periode")
    fun getAllPeriode(): Flow<List<PeriodeData>>

    @Query("SELECT id_periode FROM periode WHERE id_periode = (SELECT MAX(id_periode) FROM periode)")
    fun getLastPeriode() : Int

}
