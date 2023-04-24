package com.example.gestionnairesante.database.dao.plats

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlatDao {

    /**
     * GLYCEMIE
     */
    @Insert
    suspend fun insertPlat(user: PlatData)
    @Delete
    suspend fun deletePlatObj(data: PlatData)

    @Query("UPDATE plat SET nom_plat = :nomPlat, glucide_plat = :glucide, calorie_plat = :calorie WHERE id_plat = :id")
    suspend fun updatePlat(id:Int, nomPlat: String, glucide: Int, calorie: Int)

    @Query("DELETE FROM plat WHERE id_plat = :id")
    suspend fun deletePlat(id: Int)

    @Query("SELECT * FROM plat")
    fun getAllPlat(): Flow<List<PlatData>>

    @Query("SELECT calorie_plat FROM plat")
    fun getAllCaloriePlat(): Flow<List<Int>>

    @Query("SELECT glucide_plat FROM plat")
    fun getAllglucidePlat(): Flow<List<Int>>

    @Query("SELECT id_plat FROM plat WHERE id_plat = (SELECT MAX(id_plat) FROM plat)")
    fun getLastPlat(): Int

}