package com.example.gestionnairesante.ui.diabete.service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.gestionnairesante.databinding.FragChartPieBinding
import com.example.gestionnairesante.ui.diabete.vm.VMDiabete
import com.example.gestionnairesante.utils.createColorTab
import com.example.gestionnairesante.utils.creationPieChart

class DiabeteChartPie : Fragment() {

    private var binding: FragChartPieBinding? = null
    private val vmdiabete: VMDiabete by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // View binding
        val fragBinding = FragChartPieBinding.inflate(inflater, container, false)
        binding = fragBinding

        // Inflate the layout for this fragment
        return fragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
            binding?.fragChartPie = this@DiabeteChartPie
        }

        //creation de message pout l'utilisateur si qqc est arrivé
        vmdiabete.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        createDataPieChart()

    }

    fun createDataPieChart() {
        val arrayData = ArrayList<Float>()
        val tabValeur = ArrayList<Int>()

        vmdiabete.getAllGlycemie().observe(viewLifecycleOwner) { it ->
            var hypo = 0
            var cible = 0
            var fort = 0
            var hyper = 0

            tabValeur.clear()
            tabValeur.addAll(it)

            if(tabValeur.size >2){
                binding?.chart0?.visibility = View.VISIBLE
                binding?.llAvertissement?.visibility = View.GONE

            }else{
                binding?.chart0?.visibility = View.GONE
                binding?.llAvertissement?.visibility = View.VISIBLE
            }

            for (i in 0 until it.size) {
                when (tabValeur.get(i)) {
                    in 0..79 -> hypo += 1
                    in 80..179 -> cible += 1
                    in 180..249 -> fort += 1
                    else -> hyper += 1
                }
            }

            arrayData.add(hypo.toFloat())
            arrayData.add(cible.toFloat())
            arrayData.add(fort.toFloat())
            arrayData.add(hyper.toFloat())

            val couleurs = createColorTab(requireContext(), arrayData.size)

            creationPieChart(binding!!.chart0, arrayData, couleurs)
            binding!!.chart0.invalidate()
        }

    }

}
