@file: JvmName("ChartsUtils")
@file: JvmMultifileClass


package com.example.gestionnairesante.ui.poids.service

fun calculerIMC(taille: Int, poids: Float): Double {
    //  IMC = poids en kg/taille²
    val weight = poids.toDouble()
    val height = (taille.toDouble() / 100)

    return (weight / (height * height))
}

