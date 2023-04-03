package com.example.gestionnairesante.database.viewmodels.poids

import androidx.lifecycle.*
import com.example.gestionnairesante.Event
import com.example.gestionnairesante.database.dao.poids.PoidsData
import com.example.gestionnairesante.database.dao.poids.PoidsRepo
import kotlinx.coroutines.launch

class VMPoids (private val repo: PoidsRepo) : ViewModel() {
    private var isUpdateOrDelete = false
    private lateinit var dataToUpdateOrDelete: PoidsData

    var inputLastPoid = MutableLiveData<String?>()

    val saveOrUpdateButtonText = MutableLiveData<String>()
    private val clearAllOrDeleteButtonText = MutableLiveData<String>()
    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage
    init{
        saveOrUpdateButtonText.value = "rechercher"
        clearAllOrDeleteButtonText.value = "clear All"
        inputLastPoid.value = "couchhhhhou"
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
        inputLastPoid.value = "couchhhhhou"

    }

    fun updatePoids(id:Int, poids: Float) = viewModelScope.launch {
        val noOfRow = repo.updatePoids(id, poids)
        if (noOfRow > 0){
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "save"
            clearAllOrDeleteButtonText.value = "clear all"
            statusMessage.value = Event("$noOfRow update ok")
        }else {
            statusMessage.value = Event("Problemes")
        }
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
    fun getValeurPoidsDesc() = liveData {
        repo.allValeurPoidsDesc.collect{
            emit(it)
        }
    }

    fun clearallOrdelete(){
        if (isUpdateOrDelete){
            deletePoids(dataToUpdateOrDelete)
        }else{
            clearAll()
        }
    }

    fun deletePoids(data: PoidsData) = viewModelScope.launch {
        val noOfRowDeleted = repo.deletePoids(data)
        if (noOfRowDeleted > 0){
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "save"
            clearAllOrDeleteButtonText.value = "clear all"
            statusMessage.value = Event("$noOfRowDeleted Row supprimee")
        }else{
            statusMessage.value = Event("Probleme")
        }
    }

    fun deleteAllPoids(id: Int) = viewModelScope.launch {
        val noOfRowDeleted = repo.deleteAllPoids(id)
        if (noOfRowDeleted > 0){
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "save"
            clearAllOrDeleteButtonText.value = "clear all"
            statusMessage.value = Event("$noOfRowDeleted Row supprimee")
        }else{
            statusMessage.value = Event("Probleme")
        }
    }

    private fun clearAll() = viewModelScope.launch {
/*        val noOfRowDeleted = repo.deleteAllPoids()
        if (noOfRowDeleted > 0){
            statusMessage.value = Event("$noOfRowDeleted user supprimee")
        }else{
            statusMessage.value = Event("Probleme")
        }*/
    }

}