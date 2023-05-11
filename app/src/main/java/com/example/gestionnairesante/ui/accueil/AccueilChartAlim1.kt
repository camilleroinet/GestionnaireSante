package com.example.gestionnairesante.ui.accueil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gestionnairesante.databinding.AccueilChartAlimentationBinding
import com.example.gestionnairesante.ui.accueil.vm.VmAccueil
import com.github.mikephil.charting.data.BarEntry

class AccueilChartAlim1 : Fragment() {

    private lateinit var binding: AccueilChartAlimentationBinding
    private lateinit var vmaccueil: VmAccueil

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // View binding
        val fragBinding = AccueilChartAlimentationBinding.inflate(inflater, container, false)
        binding = fragBinding

        // Inflate the layout for this fragment
        return fragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            vmaccueil = vmaccueil
            binding.chartAlim1 = this@AccueilChartAlim1
        }
        val tabData = ArrayList<Int>()


        recupDataBarChart()
    }

    fun recupDataBarChart(): ArrayList<BarEntry> {
        val valeur = ArrayList<BarEntry>()
        val tabValeur = ArrayList<Int>()
        val barChart = binding!!.chart1Alim
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