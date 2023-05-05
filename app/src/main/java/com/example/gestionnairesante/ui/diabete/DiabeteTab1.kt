package com.example.gestionnairesante.ui.diabete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairesante.adapter.AdapterRecyclerDiabete
import com.example.gestionnairesante.database.dao.innerDiabete.DataInner
import com.example.gestionnairesante.databinding.DiabeteTab1Binding
import com.example.gestionnairesante.ui.diabete.vm.VMDiabete

class DiabeteTab1 : Fragment() {
    private var binding: DiabeteTab1Binding? = null
    private lateinit var adapteur: AdapterRecyclerDiabete
    private val vmdiabete: VMDiabete by activityViewModels()

    val tabInner = ArrayList<DataInner>()

    private var ind = 0

    private val statusMessage = MutableLiveData<com.example.gestionnairesante.Event<String>>()
    val message: LiveData<com.example.gestionnairesante.Event<String>>
        get() = statusMessage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // View binding
        val tab1Binding = DiabeteTab1Binding.inflate(inflater, container, false)
        binding = tab1Binding

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            binding?.recyclerDiabete = this@DiabeteTab1
        }

        binding?.vmdiabete = vmdiabete

        initRecycler()
        touchRecycler()
        displayUser()

        // Inflate the layout for this fragment
        return tab1Binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //creation de message pout l'utilisateur si qqc est arrivé
        vmdiabete.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

    }

    fun initRecycler() {
        // Configuration du layout
        binding?.rvDiabete?.layoutManager = LinearLayoutManager(context)

        // Configuration de l'adapter
        adapteur = AdapterRecyclerDiabete { daouser: DataInner -> listItemClicked(daouser) }
        binding?.rvDiabete?.adapter = adapteur

        //adapteur.setList(tabInner)
    }

    fun listItemClicked(data: DataInner) {
        val idgly = data.idgly
        val idper = data.idper
        val idins = data.idins
        val glycemie = data.glycemie
        val rapide = data.rapide
        val lente = data.lente
        val date = data.date
        val heure = data.heure
        val periode = data.periode

        DiabeteDialogGlycemie.newInstance(
            "titre", "sous titre",
            ind,
            idgly, idins, idper,
            glycemie, rapide, lente,
            date, heure, periode
        ).show(childFragmentManager, DiabeteDialogGlycemie.TAG)


    }

    fun touchRecycler() {
        val itemTouchHelper by lazy {
            val simplecall = object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val sp = viewHolder.adapterPosition
                    val obj = adapteur.getDbObjet(sp)

                    when (direction) {
                        ItemTouchHelper.RIGHT -> {
                            vmdiabete.deleteDiabete(obj.idper, obj.idgly, obj.idins)
                            adapteur.remove(sp)
                        }

                        ItemTouchHelper.LEFT -> {
                            val idgly = obj.idgly
                            val idper = obj.idper
                            val idins = obj.idins
                            val glycemie = obj.glycemie
                            val rapide = obj.rapide
                            val lente = obj.lente
                            val date = obj.date
                            val heure = obj.heure
                            val periode = obj.periode

                            DiabeteDialogGlycemie.newInstance(
                                "titre", "sous titre",
                                ind,
                                idgly, idins, idper,
                                glycemie, rapide, lente,
                                date, heure, periode
                            ).show(childFragmentManager, DiabeteDialogGlycemie.TAG)

                            adapteur.update(sp)
                            adapteur.setList(tabInner)
                            adapteur.notifyDataSetChanged()

                        }
                    }


                    //
                    //binding?.rvDiabete?.invalidate()
                }
            }
            ItemTouchHelper(simplecall)
        }
        itemTouchHelper.attachToRecyclerView(binding?.rvDiabete)

    }

    fun displayUser() {
        vmdiabete.getGlycemiePeriode().observe(viewLifecycleOwner, Observer {
            tabInner.clear()
            tabInner.addAll(it)
            adapteur.setList(tabInner)
            adapteur.notifyDataSetChanged()
            binding?.rvDiabete?.invalidate()

        })
    }

}