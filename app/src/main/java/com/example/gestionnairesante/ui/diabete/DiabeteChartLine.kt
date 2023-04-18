package com.example.gestionnairesante.ui.diabete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.gestionnairesante.database.viewmodels.glycemie.VMGlycemie
import com.example.gestionnairesante.databinding.FragChartLineBinding
import com.example.gestionnairesante.ui.diabete.vm.VMDiabete
import com.example.gestionnairesante.utils.createLineChart
import com.example.gestionnairesante.utils.recupDataChart

class DiabeteChartLine : Fragment() {
    private var binding: FragChartLineBinding? = null
    private val viewModel: VMDiabete by viewModels({ requireParentFragment() })

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
            viewModel = viewModel
            binding?.fragChartLine = this@DiabeteChartLine
        }
        val tabData = ArrayList<Int>()

        //creation de message pout l'utilisateur si qqc est arrivé
        // todo a commenter
        viewModel.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.getAllGlycemie().observe(viewLifecycleOwner) { it ->
            binding?.chart0?.invalidate()
            tabData.clear()
            tabData.addAll(it)
            recupDataLineChart()
        }
    }

    fun recupDataLineChart() {
        val valuesBdd = ArrayList<Int>()

        viewModel.getAllGlycemie().observe(viewLifecycleOwner, Observer {
            binding?.chart0?.invalidate()
            valuesBdd.clear()
            valuesBdd.addAll(it)
            createLineChart(requireContext(), binding!!.chart0, recupDataChart(valuesBdd))
        })
    }

}

