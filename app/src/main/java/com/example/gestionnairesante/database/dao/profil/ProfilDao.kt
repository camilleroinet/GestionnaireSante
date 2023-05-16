package com.example.gestionnairesante.database.dao.profil

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gestionnairesante.database.dao.poids.PoidsData
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfilDao {

    @Insert
    suspend fun insertProfil(user: ProfilData)

    @Query("UPDATE profil SET login = :login, password = :password, photo = :photo, taille = :taille WHERE id_profil = :id")
    suspend fun updateProfil(id: Int, login: String, password: String, photo: String, taille: Int)

}