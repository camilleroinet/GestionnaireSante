package com.example.gestionnairesante.ui.diabete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairesante.database.DB_sante
import com.example.gestionnairesante.database.dao.glycemie.GlycemieRepo
import com.example.gestionnairesante.database.viewmodels.glycemie.VMGlycemie
import com.example.gestionnairesante.database.viewmodels.glycemie.VMGlycemieFactory
import com.example.gestionnairesante.databinding.FragChartPieBinding
import com.example.gestionnairesante.utils.createColorTab
import com.example.gestionnairesante.utils.creationPieChart

class DiabeteChartPie : Fragment() {
    /*
        private var binding: FragChartPieBinding?= null
        private val viewModel: VMGlycemie by viewModels ({ requireParentFragment() })
    */
    private lateinit var binding: FragChartPieBinding
    private lateinit var viewModel: VMGlycemie

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // View binding
        val fragBinding = FragChartPieBinding.inflate(inflater, container, false)
        binding = fragBinding

        // Data binding
        val dao = DB_sante.getInstance(requireContext()).tabGlycemie
        val repository = GlycemieRepo(dao)
        val factory = VMGlycemieFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(VMGlycemie::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Inflate the layout for this fragment
        return fragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabData = ArrayList<Int>()

        //creation de message pout l'utilisateur si qqc est arrivé
        viewModel.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.getAllValeurGlycemie().observe(viewLifecycleOwner) { it ->
            tabData.clear()
            tabData.addAll(it)
        }

        createDataPieChart()
    }

    fun createDataPieChart() {
        val arrayData = ArrayList<Float>()
        val tabValeur = ArrayList<Int>()

        viewModel.getAllValeurGlycemie().observe(viewLifecycleOwner) { it ->
            var hypo = 0
            var cible = 0
            var fort = 0
            var hyper = 0

            tabValeur.clear()
            tabValeur.addAll(it)

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

            creationPieChart(binding.chart0, arrayData, couleurs)

        }

    }

}
