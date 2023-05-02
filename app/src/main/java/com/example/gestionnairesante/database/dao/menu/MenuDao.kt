package com.example.gestionnairesante.database.dao.menu

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {

    @Insert
    suspend fun insertMenu(user: MenuData)
    @Query("UPDATE menu SET totalPlat = :tplat, totalGly = :tGly, totalCal = :tCal  WHERE id_menu = :id")
    suspend fun updateMenu(id:Int, tplat : Int, tGly : Int, tCal : Int )

    @Query("SELECT * FROM menu")
    fun getAllMenu(): Flow<List<MenuData>>

    @Query("DELETE FROM menu WHERE id_menu = :id")
    suspend fun deleteMenu(id: Int)

    @Query("SELECT id_menu FROM menu WHERE id_menu = (SELECT MAX(id_menu) FROM menu)")
    fun getLastMenut(): Int
}