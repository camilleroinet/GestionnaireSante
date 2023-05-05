package com.example.gestionnairesante.ui.repas.vm

import androidx.lifecycle.*
import com.example.gestionnairesante.Event
import com.example.gestionnairesante.database.dao.innerMenu.InnerPeriodeMenuData
import com.example.gestionnairesante.database.dao.innerMenu.InnerPeriodeMenuRepo
import com.example.gestionnairesante.database.dao.innerPlat.InnerPlatMenuData
import com.example.gestionnairesante.database.dao.innerPlat.InnerPlatMenuRepo
import com.example.gestionnairesante.database.dao.menu.MenuData
import com.example.gestionnairesante.database.dao.periode.PeriodeData
import com.example.gestionnairesante.database.dao.plats.PlatData
import kotlinx.coroutines.launch

class VmRepas (private val repo: InnerPlatMenuRepo, private val repo2: InnerPeriodeMenuRepo) : ViewModel(){
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

    //
    //  Crud
    //
    fun composerMenu(plat: PlatData)  = viewModelScope.launch {
        val lastMenu = repo.getLastMenu()
        val inner = InnerPlatMenuData(0,plat.id_plat, lastMenu)
        repo.insertInnerPlatMenu(inner)
    }

    fun ajouterPlat(data: PlatData) = viewModelScope.launch{
        repo.insertPlat(data)
    }

    fun ajouterMenu(data: MenuData) = viewModelScope.launch{
        repo.insertMenu(data)
    }
    fun ajouterPeriode(data: PeriodeData) = viewModelScope.launch{
        repo2.insertPeriode(data)
    }

    fun ajouterInnerPlatMenu(data: InnerPlatMenuData) = viewModelScope.launch{
        repo.insertInnerPlatMenu(data)
    }

    fun ajouterInnerPeriodeMenu(data: InnerPeriodeMenuData) = viewModelScope.launch{
        repo.insertInnerPeriodeMenu(data)
    }

    //
    // cRud
    //
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
    fun getLastPeriodeInCurrent() : Int{
        return repo2.getLastPeriode()
    }

    fun getPlatInMenu() = liveData {
        repo.getPlatInMenu().collect() {
            emit(it)
        }
    }

    //
    // crUd
    //
    fun updateMenu(id: Int, tplat: Int, tGly: Int, tCal: Int) = viewModelScope.launch {
        repo.updateMenu(id, tplat, tGly, tCal)
    }

    fun updatePlat(id: Int, nom: String, glu: Int, cal: Int) = viewModelScope.launch {
        repo.updatePlat(id, nom, glu, cal)
    }


    //
    // cruD
    //
    fun deletePlatInCurrent(id: Int) = viewModelScope.launch {
        repo.deletePlatInCurrent(id)
    }

    fun deletePlat(id: Int) = viewModelScope.launch{
        repo.deletePlat(id)
    }

    fun deleteMenu(id: Int) = viewModelScope.launch {
        repo.deleteMenu(id)
    }

    fun deletePlatObj(data: PlatData) = viewModelScope.launch{
        repo.deletePlatObj(data)
    }

}