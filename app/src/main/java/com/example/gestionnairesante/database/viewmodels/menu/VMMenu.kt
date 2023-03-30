package com.example.gestionnairesante.database.viewmodels.menu

import androidx.lifecycle.*
import com.example.gestionnairesante.Event
import com.example.gestionnairesante.database.dao.menu.MenuData
import com.example.gestionnairesante.database.dao.menu.MenuRepo
import kotlinx.coroutines.launch

class VMMenu (private val repo: MenuRepo) : ViewModel() {
    private var isUpdateOrDelete = false
    private lateinit var dataToUpdateOrDelete: MenuData

    var inputLastPoid = MutableLiveData<String?>()

    val saveOrUpdateButtonText = MutableLiveData<String>()
    private val clearAllOrDeleteButtonText = MutableLiveData<String>()
    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage
    init{
        saveOrUpdateButtonText.value = "rechercher"
        clearAllOrDeleteButtonText.value = "clear All"
    }

    fun insertMenu(data: MenuData) = viewModelScope.launch {
        val newRowId = repo.insertMenu(data)
        if (newRowId > -1){
            statusMessage.value = Event("insertion ok $newRowId")
        } else {
            statusMessage.value = Event("Tache non effectuee")
        }
    }

    fun initUpdateAndDelete(data: MenuData){
        isUpdateOrDelete = true
        dataToUpdateOrDelete = data
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"

    }

    private fun updatePoids(data: MenuData) = viewModelScope.launch {
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
    fun getAllMenu() = liveData {
        repo.allMenu.collect{
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

    fun deleteGlycemie(data: MenuData) = viewModelScope.launch {
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