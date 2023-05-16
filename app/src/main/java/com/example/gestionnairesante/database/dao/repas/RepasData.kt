package com.example.gestionnairesante.database.dao.repas

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu")
data class RepasData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_menu")
    val id_menu: Int,

    @ColumnInfo(name = "nom_menu")
    var nom_menu: String?,

    @ColumnInfo(name = "totalPlat")
    var totalPlat: Int?,

    @ColumnInfo(name = "totalGly")
    var totalGly: Float?,

    @ColumnInfo(name = "totalCal")
    var totalCal: Float?


    )