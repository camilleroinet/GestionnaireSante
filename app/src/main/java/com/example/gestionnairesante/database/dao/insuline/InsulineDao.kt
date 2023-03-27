package com.example.gestionnairesante.database.dao.insuline

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface InsulineDao {

    @Insert
    suspend fun insertInsuline(user: InsulineData) : Long

    @Query("SELECT * FROM insuline")
    fun getAllInsuline(): Flow<List<InsulineData>>


    @Query("SELECT insuline_rapide FROM insuline")
    fun getAllRapide(): Flow<List<Int>>

    @Query("SELECT insuline_lente FROM insuline")
    fun getAllLente(): Flow<List<Int>>
}