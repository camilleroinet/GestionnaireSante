package com.example.gestionnairesante.database.dao.innerPoids

data class PoidsInner(
    val idpoi: Int,
    val idper: Int,
    val poids: Float,
    val date: String,
    val heure: String,
    val periode: String
)