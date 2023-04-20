package com.example.gestionnairesante.ui.poids.vm

import androidx.lifecycle.*
import com.example.gestionnairesante.Event
import com.example.gestionnairesante.database.dao.innerPoids.InnerPoidsData
import com.example.gestionnairesante.database.dao.innerPoids.InnerPoidsRepo
import com.example.gestionnairesante.database.dao.periode.PeriodeData
import com.example.gestionnairesante.database.dao.poids.PoidsData
import kotlinx.coroutines.launch

class VmPoids(private val repo: InnerPoidsRepo) : ViewModel() {
    val valeurPoids = MutableLiveData<String>()
    val valeurDate = MutableLiveData<String>()
    val valeurHeure = MutableLiveData<String>()
    val valeurPeriode = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        valeurPoids.value = ""
        valeurPeriode.value = "1"
        valeurDate.value = "01/01/01"
        valeurHeure.value = "12:00"
    }

    fun insertPoids(poids: Float, date: String, heure: String, periode: String) =
        viewModelScope.launch {
            //
            // Insert du poids
            //
            val newPoids = PoidsData(0, poids)
            repo.insertPoids(newPoids)
            val lastPoids = repo.getLastPoids()

            //
            // Insert de la periode
            //
            val newPeriode = PeriodeData(0,periode, date, heure )
            repo.insertPeriode(newPeriode)
            val lastPeriode = repo.getLastPeriode()
            //
            // Insert de l'inner join
            //
            val inner = InnerPoidsData(lastPoids, lastPeriode)
            repo.insertPoidsInner(inner)
            statusMessage.value = Event("L'enregistrement s'est bien déroulé.")

        }

    fun getPoidsPeriode() = liveData {
        repo.innerPoidsPeriode.collect() {
            emit(it)
        }
    }

    fun updatePoids(
        idpoi: Int,
        idper: Int,
        poids: Float,
        date: String,
        heure: String,
        periode: String
    ) = viewModelScope.launch {
        repo.updatePoids(idpoi, poids)
        repo.updatePeriode(idper, date, heure, periode)
        statusMessage.value = Event("Mise à jour réussie.")
    }

    fun getAllPoids() = liveData {
        repo.allPoids.collect {
            emit(it)
        }
    }

    fun getAllValeurPoids() = liveData {
        repo.allValeurPoids.collect {
            emit(it)
        }
    }

    fun deletePoids(id: Int) = viewModelScope.launch {
        repo.deletePoids(id)
        statusMessage.value = Event("Suppression réussie")
    }

}