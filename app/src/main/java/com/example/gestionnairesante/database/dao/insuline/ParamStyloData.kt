package com.example.gestionnairesante.database.dao.insuline

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "stylo")
data class ParamStyloData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_stylo")
    val id_stylo: Int,

    @ColumnInfo(name = "quantite")
    var ins_quantite: Int,

    @ColumnInfo(name = "maxquantite")
    var ins_max: Int,

    @ColumnInfo(name = "purge")
    var ind_pruge: Int,

    )
