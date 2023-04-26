package com.example.gestionnairesante.ui.repas.vm

import androidx.lifecycle.*
import com.example.gestionnairesante.Event
import com.example.gestionnairesante.database.dao.innerPlat.InnerPlatMenuData
import com.example.gestionnairesante.database.dao.innerPlat.InnerPlatMenuRepo
import com.example.gestionnairesante.database.dao.innerPlat.PlatInner
import com.example.gestionnairesante.database.dao.innerPoids.InnerPoidsRepo
import com.example.gestionnairesante.database.dao.menu.MenuData
import com.example.gestionnairesante.database.dao.plats.PlatData
import kotlinx.coroutines.launch

class VmRepas (private val repo: InnerPlatMenuRepo) : ViewModel(){
    var totalPlats = MutableLiveData<String>()
    var totalCalories = MutableLiveData<String>()
    var totalGlucides = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        totalPlats.value = ""
        totalCalories.value = ""
        totalGlucides.value = ""

    }

    fun deletePlat(id: Int) = viewModelScope.launch{
        repo.deletePlat(id)
    }

    fun deletePlatObj(data: PlatData) = viewModelScope.launch{
        repo.deletePlatObj(data)
    }

    fun ajouterPlat(data: PlatData) = viewModelScope.launch{
        repo.insertPlat(data)
    }

    fun ajouterMenu(data: MenuData) = viewModelScope.launch{
        repo.insertMenu(data)
    }

    fun ajouterInnerPlatMenu(data: InnerPlatMenuData) = viewModelScope.launch{
        repo.insertInnerPlatMenu(data)
    }

    fun getAllPlats() = liveData {
        repo.allPlat.collect {
            emit(it)
        }
    }

    fun getAllMenu() = liveData {
        repo.allMenu.collect {
            emit(it)
        }
    }

    fun getLastMenuInCurrent() : Int{
        return repo.getLastMenuInCurrent()
    }

    fun getPlatInMenu() = liveData {
        repo.getPlatInMenu().collect() {
            emit(it)
        }
    }

    fun composerMenu(plat: PlatData)  = viewModelScope.launch {
        val lastMenu = repo.getLastMenu()
        val inner = InnerPlatMenuData(0,plat.id_plat, lastMenu)
        repo.insertInnerPlatMenu(inner)
    }

    fun deletePlatInCurrent(id: Int) = viewModelScope.launch {
        val lastMenu = repo.getLastMenu()
        repo.deletePlatInCurrent(id)
    }

}