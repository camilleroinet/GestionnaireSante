package com.example.gestionnairesante.database.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poids")
data class PoidsData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_poids")
    val id_poids: Int,

    @ColumnInfo(name = "valeur_poids")
    var valeur_poids: Float?,
)