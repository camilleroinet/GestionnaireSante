package com.example.gestionnairesante.database.dao.innerMenu

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gestionnairesante.database.dao.menu.MenuData
import com.example.gestionnairesante.database.dao.periode.PeriodeData
import kotlinx.coroutines.flow.Flow

@Dao
interface InnerMenuDao {

    @Insert
    suspend fun insertInnerMenu(data: InnerMenuData)

    @Query(
        "SELECT * " +
                "FROM menu " +
                "INNER JOIN innerMenu " +
                "ON menu.id_menu = innerMenu.idmen " +
                "WHERE innerMenu.idper = :id_periode"
    )
    fun getInnerMenu(id_periode: Int): Flow<List<MenuData>>

    @Query(
        "SELECT * " +
                "FROM periode " +
                "INNER JOIN innerMenu " +
                "ON periode.id_periode = innerMenu.idper " +
                "WHERE innerMenu.idmen = :id_menu"
    )
    fun getInnerPeriode(id_menu: Int): Flow<List<PeriodeData>>

    @Query(
        "SELECT * " +
                "FROM innerMenu " +
                "INNER JOIN periode " +
                "ON periode.id_periode = innerMenu.idper " +
                "INNER JOIN menu " +
                "ON menu.id_menu = innerMenu.idmen "
    )
    fun getAllInner(): Flow<List<InnerMenuData>>

    @Query(
        "SELECT " +
                "periode.id_periode AS idper, " +
                "periode.date_periode AS date, " +
                "periode.heure_periode AS heure, " +
                "periode.libelle_periode AS periode, " +
                "menu.id_menu AS idmenu, " +
                "menu.nom_menu AS menu, " +
                "menu.totalPlats AS tPlats, " +
                "menu.totalGly AS tGly, " +
                "menu.totalCal AS tCal, " +
                "FROM innerMenu " +
                "INNER JOIN periode " +
                "ON periode.id_periode = innerMenu.idper " +
                "INNER JOIN menu " +
                "ON menu.id_menu = innerMenu.idmen "
    )
    fun getAllValeurs(): Flow<List<DataMenuInner>>

}