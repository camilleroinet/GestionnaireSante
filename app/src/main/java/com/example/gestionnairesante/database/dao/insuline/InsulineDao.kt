package com.example.gestionnairesante.database.dao.insuline

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface InsulineDao {

    @Insert
    suspend fun insertInsuline(user: InsulineData): Long

    @Delete
    suspend fun deleteInsuline(data: InsulineData): Int

    @Query("SELECT * FROM insuline")
    fun getAllInsuline(): Flow<List<InsulineData>>

    @Query("UPDATE insuline SET insuline_rapide = :rapide, insuline_lente = :lente WHERE id_insuline = :id")
    fun insulineUpdate(id: Int, rapide: Int, lente: Int) : Int

    @Query("SELECT * from insuline WHERE id_insuline = :id")
    fun getInsulineToUdpate(id: Int): InsulineData

    @Query("SELECT insuline_rapide FROM insuline")
    fun getAllRapide(): Flow<List<Int>>

    @Query("SELECT insuline_lente FROM insuline")
    fun getAllLente(): Flow<List<Int>>
}