package com.example.gestionnairesante.database.dao.periode

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "periode")
data class PeriodeData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_periode")
    val id_periode: Int,

    @ColumnInfo(name = "libelle_periode")
    var libelle_periode: String,

    @ColumnInfo(name = "date_periode")
    var date_periode: String,

    @ColumnInfo(name = "heure_periode")
    var heure_periode: String,

    )