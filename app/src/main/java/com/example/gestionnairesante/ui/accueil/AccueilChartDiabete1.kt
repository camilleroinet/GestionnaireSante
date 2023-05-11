package com.example.gestionnairesante.ui.accueil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.gestionnairesante.databinding.AccueilChartAlimentationBinding
import com.example.gestionnairesante.databinding.AccueilChartDiabeteBinding
import com.example.gestionnairesante.ui.accueil.vm.VmAccueil
import com.example.gestionnairesante.utils.createLineChart
import com.example.gestionnairesante.utils.recupDataChart
import com.github.mikephil.charting.data.BarEntry

class AccueilChartDiabete1 : Fragment() {
    private var binding: AccueilChartDiabeteBinding? = null
    private val vmaccueil: VmAccueil by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // View binding
        val fragBinding = AccueilChartDiabeteBinding.inflate(inflater, container, false)
        binding = fragBinding

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            binding?.accueilChartLine1 = this@AccueilChartDiabete1
        }
        binding?.vmaccueil = vmaccueil



        vmaccueil.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
        return fragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recupDataLineChart()
    }

    fun recupDataLineChart() {
        val valuesBdd = ArrayList<Int>()
        val valuesRapide = ArrayList<Int>()
        val valuesLente = ArrayList<Int>()


        vmaccueil.getAllGlycemie().observe(viewLifecycleOwner, Observer {
            binding?.chart1AccueilDiabete?.invalidate()
            valuesBdd.clear()
            valuesBdd.addAll(it)

            if(valuesBdd.size ==0){
                binding?.chart1AccueilDiabete?.visibility = View.GONE
                binding?.llAvertissementDiabetechart?.visibility = View.VISIBLE
            }else{
                binding?.llAvertissementDiabetechart?.visibility = View.GONE
                binding?.chart1AccueilDiabete?.visibility = View.VISIBLE
                createLineChart(requireContext(), binding!!.chart1AccueilDiabete,
                    recupDataChart(valuesBdd),
                    recupDataChart(valuesRapide),
                    recupDataChart(valuesLente) )

                binding?.chart1AccueilDiabete?.invalidate()
            }

        })
    }


}