package com.example.gestionnairesante.ui.diabete.vm

import androidx.lifecycle.*
import com.example.gestionnairesante.Event
import com.example.gestionnairesante.database.dao.InnerDiabete.InnerDiabeteData
import com.example.gestionnairesante.database.dao.InnerDiabete.InnerDiabeteRepo
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import com.example.gestionnairesante.database.dao.periode.PeriodeData
import kotlinx.coroutines.launch

class VMDiabete( private val repo: InnerDiabeteRepo): ViewModel() {
    val valeurGlycemie = MutableLiveData<String>()
    val valeurPeriode = MutableLiveData<String>()
    val valeurDate = MutableLiveData<String>()
    val valeurHeure = MutableLiveData<String>()

    val lastPeriode = MutableLiveData<Int>()
    val lastGlycemie =  MutableLiveData<Int>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        valeurGlycemie.value = ""
        valeurPeriode.value = "1"
        valeurDate.value = "01/01/01"
        valeurHeure.value = "12:00"
    }

    fun insertDiabete(periode: String, date: String, heure: String, per: String, glycemie: Int) = viewModelScope.launch {
        val newPeriode = PeriodeData(0, periode,date,heure,per)
        repo.insertPeriode(newPeriode)//insertion de la periode
        val lastPeriode = repo.getLastPeriode()
        val newGlycemie = GlycemieData(
            0,
            glycemie
        )
        repo.insertGlycemie(newGlycemie)
        val lastGlycemie = repo.getLastGlycemie()//insertion de la glycemie
        val inner = InnerDiabeteData(lastGlycemie, lastPeriode)
        repo.insertGlycemieInner(inner)//insertion de la glycemie Inner Join
    }

    fun getAllGlycemie() = liveData {
        repo.allPeriode.collect{
            emit(it)
        }
    }
    fun getGlycemie() = liveData {
        repo.allGlycemie.collect{
            emit(it)
        }
    }

    fun getGlycemieInnerJoin() = liveData {
        repo.innerGlycemie.collect{
            emit(it)
        }
    }

    fun getGlycemiePeriode() = liveData {
        repo.innerPeriodeGlycemie.collect{
            emit(it)
        }
    }
/*    fun updateGlycemie(id: Int, glycemie: Int) = viewModelScope.launch {
        val noOfRow = repo.updateGlycemie(id, glycemie)
        if (noOfRow > 0) {
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "save"
            clearAllOrDeleteButtonText.value = "clear all"
            statusMessage.value = Event("$noOfRow update ok")
        } else {
            statusMessage.value = Event("Problemes")
        }
    }*/

/*    fun getGlycemieToUpdate(data: GlycemieData): GlycemieData {
        return repo.getGlycemieToUpdate(data.id_glycemie)
    }*/

/*    fun getallGlycemie() = liveData {
        repo.allglycemie.collect {
            emit(it)
        }
    }*/

/*    fun deleteGlycemie(data: GlycemieData) = viewModelScope.launch {
        val noOfRowDeleted = repo.deleteGlycemie(data)
        if (noOfRowDeleted > 0) {
            inputNameData.value = 0
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "save"
            clearAllOrDeleteButtonText.value = "clear all"
            statusMessage.value = Event("$noOfRowDeleted Row supprimee")
        } else {
            statusMessage.value = Event("Probleme")
        }
    }*/

/*    fun getAllValeurGlycemie() = liveData {
        repo.allValeurGlycemie.collect {
            emit(it)
        }
    }*/

}