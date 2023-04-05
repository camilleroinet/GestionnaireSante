package com.example.gestionnairesante.database.viewmodels.glycemie

import androidx.lifecycle.*
import com.example.gestionnairesante.Event
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import com.example.gestionnairesante.database.dao.glycemie.GlycemieRepo
import kotlinx.coroutines.launch

class VMGlycemie(private val repo: GlycemieRepo) : ViewModel() {

    private var isUpdateOrDelete = false

    val inputNameData = MutableLiveData<Int?>()

    val saveOrUpdateButtonText = MutableLiveData<String>()
    private val clearAllOrDeleteButtonText = MutableLiveData<String>()
    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "rechercher"
        clearAllOrDeleteButtonText.value = "clear All"
    }

    fun insertGlycemie(data: GlycemieData) = viewModelScope.launch {
        val newRowId = repo.insertGlycemie(data)
        if (newRowId > -1) {
            statusMessage.value = Event("insertion ok $newRowId")
        } else {
            statusMessage.value = Event("Tache non effectuee")
        }
    }
    fun updateGlycemie(id: Int, glycemie: Int) = viewModelScope.launch {
        val noOfRow = repo.updateGlycemie(id, glycemie)
        if (noOfRow > 0) {
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "save"
            clearAllOrDeleteButtonText.value = "clear all"
            statusMessage.value = Event("$noOfRow update ok")
        } else {
            statusMessage.value = Event("Problemes")
        }
    }

    fun getGlycemieToUpdate(data: GlycemieData): GlycemieData{
        return repo.getGlycemieToUpdate(data.id_glycemie)
    }
    fun getallGlycemie() = liveData {
        repo.allglycemie.collect {
            emit(it)
        }
    }


    fun deleteGlycemie(data: GlycemieData) = viewModelScope.launch {
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
    }

    fun getAllValeurGlycemie() = liveData {
        repo.allValeurGlycemie.collect {
            emit(it)
        }
    }

}