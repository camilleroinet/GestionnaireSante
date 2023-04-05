package com.example.gestionnairesante.database.dao.menu

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {

    @Insert
    suspend fun insertMenu(user: MenuData): Long

    @Query("SELECT * FROM menu")
    fun getAllMenu(): Flow<List<MenuData>>


}