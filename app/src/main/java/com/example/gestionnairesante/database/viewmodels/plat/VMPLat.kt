package com.example.gestionnairesante.database.viewmodels.plat

import androidx.lifecycle.*
import com.example.gestionnairesante.Event
import com.example.gestionnairesante.database.dao.plats.PlatData
import com.example.gestionnairesante.database.dao.plats.PlatRepo
import kotlinx.coroutines.launch

class VMPLat(private val repo: PlatRepo) : ViewModel() {

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

    fun insertPlat(data: PlatData) = viewModelScope.launch {
        val newRowId = repo.insertPlat(data)
        if (newRowId > -1) {
            statusMessage.value = Event("insertion ok $newRowId")
        } else {
            statusMessage.value = Event("Tache non effectuee")
        }
    }

    fun getPlatToUpdate(data: PlatData): PlatData {
        return repo.getPlatToUpdate(data.id_plat)
    }

    fun updatePlat(id: Int, nomPlat: String, glucidePlat: Int, caloriePlat: Int) =
        viewModelScope.launch {
            val noOfRow = repo.updatePlat(id, nomPlat, glucidePlat, caloriePlat)
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

    fun getallPlat() = liveData {
        repo.allPlat.collect {
            emit(it)
        }
    }

    fun deleteGlycemie(data: PlatData) = viewModelScope.launch {
        val noOfRowDeleted = repo.deletePlat(data)
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

}