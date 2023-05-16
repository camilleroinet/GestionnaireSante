package com.example.gestionnairesante.database.dao.profil

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profil")
data class ProfilData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_profil")
    val id_profil: Int,

    @ColumnInfo(name = "login")
    var login: String,

    @ColumnInfo(name = "password")
    var password: String,

    @ColumnInfo(name = "photo")
    var photo: String,

    @ColumnInfo(name = "taille")
    var taille: Int

    )