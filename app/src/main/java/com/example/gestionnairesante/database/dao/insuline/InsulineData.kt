package com.example.gestionnairesante.database.dao.insuline

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "insuline")
data class InsulineData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_insuline")
    val id_insuline: Int,

    @ColumnInfo(name = "insuline_rapide")
    var insuline_rapide: Int,

    @ColumnInfo(name = "insuline_lente")
    var insuline_lente: Int,



    )
