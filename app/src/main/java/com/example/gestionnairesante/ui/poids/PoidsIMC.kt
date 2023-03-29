package com.example.gestionnairesante.ui.poids

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairesante.database.DB_sante
import com.example.gestionnairesante.database.dao.poids.PoidsRepo
import com.example.gestionnairesante.database.viewmodels.VMPoids
import com.example.gestionnairesante.database.viewmodels.VMPoidsFactory
import com.example.gestionnairesante.databinding.PoidsChartImcBinding

class PoidsIMC : Fragment() {
    private var binding: PoidsChartImcBinding?= null
    private lateinit var viewModelbis: VMPoids

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // View binding
        val fragBinding =  PoidsChartImcBinding.inflate(inflater, container, false)
        binding = fragBinding

        val dao = DB_sante.getInstance(requireContext()).tabPoids
        val repository = PoidsRepo(dao)
        val factory = VMPoidsFactory(repository)
        viewModelbis = ViewModelProvider(this, factory).get(VMPoids::class.java)


        // Inflate the layout for this fragment
        return fragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModelbis = viewModelbis
            binding?.fragImc = this@PoidsIMC
        }
        // databinding

        val tabPoids = ArrayList<Float>()

        //creation de message pout l'utilisateur si qqc est arrivé
        viewModelbis.message.observe(viewLifecycleOwner){ it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }



        /*viewModel.l*/
        viewModelbis.getAllPoids().observe(viewLifecycleOwner) { it ->
            //val az = viewModel.recupLastPoids().toString()

           // val a = ArrayList<PoidsData>()
            //a.addAll(it)
            //val e = viewModel.recupLast()
            //Toast.makeText(requireContext(), "ddddd=====> $az", Toast.LENGTH_LONG).show()
        }
        //viewModel.inputLastPoids.observe(viewLifecycleOwner){

        //}
        //binding?.imc?.text = viewModel.recupLastPoids()

       //viewModel.las.observe(viewLifecycleOwner) { it ->


    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

}

