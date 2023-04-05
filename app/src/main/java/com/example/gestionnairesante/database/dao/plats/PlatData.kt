package com.example.gestionnairesante.database.dao.plats

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plat")
data class PlatData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_plat")
    val id_plat: Int,

    @ColumnInfo(name = "nom_plat")
    var nom_plat: String?,

    @ColumnInfo(name = "glucide_plat")
    var glucide_plat: Int?,

    @ColumnInfo(name = "calorie_plat")
    var calorie_plat: Int?,
)