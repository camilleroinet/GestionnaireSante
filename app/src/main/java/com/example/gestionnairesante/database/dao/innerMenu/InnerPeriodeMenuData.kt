package com.example.gestionnairesante.database.dao.innerMenu

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.gestionnairesante.database.dao.menu.MenuData
import com.example.gestionnairesante.database.dao.periode.PeriodeData

/**
 * Table relationnelle Many to Many
 * entre
 * la table Menu
 * la table Periode
 */

@Entity(
    tableName = "innerMenu",
    //primaryKeys = ["idmen", "idper"],
    foreignKeys = [
        ForeignKey(
            entity = MenuData::class,
            parentColumns = arrayOf("id_menu"),
            childColumns = arrayOf("idmen"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PeriodeData::class,
            parentColumns = arrayOf("id_periode"),
            childColumns = arrayOf("idper"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class InnerPeriodeMenuData (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk_menu")
    val id: Int,

    @ColumnInfo(name = "idmen")
    var idmen: Int,

    @ColumnInfo(name = "idper")
    var idper: Int
)