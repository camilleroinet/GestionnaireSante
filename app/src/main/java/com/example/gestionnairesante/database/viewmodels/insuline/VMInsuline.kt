package com.example.gestionnairesante.database.viewmodels.insuline

import androidx.lifecycle.*
import com.example.gestionnairesante.Event
import com.example.gestionnairesante.database.dao.insuline.InsulineData
import com.example.gestionnairesante.database.dao.insuline.InsulineRepo
import kotlinx.coroutines.launch

class VMInsuline(private val repo: InsulineRepo) : ViewModel() {

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

    fun insertInsuline(data: InsulineData) = viewModelScope.launch {
        repo.insertInsuline(data)
        statusMessage.value = Event("Enregistrement effectué")
    }


    fun updateInsuline(id: Int, rapide: Int, lente: Int) = viewModelScope.launch {
        repo.insulineUpdate(id, rapide, lente)
        statusMessage.value = Event("Mise à jour des données effectuée")
    }


}