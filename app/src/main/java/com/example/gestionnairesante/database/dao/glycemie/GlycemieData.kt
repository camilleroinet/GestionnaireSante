package com.example.gestionnairesante.database.dao.glycemie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "glycemie")
data class GlycemieData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_glycemie")
    val id_glycemie: Int,

    @ColumnInfo(name = "valeur_glycemie")
    var valeur_glycemie: Int,
)
