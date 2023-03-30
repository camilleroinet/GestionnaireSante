package com.example.gestionnairesante.database.dao.menu

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu")
data class MenuData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_menu")
    val id_menu: Int,

    @ColumnInfo(name = "nom_menu")
    var nom_menu: String?,

    )