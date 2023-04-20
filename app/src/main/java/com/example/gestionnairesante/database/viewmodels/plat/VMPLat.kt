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

    }

/*    fun getPlatToUpdate(data: PlatData): PlatData {

    }*/

    fun updatePlat(id: Int, nomPlat: String, glucidePlat: Int, caloriePlat: Int) =
        viewModelScope.launch {

        }

    fun getallPlat() = liveData {
        repo.allPlat.collect {
            emit(it)
        }
    }

    fun deleteGlycemie(data: PlatData) = viewModelScope.launch {

    }

}