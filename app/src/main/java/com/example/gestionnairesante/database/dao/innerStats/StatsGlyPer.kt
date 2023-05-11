package com.example.gestionnairesante.database.dao.innerStats

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import com.example.gestionnairesante.database.dao.insuline.InsulineData
import com.example.gestionnairesante.database.dao.periode.PeriodeData

/**
 * Table relationnelle Many to Many
 * entre
 * la table diabete
 * la table periode
 * la table
 */

@Entity(
    tableName = "statsGlyPer",
    primaryKeys = ["idgly", "idper"],
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
        )
    ])

data class StatsGlyPer (
    @ColumnInfo(name = "idgly")
    var idGly: Int,

    @ColumnInfo(name = "idper")
    var idPer: Int,

)