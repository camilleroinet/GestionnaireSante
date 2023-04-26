package com.example.gestionnairesante.ui.sport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionnairesante.adapter.AdapterRecyclerDiabete
import com.example.gestionnairesante.database.DB_sante
import com.example.gestionnairesante.database.dao.innerDiabete.DataInner
import com.example.gestionnairesante.database.dao.innerDiabete.InnerDiabeteData
import com.example.gestionnairesante.database.dao.innerDiabete.InnerDiabeteRepo
import com.example.gestionnairesante.databinding.FragmentSportBinding

class SportFragment  : Fragment() {
/*    private lateinit var binding: FragmentSportBinding
    private lateinit var vmsport : SportViewModel
    private lateinit var adapterPlat: AdapterRecyclerDiabete

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // viewbinding
        val sportBinding = FragmentSportBinding.inflate(inflater, container, false)
        binding = sportBinding
        return sportBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
    /*            lifecycleOwner = viewLifecycleOwner
                binding!!.fragSport = this@SportFragment*/
        }

        // databinding
        val daoPlat = DB_sante.getInstance(requireContext()).tabGlycemie
        val daoPeriode = DB_sante.getInstance(requireContext()).tabPeriode
        val daoInsuline = DB_sante.getInstance(requireContext()).tabInsuline

        val daoInner = DB_sante.getInstance(requireContext()).tabRelationnelDiabete

        val repoDiabete = InnerDiabeteRepo(daoPlat, daoPeriode, daoInsuline, daoInner)
        val factoryDiabete = SportViewModelFactory(repoDiabete)
        vmsport = ViewModelProvider(this, factoryDiabete).get(SportViewModel::class.java)
        binding.vmsport = vmsport


        vmsport.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        val tabPlat = ArrayList<DataInner>()
        vmsport.getGlycemiePeriode().observe(viewLifecycleOwner) { it ->
            tabPlat.clear()
            tabPlat.addAll(it)
        }

        binding.btnSave.setOnClickListener {
            vmsport.insertDiabete("maPeriode", "aujourd'hui", "19:99", 125, 12,0)
            vmsport.insertDiabete("maPeriode2", "aujourd'hui", "19:99", 333, 12,0)
        }

        initRecycler()
        touchRecycler()
        displayUser()
    }

    fun initRecycler() {
        // Configuration du layout
        binding.recyclerPlat.layoutManager = LinearLayoutManager(context)

        // Configuration de l'adapter
        adapterPlat = AdapterRecyclerDiabete { daouser: DataInner -> listItemClicked(vmsport, daouser) }
        binding.recyclerPlat.adapter = adapterPlat

    }

    private fun listItemClicked(viewModel: SportViewModel, daouser: DataInner) {

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

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val sp = viewHolder.adapterPosition
                    val obj = adapterPlat.getDbObjet(sp)
                    vmsport.deleteDiabete(obj.idgly, obj.idins, obj.idper)

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
        itemTouchHelper.attachToRecyclerView(binding.recyclerPlat)
    }
    fun displayUser() {
        vmsport.getGlycemiePeriode().observe(viewLifecycleOwner, Observer {
            adapterPlat.setList(it)
            adapterPlat.notifyDataSetChanged()
        })
    }
*/
}