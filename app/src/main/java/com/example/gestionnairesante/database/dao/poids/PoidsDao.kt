package com.example.gestionnairesante.database.dao.poids

import androidx.room.*
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import kotlinx.coroutines.flow.Flow

@Dao
interface PoidsDao {

    @Insert
    suspend fun insertPoids(user: PoidsData) : Long

    @Query("UPDATE poids SET valeur_poids = :poids WHERE id_poids = :id" )
    suspend fun updatePoids(id: Int, poids:Float) : Int

    @Delete
    suspend fun deletePoids(user: PoidsData) : Int

    @Query("DELETE FROM poids WHERE id_poids = :id")
    suspend fun deleteAllPoids(id: Int): Int

    @Query("SELECT * FROM poids")
    fun getAllPoids(): Flow<List<PoidsData>>

    @Query("SELECT valeur_poids FROM poids")
    fun getAllValeurPoids(): Flow<List<Float>>

    @Query("SELECT valeur_poids FROM poids ORDER BY id_poids DESC")
    fun getAllValeurPoidsDesc(): Flow<List<Float>>

    @Query("SELECT valeur_poids FROM poids WHERE id_poids = ( SELECT MAX( id_poids )  FROM poids )")
    fun getLastPoids(): Float

    @Query("SELECT * FROM poids WHERE id_poids = :id")
    fun getPoidsToUpdate(id: Int): PoidsData


}