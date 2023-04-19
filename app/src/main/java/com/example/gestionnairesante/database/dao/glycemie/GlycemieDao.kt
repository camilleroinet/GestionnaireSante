package com.example.gestionnairesante.database.dao.glycemie

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GlycemieDao {
    @Insert
    suspend fun insertGlycemie(user: GlycemieData)

    @Query("UPDATE glycemie SET valeur_glycemie = :glycemie WHERE id_glycemie = :id")
    suspend fun updateGlycemie(id: Int, glycemie: Int)

    @Query("DELETE FROM glycemie WHERE id_glycemie = :id")
    suspend fun deleteGlycemie(id: Int)

    @Query("SELECT * FROM glycemie")
    fun getAllGlycemie(): Flow<List<GlycemieData>>

    @Query("SELECT valeur_glycemie FROM glycemie")
    fun getAllValeurGlycemie(): Flow<List<Int>>

    // Retourne le dernier id de la glycemie
    @Query("SELECT id_glycemie FROM glycemie WHERE id_glycemie=(SELECT MAX(id_glycemie) FROM glycemie)")
    fun getLastGlycemie() : Int

}