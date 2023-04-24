package com.example.gestionnairesante.database.dao.innerPlat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
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
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_service")
    val id_service: Int,

    @ColumnInfo(name = "idpla")
    var idpla: Int,

    @ColumnInfo(name = "idmen")
    var idmen: Int
)