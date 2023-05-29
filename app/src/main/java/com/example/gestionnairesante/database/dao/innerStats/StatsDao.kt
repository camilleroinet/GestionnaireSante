package com.example.gestionnairesante.database.dao.innerStats

import androidx.loader.content.Loader.ForceLoadContentObserver
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StatsDao {

    /**
     * Requete initiale
     *
     * Retourne toutes les glycemies
     * par ordre decroissant de date
     */
    @Query(
        "SELECT " +
        "valeur_glycemie FROM glycemie " +
        "INNER JOIN innerDiabete " +
        "ON glycemie.id_glycemie = innerDiabete.idgly " +
        "INNER JOIN periode " +
        "ON periode.id_periode = innerDiabete.idper " +
        "ORDER BY date_periode DESC"
    )
    fun getAllGlycemie(): Flow<List<Int>>

    /**
     * Retourne toutes les glycemies
     * en fonction d'une date
     * par ordre decroissant de date
     */
    @Query(
        "SELECT valeur_glycemie " +
        "FROM glycemie " +
        "INNER JOIN innerDiabete " +
        "ON glycemie.id_glycemie = innerDiabete.idgly " +
        "INNER JOIN periode " +
        "ON periode.id_periode = innerDiabete.idper " +
        "WHERE periode.date_periode = :date " +
        "ORDER BY periode.libelle_periode ASC"
    )
    fun getSpecGlycemie(date: String): Flow<List<Int>>

    @Query(
        "SELECT insuline_rapide " +
        "FROM glycemie " +
        "INNER JOIN innerDiabete " +
        "ON glycemie.id_glycemie = innerDiabete.idgly " +
        "INNER JOIN periode " +
        "ON periode.id_periode = innerDiabete.idper " +
        "INNER JOIN insuline " +
        "ON insuline.id_insuline = innerDiabete.idIns " +
        "WHERE periode.date_periode = :date " +
        "ORDER BY date_periode DESC"
    )
    fun getSpecRapide(date: String): Flow<List<Int>>

    @Query(
        "SELECT insuline_lente " +
        "FROM glycemie " +
        "INNER JOIN innerDiabete " +
        "ON glycemie.id_glycemie = innerDiabete.idgly " +
        "INNER JOIN periode " +
        "ON periode.id_periode = innerDiabete.idper " +
        "INNER JOIN insuline " +
        "ON insuline.id_insuline = innerDiabete.idIns " +
        "WHERE periode.date_periode = :date " +
        "ORDER BY date_periode DESC"
    )
    fun getSpecLente(date: String): Flow<List<Int>>


    @Query(
        "SELECT valeur_poids " +
        "FROM poids " +
        "INNER JOIN innerPoids " +
        "ON poids.id_poids = innerPoids.idpoi " +
        "INNER JOIN periode " +
        "ON periode.id_periode = innerPoids.idper " +
        "WHERE periode.date_periode = :date " +
        "AND id_poids = (SELECT MAX(id_poids) FROM poids)"
    )
    fun getSpecPoids(date: String): Float

    @Query(
        "SELECT poids.valeur_poids  " +
        "FROM poids " +
        "INNER JOIN innerPoids " +
        "ON poids.id_poids = innerPoids.idpoi " +
        "INNER JOIN periode " +
        "ON periode.id_periode = innerPoids.idper " +
        "WHERE periode.date_periode = :date " +
        "ORDER BY date_periode DESC"
    )
    fun getSpecPoids2(date: String): Flow<List<Float>>

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

    @Query(
        "SELECT valeur_glycemie  " +
        "FROM periode " +
        "INNER JOIN innerDiabete " +
        "ON id_periode = innerDiabete.idper " +
        "INNER JOIN glycemie " +
        "ON glycemie.id_glycemie = innerDiabete.idgly " +
        "WHERE date_periode = :date " +
        "ORDER BY date_periode DESC "
    )
    fun getGlycemiePeriode(date: String) : Flow<List<Int>>


}

