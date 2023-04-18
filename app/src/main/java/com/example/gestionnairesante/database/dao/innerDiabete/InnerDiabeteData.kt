package com.example.gestionnairesante.database.dao.innerDiabete

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import com.example.gestionnairesante.database.dao.insuline.InsulineDao
import com.example.gestionnairesante.database.dao.insuline.InsulineData
import com.example.gestionnairesante.database.dao.periode.PeriodeData


/**
 * Table relationnelle Many to Many
 * entre
 * la table diabete
 * la table insuline
 * la table periode
 */

@Entity(tableName = "innerDiabete",
    primaryKeys = ["idgly", "idper", "idIns"],
    foreignKeys = [
        ForeignKey(
            entity = GlycemieData::class,
            parentColumns = arrayOf("id_glycemie"),
            childColumns = arrayOf("idgly"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PeriodeData::class,
            parentColumns = arrayOf("id_periode"),
            childColumns = arrayOf("idper"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = InsulineData::class,
            parentColumns = arrayOf("id_insuline"),
            childColumns = arrayOf("idIns"),
            onDelete = ForeignKey.CASCADE
        )
    ])

data class InnerDiabeteData (
    @ColumnInfo(name = "idgly")
    val idGly: Int,

    @ColumnInfo(name = "idper")
    var idPer: Int,

    @ColumnInfo(name = "idIns")
    var idIns: Int
)