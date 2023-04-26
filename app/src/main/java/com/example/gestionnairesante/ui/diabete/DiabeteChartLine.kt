package com.example.gestionnairesante.ui.diabete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.gestionnairesante.databinding.FragChartLineBinding
import com.example.gestionnairesante.ui.diabete.vm.VMDiabete
import com.example.gestionnairesante.utils.createLineChart
import com.example.gestionnairesante.utils.recupDataChart

class DiabeteChartLine : Fragment() {

    private var binding: FragChartLineBinding? = null
    private val vmChart: VMDiabete by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // View binding
        val fragBinding = FragChartLineBinding.inflate(inflater, container, false)
        binding = fragBinding

        // Inflate the layout for this fragment
        return fragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            vmChart = vmChart
            binding?.fragChartLine = this@DiabeteChartLine
        }
        val tabData = ArrayList<Int>()

        //creation de message pout l'utilisateur si qqc est arrivé
        // todo a commenter
        vmChart.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        recupDataLineChart()

    }

    fun recupDataLineChart() {
        val valuesBdd = ArrayList<Int>()
        vmChart.getAllGlycemie().observe(viewLifecycleOwner, Observer {
            binding?.chart0?.invalidate()
            valuesBdd.clear()
            valuesBdd.addAll(it)

            if(valuesBdd.size <=2){
                binding?.chart0?.visibility = View.GONE
                binding?.llAvertissement?.visibility = View.VISIBLE
            }else{
                binding?.chart0?.visibility = View.VISIBLE
                binding?.llAvertissement?.visibility = View.GONE
            }

            createLineChart(requireContext(), binding!!.chart0, recupDataChart(valuesBdd))
            binding!!.chart0.invalidate()
        })
    }

}

