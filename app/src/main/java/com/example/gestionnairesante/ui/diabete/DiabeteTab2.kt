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
import com.example.gestionnairesante.ui.diabete.vm.VMDiabete
import com.example.gestionnairesante.ui.poids.PoidsDialog

class DiabeteTab2 : Fragment() {

    private var binding: DiabeteTab2Binding? = null
    private val viewModelinsuline: VMDiabete by viewModels({ requireParentFragment() })

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
            binding?.recyclerinsuline = this@DiabeteTab2
        }

        //creation de message pout l'utilisateur si qqc est arrivé
        viewModelinsuline.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        var newPurge = 2
        binding?.btnPurgeMoins?.setOnClickListener() {

            if(newPurge > 0){
                newPurge -= 1
            }else{
                newPurge = 0
            }

            binding?.nbPurge?.text = newPurge.toString()

        }

        binding?.btnPurgePlus?.setOnClickListener() {

            if(newPurge < 10){
                newPurge += 1
            }else{
                newPurge = 10
            }

            binding?.nbPurge?.text = newPurge.toString()

        }

    }

}

