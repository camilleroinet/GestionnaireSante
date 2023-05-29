package com.example.gestionnairesante.database.dao.innerPoids

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gestionnairesante.database.dao.periode.PeriodeData
import com.example.gestionnairesante.database.dao.poids.PoidsData
import kotlinx.coroutines.flow.Flow

@Dao
interface InnerPoidsDao {

    @Insert
    suspend fun insertInnerPoids(data: InnerPoidsData)

    @Query(
        "SELECT * " +
        "FROM poids " +
        "INNER JOIN innerPoids " +
        "ON poids.id_poids = innerPoids.idpoi " +
        "WHERE innerPoids.idper = :id_periode"
    )
    fun getInnerPoids(id_periode: Int): Flow<List<PoidsData>>

    @Query(
        "SELECT * " +
        "FROM periode " +
        "INNER JOIN innerPoids " +
        "ON periode.id_periode = innerPoids.idper " +
        "WHERE innerPoids.idpoi = :id_poids"
    )
    fun getInnerPeriode(id_poids: Int): Flow<List<PeriodeData>>

    @Query(
        "SELECT * " +
        "FROM innerPoids " +
        "INNER JOIN periode " +
        "ON periode.id_periode = innerPoids.idper " +
        "INNER JOIN poids " +
        "ON poids.id_poids = innerPoids.idpoi"
    )
    fun getInnerAllInner(): Flow<List<InnerPoidsData>>

    @Query(
        "SELECT " +
            "poids.id_poids AS idpoi, " +
            "poids.valeur_poids AS poids, " +
            "periode.id_periode AS idper, " +
            "periode.date_periode AS date, " +
            "periode.heure_periode AS heure, " +
            "periode.libelle_periode AS periode " +
        "FROM innerPoids " +
        "INNER JOIN poids " +
        "ON poids.id_poids = innerPoids.idpoi " +
        "INNER JOIN periode " +
        "ON periode.id_periode = innerPoids.idper " +
        "ORDER BY periode.date_periode DESC"
    )
    fun getAllPoids(): Flow<List<PoidsInner>>
}