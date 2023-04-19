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
        repo.insertGlycemie(data)
        statusMessage.value = Event("Enregistrement effectué")
    }

    fun updateGlycemie(id: Int, glycemie: Int) = viewModelScope.launch {
        repo.updateGlycemie(id, glycemie)
        statusMessage.value = Event("Mise à jour des données effectuée")
    }

    fun getallGlycemie() = liveData {
        repo.allglycemie.collect {
            emit(it)
        }
    }

    fun deleteGlycemie(data: Int) = viewModelScope.launch {
        repo.deleteGlycemie(data)
        statusMessage.value = Event("Suppression reussie")
    }

    fun getAllValeurGlycemie() = liveData {
        repo.allValeurGlycemie.collect {
            emit(it)
        }
    }

}