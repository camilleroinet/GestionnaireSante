package com.example.gestionnairesante.database.dao.glycemie

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GlycemieDao {
    @Insert
    suspend fun insertGlycemie(user: GlycemieData) : Long

    @Query("UPDATE glycemie SET valeur_glycemie = :glycemie WHERE id_glycemie = :id")
    suspend fun updateGlycemie(id: Int, glycemie: Int) : Int

    @Query("DELETE FROM glycemie WHERE id_glycemie = :id")
    suspend fun deleteGlycemie(id: Int): Int

    @Query("SELECT * FROM glycemie")
    fun getAllGlycemie(): Flow<List<GlycemieData>>

    @Query("SELECT valeur_glycemie FROM glycemie")
    fun getAllValeurGlycemie(): Flow<List<Int>>

    @Query("SELECT * FROM glycemie WHERE id_glycemie = :glycemie")
    fun getGlycemieToUpdate(glycemie: Int) : GlycemieData

    // Retourne le dernier id de la glycemie
    @Query("SELECT id_glycemie FROM glycemie WHERE id_glycemie=(SELECT MAX(id_glycemie) FROM glycemie)")
    fun getLastGlycemie() : Int


}