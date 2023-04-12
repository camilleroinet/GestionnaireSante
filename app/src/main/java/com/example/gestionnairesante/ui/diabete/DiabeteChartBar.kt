package com.example.gestionnairesante.ui.diabete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gestionnairesante.database.viewmodels.glycemie.VMGlycemie
import com.example.gestionnairesante.databinding.FragChartBarBinding
import com.example.gestionnairesante.utils.createBarChart
import com.github.mikephil.charting.data.BarEntry

class DiabeteChartBar : Fragment() {
    private var binding: FragChartBarBinding? = null
    private val viewModel: VMGlycemie by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // View binding
        val fragBinding = FragChartBarBinding.inflate(inflater, container, false)
        binding = fragBinding

        // Inflate the layout for this fragment
        return fragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
            binding?.fragChartBar = this@DiabeteChartBar
        }
        val tabData = ArrayList<Int>()

        //creation de message pout l'utilisateur si qqc est arrivé
        // todo a commenter
        viewModel.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.getAllValeurGlycemie().observe(viewLifecycleOwner) { it ->
            tabData.clear()
            tabData.addAll(it)
            recupDataBarChart()
            //recupDataBarChart()
            //binding.chart0.invalidate()
        }
        recupDataBarChart()
    }

    fun recupDataBarChart(): ArrayList<BarEntry> {
        val valeur = ArrayList<BarEntry>()
        val tabValeur = ArrayList<Int>()
        val barChart = binding!!.chart0
        val stringValue = ArrayList<String>()

        viewModel.getAllValeurGlycemie().observe(viewLifecycleOwner) {
            tabValeur.clear()
            tabValeur.addAll(it)

            val r = tabValeur.size - 1

            for (i in 0..r) {
                stringValue.add("")
                valeur.add(BarEntry(i.toFloat(), tabValeur[i].toFloat()))
            }
            createBarChart(barChart, valeur, stringValue, "Glycemies")
        }
        return valeur
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}

