package com.example.gestionnairesante.database.dao.innerDiabete

import android.provider.ContactsContract.Data
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import com.example.gestionnairesante.database.dao.insuline.InsulineData
import com.example.gestionnairesante.database.dao.periode.PeriodeData
import kotlinx.coroutines.flow.Flow
import java.nio.channels.DatagramChannel

@Dao
interface InnerDiabeteDao {
    @Insert
    suspend fun insertInnerDiabete(user: InnerDiabeteData)

    @Query(
        "SELECT * FROM glycemie INNER JOIN innerDiabete " +
                "ON glycemie.id_glycemie = innerDiabete.idgly " +
                "WHERE innerDiabete.idper = :id_periode"
    )
    fun getInnerGlycemie(id_periode: Int): Flow<List<GlycemieData>>

    @Query(
        "SELECT * FROM periode " +
                "INNER JOIN innerDiabete " +
                "ON periode.id_periode = innerDiabete.idper " +
                "WHERE innerDiabete.idgly = :id_glycemie"
    )
    fun getInnerPeriode(id_glycemie: Int): Flow<List<PeriodeData>>

    @Query(
        "SELECT * FROM insuline INNER JOIN innerDiabete " +
                "ON insuline.id_insuline = innerDiabete.idIns " +
                "WHERE innerDiabete.idIns = :id_insuline"
    )
    fun getInnerInsuline(id_insuline: Int): Flow<List<InsulineData>>

    @Query(
        "SELECT * FROM innerDiabete " +
                "INNER JOIN periode ON periode.id_periode = innerDiabete.idper " +
                "INNER JOIN glycemie ON glycemie.id_glycemie = innerDiabete.idgly " +
                "INNER JOIN insuline ON insuline.id_insuline = innerDiabete.idIns "
    )
    fun getAllInner(): Flow<List<InnerDiabeteData>>

    @Query(
        "SELECT " +
                "periode.id_periode AS idper, " +
                "periode.date_periode AS date, " +
                "periode.heure_periode AS heure, " +
                "periode.libelle_periode AS periode, " +
                "glycemie.id_glycemie AS idgly, " +
                "glycemie.valeur_glycemie AS glycemie, " +
                "insuline.id_insuline AS idins, " +
                "insuline_rapide AS rapide, " +
                "insuline_lente AS lente " +
                "FROM innerDiabete " +
                "INNER JOIN periode ON periode.id_periode = innerDiabete.idper " +
                "INNER JOIN glycemie ON glycemie.id_glycemie = innerDiabete.idgly " +
                "INNER JOIN insuline ON insuline.id_insuline = innerDiabete.idIns "
    )
    fun getAllValeurs(): Flow<List<DataInner>>

}