package com.example.gestionnairesante.database.dao.innerStats

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
        "ORDER BY date_periode DESC"
    )
    fun getSpecGlycemie(date: String): Flow<List<Int>>


    @Query(
        "SELECT valeur_poids " +
        "FROM poids " +
        "INNER JOIN innerPoids " +
        "ON poids.id_poids = innerPoids.idpoi " +
        "INNER JOIN periode " +
        "ON periode.id_periode = innerPoids.idper " +
        "WHERE periode.date_periode = :date " +
        "ORDER BY date_periode DESC"
    )
    fun getSpecPoids(date: String): Flow<List<Float>>

}

