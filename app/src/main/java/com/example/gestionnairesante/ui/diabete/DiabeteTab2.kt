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
import com.example.gestionnairesante.adapter.AdapterRecyclerInsuline
import com.example.gestionnairesante.database.dao.insuline.InsulineData
import com.example.gestionnairesante.database.viewmodels.insuline.VMInsuline
import com.example.gestionnairesante.databinding.DiabeteTab2Binding

class DiabeteTab2 : Fragment() {
    private var binding: DiabeteTab2Binding? = null
    private lateinit var adapter: AdapterRecyclerInsuline
    private val viewModelinsuline: VMInsuline by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // View binding
        val tab2Binding = DiabeteTab2Binding.inflate(inflater, container, false)
        binding = tab2Binding

        // Inflate the layout for this fragment
        return tab2Binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModelinsuline = viewModelinsuline
            binding?.recyclerinsuline = this@DiabeteTab2
        }
        val tabInsulineRapide = ArrayList<Int>()
        val tabInsulineLente = ArrayList<Int>()

        //creation de message pout l'utilisateur si qqc est arrivé
        viewModelinsuline.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        viewModelinsuline.getallRapide().observe(viewLifecycleOwner) { it ->
            tabInsulineRapide.clear()
            tabInsulineRapide.addAll(it)
        }
        viewModelinsuline.getallLente().observe(viewLifecycleOwner) { it ->
            tabInsulineLente.clear()
            tabInsulineLente.addAll(it)
        }

        initRecycler()
        displayUser()
        touchRecycler()
    }

    fun initRecycler() {
        // Configuration du layout
        binding?.recyclerInsuline?.layoutManager = LinearLayoutManager(context)

        // Configuration de l'adapter
        adapter = AdapterRecyclerInsuline { daouser: InsulineData ->
            listItemClicked(
                viewModelinsuline,
                daouser
            )
        }
        binding?.recyclerInsuline?.adapter = adapter

    }

    fun listItemClicked(viewModel: VMInsuline, data: InsulineData) {
        val insulineRapide = data.insuline_rapide
        val insulineLente = data.insuline_lente
        val idInsuline = data.id_insuline

    }

    fun displayUser() {
        viewModelinsuline.getallInsuline().observe(viewLifecycleOwner, Observer {
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
                    val adapter = binding?.recyclerInsuline?.adapter as AdapterRecyclerInsuline
                    val from = viewHolder.adapterPosition
                    val to = target.adapterPosition
                    adapter.notifyItemMoved(from, to)
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val sp = viewHolder.adapterPosition
                    val obj = adapter.getDbObjet(sp)
                    viewModelinsuline.deleteInsuline(obj)
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
        itemTouchHelper.attachToRecyclerView(binding?.recyclerInsuline)
    }

}

