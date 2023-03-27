@file: JvmName("UtilsPopulate")
@file: JvmMultifileClass

package com.example.gestionnairesante.utils

import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import com.example.gestionnairesante.database.viewmodels.VMGlycemie

fun populate(viewModel: VMGlycemie){


    val newInsert = GlycemieData(0, 133)
    val newInsert2 = GlycemieData(0, 157)
    val newInsert3 = GlycemieData(0, 244)
    val newInsert4 = GlycemieData(0, 99)
    val newInsert5 = GlycemieData(0, 117)
    val newInsert6 = GlycemieData(0, 159)
    val newInsert7 = GlycemieData(0, 44)


    viewModel.insertGlycemie(newInsert)
    viewModel.insertGlycemie(newInsert2)
    viewModel.insertGlycemie(newInsert3)
    viewModel.insertGlycemie(newInsert4)
    viewModel.insertGlycemie(newInsert5)
    viewModel.insertGlycemie(newInsert6)
    viewModel.insertGlycemie(newInsert7)
}
