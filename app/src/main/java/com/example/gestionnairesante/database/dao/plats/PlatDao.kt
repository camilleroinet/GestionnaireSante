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

    @Query("UPDATE plat SET nom_plat = :nomPlat, glucide_plat = :glucide, calorie_plat = :calorie WHERE id_plat = :id")
    suspend fun updatePlat(id:Int, nomPlat: String, glucide: Int, calorie: Int): Int

    @Query("SELECT * FROM plat WHERE id_plat = :id")
    fun getPlatToUpdate(id: Int) : PlatData

    @Delete
    suspend fun deletePlat(user: PlatData): Int

    @Query("DELETE FROM plat")
    suspend fun deleteAllPlat(): Int

    @Query("SELECT * FROM plat")
    fun getAllPlat(): Flow<List<PlatData>>


}