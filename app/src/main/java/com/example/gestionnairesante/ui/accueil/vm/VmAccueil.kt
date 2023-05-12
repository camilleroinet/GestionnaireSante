package com.example.gestionnairesante.ui.accueil.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.gestionnairesante.Event
import com.example.gestionnairesante.database.dao.innerStats.StatsRepo

class VmAccueil ( private val repo: StatsRepo): ViewModel() {

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    fun getAllGlycemie() = liveData {
        repo.allGlycemie.collect(){
            emit(it)
        }
    }

    fun getSpecGly(date: String) = liveData {
        repo.getSpecGlycemie(date).collect() {
            emit(it)
        }
    }
    fun getSpecRapide(date: String) = liveData {
        repo.getSpecRapide(date).collect() {
            emit(it)
        }
    }
    fun getSpecLente(date: String) = liveData {
        repo.getSpecLente(date).collect() {
            emit(it)
        }
    }

    fun getSpecCalories(date: String) = liveData {
        repo.getSpecCalories(date).collect() {
            emit(it)
        }
    }

    fun getSpecPoids(date: String) = liveData {
        repo.getSpecPoids(date).collect() {
            emit(it)
        }
    }

}