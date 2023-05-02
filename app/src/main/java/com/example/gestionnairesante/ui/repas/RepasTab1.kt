package com.example.gestionnairesante.ui.repas

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairesante.adapter.AdapterRecyclerMenu
import com.example.gestionnairesante.database.dao.menu.MenuData
import com.example.gestionnairesante.databinding.RepasTab1Binding
import com.example.gestionnairesante.ui.repas.vm.VmRepas

class RepasTab1 : Fragment() {
    private var binding: RepasTab1Binding? = null
    private lateinit var adapter: AdapterRecyclerMenu
    private val viewModelrepas: VmRepas by viewModels({ requireParentFragment() })

    val tabInner = ArrayList<MenuData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            binding?.recyclerMenu = this@RepasTab1
        }

        //creation de message pout l'utilisateur si qqc est arrivé
        viewModelrepas.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        viewModelrepas.getAllMenu().observe(viewLifecycleOwner) {
            tabInner.clear()
            tabInner.addAll(it)
            displayUser()
        }

        initRecycler()
        touchRecycler()
//        displayUser()
    }

    fun initRecycler() {
        // Configuration du layout
        binding?.recyclerMenud?.layoutManager = LinearLayoutManager(context)

        // Configuration de l'adapter
        adapter = AdapterRecyclerMenu { data: MenuData -> listItemClicked(data) }
        binding?.recyclerMenud?.adapter = adapter
    }

    fun listItemClicked(data: MenuData) {
        Toast.makeText(requireContext(), "item clique : ${data.nom_menu}", Toast.LENGTH_LONG)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun displayUser() {
        adapter.setList(tabInner)
        adapter.notifyDataSetChanged()
    }

    fun touchRecycler() {
        val itemTouchHelper by lazy {
            val simplecall = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val sp = viewHolder.adapterPosition
                    val obj = adapter.getDbObjet(sp)
                    viewModelrepas.deleteMenu(obj.id_menu)
                    tabInner.removeAt(sp)
                    adapter.notifyDataSetChanged()
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
}

