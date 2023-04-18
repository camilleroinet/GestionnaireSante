package com.example.gestionnairesante.ui.diabete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
    private lateinit var adapter: AdapterRecyclerDiabete
    private val viewModel: VMDiabete by viewModels({ requireParentFragment() })
    private var ind = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // View binding
        val tab1Binding = DiabeteTab1Binding.inflate(inflater, container, false)
        binding = tab1Binding

        // Inflate the layout for this fragment
        return tab1Binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
            binding?.recyclerDiabete = this@DiabeteTab1
        }

        val tabInner = ArrayList<DataInner>()

        //creation de message pout l'utilisateur si qqc est arrivé
        viewModel.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.getGlycemiePeriode().observe(viewLifecycleOwner) { it ->
            tabInner.clear()
            tabInner.addAll(it)
            displayUser()
        }

        initRecycler()
        displayUser()
        touchRecycler()
    }

    fun initRecycler() {
        // Configuration du layout
        binding?.rvDiabete?.layoutManager = LinearLayoutManager(context)

        // Configuration de l'adapter
        adapter =
            AdapterRecyclerDiabete { daouser: DataInner -> listItemClicked(viewModel, daouser) }
        binding?.rvDiabete?.adapter = adapter

    }

    fun listItemClicked(viewModel: VMDiabete, data: DataInner) {
/*        val id = viewModel.getGlycemieToUpdate(data).id_glycemie
        val glycemie = viewModel.getGlycemieToUpdate(data).valeur_glycemie
        if (glycemie != null) {
            DiabeteDialogGlycemie.newInstance(
                "titre",
                "subtitre",
                ind,
                id, glycemie
            ).show(childFragmentManager, PoidsDialog.TAG)
        }*/
    }

    fun displayUser() {
        viewModel.getGlycemiePeriode().observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    fun touchRecycler() {
        val itemTouchHelper by lazy {
            val simplecall = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val adapter = binding?.rvDiabete?.adapter as AdapterRecyclerDiabete
                    val from = viewHolder.adapterPosition
                    val to = target.adapterPosition
                    adapter.notifyItemMoved(from, to)
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val sp = viewHolder.adapterPosition
                    val obj = adapter.getDbObjet(sp)
                    viewModel.deleteDiabete(obj.idper, obj.idgly, obj.idins)
                }

                override fun onSelectedChanged(
                    viewHolder: RecyclerView.ViewHolder?,
                    actionState: Int
                ) {
                    super.onSelectedChanged(viewHolder, actionState)
                    if (actionState == ItemTouchHelper.RIGHT) {
                        viewHolder?.itemView?.alpha = 0.5F
                    }
                }

                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) {
                    super.clearView(recyclerView, viewHolder)
                    viewHolder.itemView.alpha = 1.0F
                }
            }
            ItemTouchHelper(simplecall)
        }
        itemTouchHelper.attachToRecyclerView(binding?.rvDiabete)
    }
}

