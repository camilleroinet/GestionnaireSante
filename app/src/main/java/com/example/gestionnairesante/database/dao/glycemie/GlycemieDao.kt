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

    @Query("UPDATE glycemie SET valeur_glycemie = :glycemie WHERE id_glycemie = :id")
    suspend fun updateGlycemie(id: Int, glycemie: Int) : Int

    @Delete
    suspend fun deleteGlycemie(user: GlycemieData) : Int

    @Query("DELETE FROM glycemie")
    suspend fun deleteAllGlycemie() : Int

    @Query("SELECT * FROM glycemie")
    fun getAllGlycemie(): Flow<List<GlycemieData>>

    @Query("SELECT valeur_glycemie FROM glycemie")
    fun getAllValeurGlycemie(): Flow<List<Int>>

    @Query("SELECT * FROM glycemie WHERE id_glycemie = :glycemie")
    fun getGlycemieToUpdate(glycemie: Int) : GlycemieData

}