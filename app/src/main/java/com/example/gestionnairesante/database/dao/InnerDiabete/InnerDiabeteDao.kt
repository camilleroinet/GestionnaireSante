package com.example.gestionnairesante.database.dao.InnerDiabete

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import com.example.gestionnairesante.database.dao.periode.PeriodeData
import kotlinx.coroutines.flow.Flow

@Dao
interface InnerDiabeteDao {
    @Insert
    suspend fun insertInnerDiabete(user: InnerDiabeteData) : Long

    @Query("SELECT * FROM glycemie INNER JOIN innerDiabete ON " +
            "glycemie.id_glycemie = innerDiabete.idgly WHERE " +
            "innerDiabete.idper = :id_periode")
    fun getInnerGlycemie(id_periode: Int): Flow<List<GlycemieData>>

    @Query("SELECT * FROM periode INNER JOIN innerDiabete ON " +
            "periode.id_periode = innerDiabete.idper WHERE " +
            "innerDiabete.idgly = :id_glycemie")
    fun getInnerPeriode(id_glycemie: Int): Flow<List<PeriodeData>>

    @Query("SELECT * FROM innerDiabete " +
            "INNER JOIN periode ON  periode.id_periode = innerDiabete.idper " +
            "INNER JOIN glycemie ON  glycemie.id_glycemie = innerDiabete.idgly")
    fun getAllInner(): Flow<List<InnerDiabeteData>>

    @Query("SELECT " +
            "periode.date_periode AS date, " +
            "periode.heure_periode AS heure, " +
            "periode.libelle_periode AS periode, " +
            "glycemie.valeur_glycemie AS glycemie " +
            "FROM innerDiabete " +
            "INNER JOIN periode ON  periode.id_periode = innerDiabete.idper " +
            "INNER JOIN glycemie ON  glycemie.id_glycemie = innerDiabete.idgly")
    fun getAllValeurs(): Flow<List<DataInner>>

}