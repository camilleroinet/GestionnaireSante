package com.example.gestionnairesante.database.dao.innerDiabete

data class DataInner(
    val idper: Int,
    val idgly: Int,
    val idins: Int,

    val periode: String,
    val date: String,
    val heure: String,

    val glycemie: Int,
    val rapide: Int,
    val lente: Int
)