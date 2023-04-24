package com.example.gestionnairesante.ui.sport

import androidx.lifecycle.*
import androidx.room.Query
import com.example.gestionnairesante.Event
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import com.example.gestionnairesante.database.dao.innerDiabete.InnerDiabeteData
import com.example.gestionnairesante.database.dao.innerDiabete.InnerDiabeteRepo
import com.example.gestionnairesante.database.dao.innerPlat.InnerPlatMenuData
import com.example.gestionnairesante.database.dao.innerPlat.InnerPlatMenuRepo
import com.example.gestionnairesante.database.dao.insuline.InsulineData
import com.example.gestionnairesante.database.dao.menu.MenuData
import com.example.gestionnairesante.database.dao.periode.PeriodeData
import com.example.gestionnairesante.database.dao.plats.PlatData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SportViewModel (private val repo: InnerDiabeteRepo) : ViewModel(){
    var totalPlats = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        totalPlats.value = "coucou"
    }

    fun insertDiabete(periode: String, date: String, heure: String, glycemie: Int, rapide: Int, lente: Int) = viewModelScope.launch {
        //
        // Periode de prise
        //
        val newPeriode = PeriodeData(0, periode,date,heure)
        repo.insertPeriode(newPeriode)//insertion de la periode
        val lastPeriode = repo.getLastPeriode()

        //
        // Insertiob de la glycemie
        //
        val newGlycemie = GlycemieData(
            0, glycemie
        )
        repo.insertGlycemie(newGlycemie)
        val lastGlycemie = repo.getLastGlycemie()//insertion de la glycemie

        //
        // Insertion de l'insulibes
        //
        val newInsuline = InsulineData(0, rapide, lente)
        repo.insertInsuline(newInsuline)
        val lastInsuline = repo.getLastInsuline()

        //
        // Inner join
        //
        val inner = InnerDiabeteData(lastGlycemie, lastPeriode, lastInsuline)
        repo.insertGlycemieInner(inner)//insertion de la glycemie Inner Join
        statusMessage.value = Event("L'enregistrement s'est bien déroulé.")
    }

    fun getGlycemiePeriode() = liveData {
        repo.innerPeriodeGlycemie.collect{
            emit(it)
        }
    }
    fun getAll() = liveData {
        repo.allInner.collect{
            emit(it)
        }
    }

    fun deleteDiabete(per: Int, gly: Int, ins: Int) = viewModelScope.launch {
        repo.deleteGlycemie(gly)
        repo.deleteInsuline(ins)
        repo.deletePeriode(per)
        statusMessage.value = Event("L'enregistrement a bien été supprimé.")
    }


}