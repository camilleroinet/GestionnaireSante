package com.example.gestionnairesante.database.dao.innerPlat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gestionnairesante.database.dao.repas.RepasData
import com.example.gestionnairesante.database.dao.plats.PlatData
import kotlinx.coroutines.flow.Flow


@Dao
interface InnerPlatMenuDao {
    @Insert
    suspend fun insertInnerPlatMenu(data: InnerPlatMenuData)

    @Query(
        "SELECT * " +
        "FROM plat " +
        "INNER JOIN innerPlat " +
        "ON plat.id_plat = innerPlat.idpla " +
        "WHERE innerPlat.idmen = :id_menu"
    )
    fun getInnerPlat(id_menu: Int): Flow<List<PlatData>>

    @Query(
        "SELECT * " +
        "FROM menu " +
        "INNER JOIN innerPlat " +
        "ON menu.id_menu = innerPlat.idmen " +
        "WHERE innerPlat.idpla = :id_plat"
    )
    fun getInnerMenu(id_plat: Int): Flow<List<RepasData>>

    @Query(
        "SELECT " +
            "innerPlat.id_service AS idser, " +
            "plat.id_plat AS idpla, " +
            "plat.nom_plat AS nomPlat, " +
            "plat.glucide_plat AS gluPlat, " +
            "plat.calorie_plat AS calPlat, " +
            "menu.id_menu AS idmen, " +
            "menu.nom_menu AS nomMenu " +
        "FROM innerPlat " +
        "INNER JOIN plat " +
        "ON plat.id_plat = innerPlat.idpla " +
        "INNER JOIN menu " +
        "ON menu.id_menu = innerPlat.idmen " +
        "WHERE innerPlat.idmen  =(SELECT MAX(id_menu) FROM menu)"
    )
    fun getPlatInMenu() : Flow<List<PlatInner>>

    @Query("SELECT idmen FROM innerPlat WHERE idmen = (SELECT MAX(idmen) FROM innerPlat)")
    fun getLastMenuInCurrent(): Int

    @Query(
        "SELECT * " +
        "FROM innerPlat " +
        "INNER JOIN plat " +
        "ON plat.id_plat = innerPlat.idpla " +
        "INNER JOIN menu " +
        "ON menu.id_menu = innerPlat.idmen"
    )
    fun getInnerAllInner(): Flow<List<InnerPlatMenuData>>

    @Query(
        "SELECT " +
            "id_service AS idser, " +
            "plat.id_plat AS idpla, " +
            "plat.nom_plat AS nomPlat, " +
            "plat.glucide_plat AS gluPlat, " +
            "plat.calorie_plat AS calPlat, " +
            "menu.id_menu AS idmen, " +
            "menu.nom_menu AS nomMenu " +
        "FROM innerPlat " +
        "INNER JOIN plat " +
        "ON plat.id_plat = innerPlat.idpla " +
        "INNER JOIN menu " +
        "ON menu.id_menu = innerPlat.idmen"
    )
    fun getAllPlat(): Flow<List<PlatInner>>

    @Query(
        "DELETE FROM innerPlat " +
        "WHERE id_service = :id"
    )
    suspend fun deletePlatInCurrent(id: Int)

}