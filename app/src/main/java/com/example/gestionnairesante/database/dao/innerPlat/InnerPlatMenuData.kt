package com.example.gestionnairesante.database.dao.innerPlat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.gestionnairesante.database.dao.menu.MenuData
import com.example.gestionnairesante.database.dao.plats.PlatData

/**
 * Table relationnelle Many to Many
 * entre
 * la table repas
 * la table menu
 */

@Entity(
    tableName = "innerPlat",
    primaryKeys = ["idpla", "idmen"],
    foreignKeys = [
        ForeignKey(
            entity = PlatData::class,
            parentColumns = arrayOf("id_plat"),
            childColumns = arrayOf("idpla"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MenuData::class,
            parentColumns = arrayOf("id_menu"),
            childColumns = arrayOf("idmen"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class InnerPlatMenuData(
    @ColumnInfo(name = "idpla")
    var idPoi: Int,

    @ColumnInfo(name = "idmen")
    var idPer: Int
)