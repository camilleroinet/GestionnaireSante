package com.example.gestionnairesante.database.dao

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


}