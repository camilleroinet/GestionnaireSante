package com.example.gestionnairesante.database.viewmodels.menu

import androidx.lifecycle.*
import com.example.gestionnairesante.Event
import com.example.gestionnairesante.database.dao.menu.MenuData
import com.example.gestionnairesante.database.dao.menu.MenuRepo
import kotlinx.coroutines.launch

class VMMenu(private val repo: MenuRepo) : ViewModel() {
    private var isUpdateOrDelete = false
    private lateinit var dataToUpdateOrDelete: MenuData

    var inputLastPoid = MutableLiveData<String?>()

    val saveOrUpdateButtonText = MutableLiveData<String>()
    private val clearAllOrDeleteButtonText = MutableLiveData<String>()
    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "rechercher"
        clearAllOrDeleteButtonText.value = "clear All"
    }

    fun insertMenu(data: MenuData) = viewModelScope.launch {

    }

    fun initUpdateAndDelete(data: MenuData) {


    }

    private fun updatePoids(data: MenuData) = viewModelScope.launch {

    }

    fun getAllMenu() = liveData {
        repo.allMenu.collect {
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