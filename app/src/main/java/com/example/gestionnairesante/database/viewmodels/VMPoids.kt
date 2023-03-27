package com.example.gestionnairesante.database.viewmodels

import androidx.lifecycle.*
import com.example.gestionnairesante.Event
import com.example.gestionnairesante.database.dao.PoidsData
import com.example.gestionnairesante.database.dao.PoidsRepo
import kotlinx.coroutines.launch

class VMPoids (private val repo: PoidsRepo) : ViewModel() {

    private var isUpdateOrDelete = false
    private lateinit var dataToUpdateOrDelete: PoidsData

    val inputNameData = MutableLiveData<Int?>()

    val saveOrUpdateButtonText = MutableLiveData<String>()
    private val clearAllOrDeleteButtonText = MutableLiveData<String>()
    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage
    init{
        saveOrUpdateButtonText.value = "rechercher"
        clearAllOrDeleteButtonText.value = "clear All"
    }

    fun insertPoids(data: PoidsData) = viewModelScope.launch {
        val newRowId = repo.insertPoids(data)
        if (newRowId > -1){
            statusMessage.value = Event("insertion ok $newRowId")
        } else {
            statusMessage.value = Event("Tache non effectuee")
        }
    }

    fun initUpdateAndDelete(data: PoidsData){
        isUpdateOrDelete = true
        dataToUpdateOrDelete = data
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    private fun updatePoids(data: PoidsData) = viewModelScope.launch {
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
    fun getAllPoids() = liveData {
        repo.allPoids.collect{
            emit(it)
        }
    }
    fun getValeurPoids() = liveData {
    repo.allValeurPoids.collect{
            emit(it)
        }
    }
    fun clearallOrdelete(){
/*        if (isUpdateOrDelete){
            deleteGlycemie(dataToUpdateOrDelete)
        }else{
            clearAll()
        }*/
    }

    fun deleteGlycemie(data: PoidsData) = viewModelScope.launch {
/*        val noOfRowDeleted = repo.deleteInsuline(data)
        if (noOfRowDeleted > 0){
            inputNameData.value = 0
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "save"
            clearAllOrDeleteButtonText.value = "clear all"
            statusMessage.value = Event("$noOfRowDeleted Row supprimee")
        }else{
            statusMessage.value = Event("Probleme")
        }*/
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