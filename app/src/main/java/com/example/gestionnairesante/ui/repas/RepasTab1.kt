package com.example.gestionnairesante.ui.repas

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
import com.example.gestionnairesante.adapter.AdapterRecyclerMenu
import com.example.gestionnairesante.adapter.AdapterRecyclerPlat
import com.example.gestionnairesante.database.dao.menu.MenuData
import com.example.gestionnairesante.database.dao.plats.PlatData
import com.example.gestionnairesante.database.viewmodels.menu.VMMenu
import com.example.gestionnairesante.databinding.RepasTab1Binding

class RepasTab1 : Fragment() {
 /*   private var binding: RepasTab1Binding? = null
    private lateinit var adapter: AdapterRecyclerMenu
   // private val viewModel: VMMenu by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // View binding
        val tab1Binding = RepasTab1Binding.inflate(inflater, container, false)
        binding = tab1Binding

        // Inflate the layout for this fragment
        return tab1Binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
            binding?.recyclerMenu = this@RepasTab1
        }


        val tabMenu = ArrayList<MenuData>()

        //creation de message pout l'utilisateur si qqc est arrivé
*//*        viewModel.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }*//*

*//*        viewModel.getAllMenu().observe(viewLifecycleOwner) { it ->
            tabMenu.clear()
            tabMenu.addAll(it)
        }*//*

        initRecycler()
        displayUser()
        touchRecycler()
    }

    fun initRecycler() {
        // Configuration du layout
        //binding?.recyclerMenud?.layoutManager = LinearLayoutManager(context)

        // Configuration de l'adapter
       // adapter = AdapterRecyclerMenu { daouser: MenuData -> listItemClicked(viewModel, daouser) }
        //binding?.recyclerMenud?.adapter = adapter

    }

    fun listItemClicked(viewModel: VMMenu, daouser: MenuData) {
        viewModel.initUpdateAndDelete(daouser)
        viewModel.clearallOrdelete()
    }

    fun displayUser() {
*//*        viewModel.getAllMenu().observe(viewLifecycleOwner, Observer {
            //Toast.makeText(requireContext(), "size ==>> ${it.size}", Toast.LENGTH_LONG).show()
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })*//*
    }

    fun touchRecycler() {
        val itemTouchHelper by lazy {
            val simplecall = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val adapter = binding?.recyclerMenu?.adapter as AdapterRecyclerMenu
                    val from = viewHolder.adapterPosition
                    val to = target.adapterPosition
                    adapter.notifyItemMoved(from, to)
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val sp = viewHolder.adapterPosition
                    val obj = adapter.getDbObjet(sp)
                    //DialogFragHomeSuppr.newInstance("titre", "subtitre", ind).show(childFragmentManager, DialogFragHomeSuppr.TAG)
                   // viewModel.deleteGlycemie(obj)
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
        itemTouchHelper.attachToRecyclerView(binding?.recyclerMenud)
    }
*/
}

