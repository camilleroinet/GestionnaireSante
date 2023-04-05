package com.example.gestionnairesante.database.viewmodels.glycemie

import androidx.lifecycle.*
import com.example.gestionnairesante.Event
import com.example.gestionnairesante.database.dao.glycemie.GlycemieData
import com.example.gestionnairesante.database.dao.glycemie.GlycemieRepo
import kotlinx.coroutines.launch

class VMGlycemie(private val repo: GlycemieRepo) : ViewModel() {

    private var isUpdateOrDelete = false
    private lateinit var dataToUpdateOrDelete: GlycemieData

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

    fun initUpdateAndDelete(data: GlycemieData) {
        isUpdateOrDelete = true
        dataToUpdateOrDelete = data
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    private fun updateGlycemie(data: GlycemieData) = viewModelScope.launch {
        val noOfRow = repo.updateGlycemie(data)
        if (noOfRow > 0) {
            inputNameData.value = 0
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "save"
            clearAllOrDeleteButtonText.value = "clear all"
            statusMessage.value = Event("$noOfRow update ok")
        } else {
            statusMessage.value = Event("Problemes")
        }
    }

    fun getallGlycemie() = liveData {
        repo.allglycemie.collect {
            emit(it)
        }
    }

    fun clearallOrdelete() {
        if (isUpdateOrDelete) {
            deleteGlycemie(dataToUpdateOrDelete)
        } else {
            clearAll()
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

    private fun clearAll() = viewModelScope.launch {
        val noOfRowDeleted = repo.deleteAllGlycemie()
        if (noOfRowDeleted > 0) {
            statusMessage.value = Event("$noOfRowDeleted user supprimee")
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