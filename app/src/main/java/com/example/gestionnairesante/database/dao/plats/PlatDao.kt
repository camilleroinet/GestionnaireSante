package com.example.gestionnairesante.database.dao.plats

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlatDao {

    /**
     * GLYCEMIE
     */
    @Insert
    suspend fun insertPlat(user: PlatData): Long

    @Update
    suspend fun updatePlat(user: PlatData): Int

    @Delete
    suspend fun deletePlat(user: PlatData): Int

    @Query("DELETE FROM plat")
    suspend fun deleteAllPlat(): Int

    @Query("SELECT * FROM plat")
    fun getAllPlat(): Flow<List<PlatData>>


}