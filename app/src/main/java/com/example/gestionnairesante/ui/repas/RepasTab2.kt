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
import com.example.gestionnairesante.adapter.AdapterRecyclerPlat
import com.example.gestionnairesante.database.dao.plats.PlatData
import com.example.gestionnairesante.database.viewmodels.plat.VMPLat
import com.example.gestionnairesante.databinding.RepasTab2Binding

class RepasTab2 : Fragment(){
    private var binding: RepasTab2Binding? = null
    private lateinit var adapter: AdapterRecyclerPlat
    private val viewModel: VMPLat by viewModels ({ requireParentFragment() })
    private var ind = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // View binding
        val tab2Binding =  RepasTab2Binding.inflate(inflater, container, false)
        binding = tab2Binding

        // Inflate the layout for this fragment
        return tab2Binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
            binding?.recyclerPlats = this@RepasTab2
        }
        val tabPlat = ArrayList<PlatData>()

        //creation de message pout l'utilisateur si qqc est arrivé
        viewModel.message.observe(viewLifecycleOwner){ it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.getallPlat().observe(viewLifecycleOwner){ it ->
            tabPlat.clear()
            tabPlat.addAll(it)

            //recupDataBarChart()
            //binding.chart0.invalidate()
        }


        initRecycler()
        displayUser()
        touchRecycler()
    }

    fun initRecycler(){
        // Configuration du layout
        binding?.recyclerPlat?.layoutManager = LinearLayoutManager(context)

        // Configuration de l'adapter
        adapter = AdapterRecyclerPlat { daouser: PlatData -> listItemClicked(viewModel, daouser)}
        binding?.recyclerPlat?.adapter = adapter

    }

    fun listItemClicked(viewModel: VMPLat, daouser: PlatData){
        viewModel.initUpdateAndDelete(daouser)
        viewModel.clearallOrdelete()
    }

    fun displayUser(){
        viewModel.getallPlat().observe(viewLifecycleOwner, Observer {
            //Toast.makeText(requireContext(), "size ==>> ${it.size}", Toast.LENGTH_LONG).show()
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
                    val adapter = binding?.recyclerPlat?.adapter as AdapterRecyclerPlat
                    val from = viewHolder.adapterPosition
                    val to = target.adapterPosition
                    adapter.notifyItemMoved(from, to)
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val sp = viewHolder.adapterPosition
                    val obj = adapter.getDbObjet(sp)
                    //DialogFragHomeSuppr.newInstance("titre", "subtitre", ind).show(childFragmentManager, DialogFragHomeSuppr.TAG)
                    viewModel.deleteGlycemie(obj)
                }

                override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                    super.onSelectedChanged(viewHolder, actionState)
                    if (actionState == ItemTouchHelper.RIGHT){
                        viewHolder?.itemView?.alpha = 0.5F
                    }
                }

                override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                    super.clearView(recyclerView, viewHolder)
                    viewHolder.itemView.alpha = 1.0F
                }
            }
            ItemTouchHelper(simplecall)
        }
        itemTouchHelper.attachToRecyclerView(binding?.recyclerPlat)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}

