package com.example.gestionnairesante.ui.poids

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gestionnairesante.database.viewmodels.VMGlycemie
import com.example.gestionnairesante.database.viewmodels.VMPoids
import com.example.gestionnairesante.database.viewmodels.VMPoidsFactory
import com.example.gestionnairesante.databinding.FragChartBarBinding
import com.example.gestionnairesante.databinding.PoidsChartBarBinding
import com.example.gestionnairesante.utils.createBarChart
import com.github.mikephil.charting.data.BarEntry

class PoidsChartBar : Fragment() {
    private var binding: PoidsChartBarBinding?= null
    private val viewModel: VMPoids by viewModels ({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // View binding
        val fragBinding =  PoidsChartBarBinding.inflate(inflater, container, false)
        binding = fragBinding

        // Inflate the layout for this fragment
        return fragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
            binding?.fragChartBar = this@PoidsChartBar
        }
        val tabPoids = ArrayList<Float>()

        //creation de message pout l'utilisateur si qqc est arrivé
        viewModel.message.observe(viewLifecycleOwner){ it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.getValeurPoids().observe(viewLifecycleOwner){ it ->
            tabPoids.clear()
            tabPoids.addAll(it)
            recupDataBarChart()            //recupDataBarChart()
            //binding.chart0.invalidate()
        }
    }

    fun recupDataBarChart(): ArrayList<BarEntry>{
        val valu = ArrayList<BarEntry>()
        val tabValeur = ArrayList<Float>()
        val barChart = binding!!.chart0
        val stringValue = ArrayList<String>()

        viewModel.getValeurPoids().observe(viewLifecycleOwner){
            tabValeur.clear()
            tabValeur.addAll(it)

            val r = tabValeur.size - 1

            for (i in 0..r){
                stringValue.add("")
                valu.add(BarEntry(i.toFloat(), tabValeur[i].toFloat()))
            }
            createBarChart(barChart,valu,stringValue ,"Poids"  )
        }

        return valu
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}

