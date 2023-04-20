package com.example.gestionnairesante.ui.repas.vm

import androidx.lifecycle.*
import com.example.gestionnairesante.Event
import com.example.gestionnairesante.database.dao.innerPlat.InnerPlatMenuRepo
import com.example.gestionnairesante.database.dao.innerPlat.PlatInner
import com.example.gestionnairesante.database.dao.innerPoids.InnerPoidsRepo
import com.example.gestionnairesante.database.dao.plats.PlatData
import kotlinx.coroutines.launch

class VmRepas (private val repo: InnerPlatMenuRepo) : ViewModel(){

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    // init {

    fun deletePlat(id: Int) = viewModelScope.launch{
        repo.deletePlat(id)
    }
    fun ajouterPlat(data: PlatData) = viewModelScope.launch{
        repo.insertPlat(data)
    }
    // }
    fun getAllPlats() = liveData {
        repo.allPlat.collect {
            emit(it)
        }
    }


}