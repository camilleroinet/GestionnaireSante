package com.example.gestionnairesante.database.viewmodels.insuline

import androidx.lifecycle.*
import com.example.gestionnairesante.Event
import com.example.gestionnairesante.database.dao.insuline.InsulineData
import com.example.gestionnairesante.database.dao.insuline.InsulineRepo
import kotlinx.coroutines.launch

class VMInsuline(private val repo: InsulineRepo) : ViewModel() {

    private var isUpdateOrDelete = false
    private lateinit var dataToUpdateOrDelete: InsulineData

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

    fun insertInsuline(data: InsulineData) = viewModelScope.launch {
        val newRowId = repo.insertInsuline(data)
        if (newRowId > -1) {
            statusMessage.value = Event("insertion ok $newRowId")
        } else {
            statusMessage.value = Event("Tache non effectuee")
        }
    }

    fun initUpdateAndDelete(data: InsulineData) {
        isUpdateOrDelete = true
        dataToUpdateOrDelete = data
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    private fun updateGlycemie(data: InsulineData) = viewModelScope.launch {
/*        val noOfRow = repo.updateGlycemie(data)
        if (noOfRow > 0){
            inputNameData.value = 0
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "save"
            clearAllOrDeleteButtonText.value = "clear all"
            statusMessage.value = Event("$noOfRow update ok")
        }else {
            statusMessage.value = Event("Problemes")
        }*/
    }

    fun getallInsuline() = liveData {
        repo.allInsuline.collect {
            emit(it)
        }
    }

    fun getallRapide() = liveData {
        repo.allRapide.collect {
            emit(it)
        }
    }

    fun getallLente() = liveData {
        repo.allLente.collect {
            emit(it)
        }
    }

    fun clearallOrdelete() {
/*        if (isUpdateOrDelete){
            deleteGlycemie(dataToUpdateOrDelete)
        }else{
            clearAll()
        }*/
    }

    fun deleteInsuline(data: InsulineData) = viewModelScope.launch {
        val noOfRowDeleted = repo.deleteInsuline(data)
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
/*        val noOfRowDeleted = repo.deleteAllGlycemie()
        if (noOfRowDeleted > 0){
            statusMessage.value = Event("$noOfRowDeleted user supprimee")
        }else{
            statusMessage.value = Event("Probleme")
        }*/
    }


}