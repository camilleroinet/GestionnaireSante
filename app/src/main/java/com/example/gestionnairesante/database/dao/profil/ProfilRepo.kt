package com.example.gestionnairesante.database.dao.profil

import com.example.gestionnairesante.database.dao.poids.PoidsDao
import com.example.gestionnairesante.database.dao.poids.PoidsData

class ProfilRepo(private val dao: ProfilDao) {

    suspend fun insertProfil(data: ProfilData) {
        return dao.insertProfil(data)
    }

    suspend fun updateProfil(id: Int, login: String, password: String, photo: String, taille: Int) {
        return dao.updateProfil(id, login, password, photo, taille)
    }

}