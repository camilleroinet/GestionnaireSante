package com.example.gestionnairesante.ui.accueil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gestionnairesante.databinding.AccueilChartAlimentation2Binding
import com.example.gestionnairesante.databinding.AccueilChartAlimentationBinding
import com.example.gestionnairesante.ui.accueil.vm.VmAccueil
import com.github.mikephil.charting.data.BarEntry

class AccueilChartAlim2 : Fragment(){

    private lateinit var binding: AccueilChartAlimentation2Binding
    private lateinit var vmaccueil: VmAccueil

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // View binding
        val fragBinding = AccueilChartAlimentation2Binding.inflate(inflater, container, false)
        binding = fragBinding

        // Inflate the layout for this fragment
        return fragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            vmaccueil = vmaccueil
            binding.alimChart2 = this@AccueilChartAlim2
        }
        val tabData = ArrayList<Int>()


        recupDataBarChart()
    }

    fun recupDataBarChart(): ArrayList<BarEntry> {
        val valeur = ArrayList<BarEntry>()
        val tabValeur = ArrayList<Int>()
        val barChart = binding!!.chart2Alim
        val stringValue = ArrayList<String>()

        /*        vmaccueil.getAllGlycemie().observe(viewLifecycleOwner) {
                    tabValeur.clear()
                    tabValeur.addAll(it)

                    val r = tabValeur.size - 1

                    for (i in 0..r) {
                        stringValue.add("")
                        valeur.add(BarEntry(i.toFloat(), tabValeur[i].toFloat()))
                    }
                    createBarChart(barChart, valeur, stringValue, "Glycemies")
                }*/
        return valeur
    }



}