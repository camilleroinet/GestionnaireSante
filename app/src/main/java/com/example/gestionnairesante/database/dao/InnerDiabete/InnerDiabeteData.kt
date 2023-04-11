package com.example.gestionnairesante.database.dao.InnerDiabete

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import com.example.gestionnairesante.database.dao.periode.PeriodeData


/**
 * Table relationnelle Many to Many
 * entre
 * la table diabete
 * la table insuline
 * la table periode
 */

@Entity(tableName = "innerDiabete",
    primaryKeys = ["idgly", "idper"],
    foreignKeys = [
        ForeignKey(
            entity = GlycemieData::class,
            parentColumns = arrayOf("id_glycemie"),
            childColumns = arrayOf("idgly"),
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = PeriodeData::class,
            parentColumns = arrayOf("id_periode"),
            childColumns = arrayOf("idper"),
            onDelete = ForeignKey.NO_ACTION
        )
    ])

data class InnerDiabeteData (
    @ColumnInfo(name = "idgly")
    val idGly: Int,

    @ColumnInfo(name = "idper")
    var idPer: Int
)