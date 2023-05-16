package com.example.gestionnairesante.ui.repas.service

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairesante.ui.repas.adapter.AdapterRecyclerPlat
import com.example.gestionnairesante.database.dao.plats.PlatData
import com.example.gestionnairesante.databinding.RepasTab2Binding
import com.example.gestionnairesante.ui.repas.vm.VmRepas

class RepasTab2 : Fragment() {
    private var binding: RepasTab2Binding? = null
    private lateinit var adapter: AdapterRecyclerPlat
    private val viewmodelrepas: VmRepas by activityViewModels()
    private var ind = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // View binding
        val tab2Binding = RepasTab2Binding.inflate(inflater, container, false)
        binding = tab2Binding
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            binding?.recyclerPlats = this@RepasTab2
        }
        binding?.viewmodelrepas = viewmodelrepas

        // Inflate the layout for this fragment
        return tab2Binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodelrepas.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        initRecycler()
        touchRecycler()
        displayUser()
    }

    fun initRecycler() {
        // Configuration du layout
        binding?.recyclerPlat?.layoutManager = LinearLayoutManager(context)

        // Configuration de l'adapter
        adapter = AdapterRecyclerPlat { daouser: PlatData -> listItemClicked(daouser) }
        binding?.recyclerPlat?.adapter = adapter

    }

    fun listItemClicked(data: PlatData) {
        //Toast.makeText(requireContext(), "item clique : ${data.nom_menu}", Toast.LENGTH_LONG)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun displayUser() {
        viewmodelrepas.getAllPlats().observe(viewLifecycleOwner) {
            //Toast.makeText(requireContext(), "size ==>> ${it.size}", Toast.LENGTH_LONG).show()
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }

    fun touchRecycler() {
        val itemTouchHelper by lazy {
            val simplecall = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val sp = viewHolder.adapterPosition
                    val obj = adapter.getDbObjet(sp)
                    when(direction) {
                        ItemTouchHelper.RIGHT -> {
                            viewmodelrepas.deletePlat(obj.id_plat)
                            adapter.remove(sp)
                        }
                        ItemTouchHelper.LEFT -> {
                            val idplat = obj.id_plat
                            val nom = obj.nom_plat
                            val cal = obj.calorie_plat
                            val glu = obj.glucide_plat

                            RepasDialogPlat.newInstance(
                                "titre", "subtitre", ind,
                                idplat, nom, glu, cal
                            )
                                .show(childFragmentManager, RepasDialogPlat.TAG)
                        }
                    }
                    //DialogFragHomeSuppr.newInstance("titre", "subtitre", ind).show(childFragmentManager, DialogFragHomeSuppr.TAG)
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
        itemTouchHelper.attachToRecyclerView(binding?.recyclerPlat)
    }

}

