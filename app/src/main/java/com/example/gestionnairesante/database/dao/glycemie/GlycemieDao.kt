package com.example.gestionnairesante.database.dao.glycemie

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GlycemieDao {

    /**
     * GLYCEMIE
     */
    @Insert
    suspend fun insertGlycemie(user: GlycemieData) : Long

    @Update
    suspend fun updateGlycemie(user: GlycemieData) : Int

    @Delete
    suspend fun deleteGlycemie(user: GlycemieData) : Int

    @Query("DELETE FROM glycemie")
    suspend fun deleteAllGlycemie() : Int

    @Query("SELECT * FROM glycemie")
    fun getAllGlycemie(): Flow<List<GlycemieData>>

    @Query("SELECT valeur_glycemie FROM glycemie")
    fun getAllValeurGlycemie(): Flow<List<Int>>

}