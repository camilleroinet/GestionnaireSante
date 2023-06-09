package com.example.gestionnairesante.database.dao.repas

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RepasDao {

    @Query("INSERT INTO menu (nom_menu, totalPlat, totalGly, totalCal ) VALUES (:nom, :nbPlat, :gly, :cal)")
    suspend fun insertMenu(nom: String, nbPlat: Int, gly: Int, cal:Int)

    @Query("UPDATE menu SET totalPlat = :tplat, totalGly = :tGly, totalCal = :tCal  WHERE id_menu = :id")
    suspend fun updateMenu(id:Int, tplat : Int, tGly : Int, tCal : Int )

    @Query("SELECT * FROM menu")
    fun getAllMenu(): Flow<List<RepasData>>

    @Query("DELETE FROM menu WHERE id_menu = :id")
    suspend fun deleteMenu(id: Int)

    @Query("SELECT id_menu FROM menu WHERE id_menu = (SELECT MAX(id_menu) FROM menu)")
    fun getLastMenu(): Int


    @Query(
        "SELECT SUM(menu.totalCal) " +
                "FROM menu " +
                "INNER JOIN innerMenu " +
                "ON menu.id_menu = innerMenu.idmen " +
                "INNER JOIN periode " +
                "ON periode.id_periode = innerMenu.idper " +
                "WHERE periode.date_periode = :date "
    )
    fun getSpecCalories(date: String): Float

    @Query(
        "SELECT SUM(menu.totalGly) " +
                "FROM menu " +
                "INNER JOIN innerMenu " +
                "ON menu.id_menu = innerMenu.idmen " +
                "INNER JOIN periode " +
                "ON periode.id_periode = innerMenu.idper " +
                "WHERE periode.date_periode = :date "
    )
    fun getSpecGlucides(date: String): Float
}