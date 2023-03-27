@file: JvmName("ChartsUtils")
@file: JvmMultifileClass

package com.example.gestionnairesante.utils

import android.content.Context
import com.example.gestionnairesante.R


fun createColorTab(context: Context, taille: Int) : ArrayList<Int> {
    val tabCouleur = ArrayList<Int>()
    val tabColor : IntArray = context.resources.getIntArray(R.array.tabCouleur)

    for(i in 0..taille){

        tabCouleur.add(tabColor[i])
    }
    return tabCouleur
}