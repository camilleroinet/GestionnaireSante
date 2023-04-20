package com.example.gestionnairesante.ui.diabete.vm

import androidx.lifecycle.*
import com.example.gestionnairesante.Event
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import com.example.gestionnairesante.database.dao.innerDiabete.InnerDiabeteData
import com.example.gestionnairesante.database.dao.innerDiabete.InnerDiabeteRepo
import com.example.gestionnairesante.database.dao.insuline.InsulineData
import com.example.gestionnairesante.database.dao.periode.PeriodeData
import kotlinx.coroutines.launch

class VMDiabete( private val repo: InnerDiabeteRepo): ViewModel() {
    val valeurGlycemie = MutableLiveData<String>()
    val valeurPeriode = MutableLiveData<String>()
    val valeurDate = MutableLiveData<String>()
    val valeurHeure = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage


    init {
        valeurGlycemie.value = ""
        valeurPeriode.value = "1"
        valeurDate.value = "01/01/01"
        valeurHeure.value = "12:00"

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

    fun getAllGlycemie() = liveData {
        repo.allGlycemie.collect(){
            emit(it)
        }
    }

    fun getGlycemiePeriode() = liveData {
        repo.innerPeriodeGlycemie.collect{
            emit(it)
        }
    }

    fun updateDiabete(idgly: Int, idper: Int, idins : Int, glycemie: Int, rapide: Int, lente: Int, date: String, heure: String, periode: String) = viewModelScope.launch {
        repo.updateGlycemie(idgly, glycemie)            // update de la glycemie
        repo.updatePeriode(idper, date, heure, periode)          // update de la periode
        repo.updateInsuline(idins, rapide, lente)       // update de l'insuline
        statusMessage.value = Event("Mise à jour réussie.")
    }

    fun deleteDiabete(per: Int, gly: Int, ins: Int) = viewModelScope.launch {
        repo.deleteGlycemie(gly)
        repo.deleteInsuline(ins)
        repo.deletePeriode(per)
        statusMessage.value = Event("L'enregistrement a bien été supprimé.")
    }

}