package com.example.gestionnairesante.database.viewmodels.poids

import androidx.lifecycle.*
import com.example.gestionnairesante.Event
import com.example.gestionnairesante.database.dao.poids.PoidsData
import com.example.gestionnairesante.database.dao.poids.PoidsRepo
import kotlinx.coroutines.launch

class VMPoids(private val repo: PoidsRepo) : ViewModel() {
    private var isUpdateOrDelete = false
    val saveOrUpdateButtonText = MutableLiveData<String>()
    private val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "rechercher"
        clearAllOrDeleteButtonText.value = "clear All"
    }

    fun insertPoids(data: PoidsData) = viewModelScope.launch {
        repo.insertPoids(data)
        statusMessage.value = Event("Enregistrement ")
    }

    fun getPoidsToUpdate(data: PoidsData): PoidsData {
        return repo.getPoidsToUpdate(data.id_poids)
    }

    fun updatePoids(id: Int, poids: Float) = viewModelScope.launch {
        repo.updatePoids(id, poids)
        statusMessage.value = Event("Mise à jour reussie")
    }

    fun getAllPoids() = liveData {
        repo.allPoids.collect {
            emit(it)
        }
    }

    fun getValeurPoids() = liveData {
        repo.allValeurPoids.collect {
            emit(it)
        }
    }

    fun deletePoids(id: Int) = viewModelScope.launch {
        repo.deletePoids(id)
        statusMessage.value = Event("Suppression réussie")
    }

}