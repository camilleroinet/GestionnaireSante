package com.example.gestionnairesante.database.dao.poids

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PoidsDao {

    @Insert
    suspend fun insertPoids(user: PoidsData) : Long

    @Query("SELECT * FROM poids")
    fun getAllPoids(): Flow<List<PoidsData>>

    @Query("SELECT valeur_poids FROM poids")
    fun getAllValeurPoids(): Flow<List<Float>>

    @Query("SELECT valeur_poids FROM poids WHERE id_poids = ( SELECT MAX( id_poids )  FROM poids )")
    fun getLastPoids(): Float
}