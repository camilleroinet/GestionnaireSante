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
            0,
            glycemie
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

    fun updateGlycemie(id: Int, glycemie: Int) = viewModelScope.launch {
        /*val noOfRow = repo.updateGlycemie(id, glycemie)
        if (noOfRow > 0) {
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "save"
            clearAllOrDeleteButtonText.value = "clear all"
            statusMessage.value = Event("$noOfRow update ok")
        } else {
            statusMessage.value = Event("Problemes")
        }*/
    }

/*    fun getGlycemieToUpdate(data: GlycemieData): GlycemieData {
        return repo.getGlycemieToUpdate(data.id_glycemie)
    }*/

/*    fun getallGlycemie() = liveData {
        repo.allglycemie.collect {
            emit(it)
        }
    }*/

    fun deleteDiabete(per: Int, gly: Int, ins: Int) = viewModelScope.launch {
        repo.deleteGlycemie(gly)
        repo.deleteInsuline(ins)
        repo.deletePeriode(per)
    }

/*    fun getAllValeurGlycemie() = liveData {
        repo.allValeurGlycemie.collect {
            emit(it)
        }
    }*/

}