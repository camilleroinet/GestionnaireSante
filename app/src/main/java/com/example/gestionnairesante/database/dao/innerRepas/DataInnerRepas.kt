package com.example.gestionnairesante.database.dao.innerRepas

data class DataMenuInner(
    val idper: Int,
    val idmen: Int,

    val periode: String,
    val date: String,
    val heure: String,

    val nomMenu: String,
    val tplat: Int,
    val tgly: Int,
    val tcal: Int

)