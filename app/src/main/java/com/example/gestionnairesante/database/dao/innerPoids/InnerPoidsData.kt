package com.example.gestionnairesante.database.dao.innerPoids

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import com.example.gestionnairesante.database.dao.insuline.InsulineData
import com.example.gestionnairesante.database.dao.periode.PeriodeData
import com.example.gestionnairesante.database.dao.poids.PoidsData

/**
 * Table relationnelle Many to Many
 * entre
 * la table poids
 * la table periode
 */

@Entity(
    tableName = "innerPoids",
    primaryKeys = ["idpoi", "idper"],
    foreignKeys = [
        ForeignKey(
            entity = PoidsData::class,
            parentColumns = arrayOf("id_poids"),
            childColumns = arrayOf("idpoi"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PeriodeData::class,
            parentColumns = arrayOf("id_periode"),
            childColumns = arrayOf("idper"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class InnerPoidsData(
    @ColumnInfo(name = "idpoi")
    var idPoi: Int,

    @ColumnInfo(name = "idper")
    var idPer: Int
)