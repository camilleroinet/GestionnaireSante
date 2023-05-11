package com.example.gestionnairesante.ui.accueil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.DB_sante
import com.example.gestionnairesante.database.dao.innerStats.StatsRepo
import com.example.gestionnairesante.databinding.AccueilBinding
import com.example.gestionnairesante.ui.accueil.vm.VmAccueil
import com.example.gestionnairesante.ui.accueil.vm.VmAccueilFactory
import com.example.gestionnairesante.utils.createBarChart
import com.example.gestionnairesante.utils.createColorTab
import com.example.gestionnairesante.utils.createLineChart
import com.example.gestionnairesante.utils.creationPieChart
import com.example.gestionnairesante.utils.recupDataChart
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.tabs.TabLayout

class AccueilFragment : Fragment() {

    private lateinit var binding: AccueilBinding
    private lateinit var vmaccueil: VmAccueil

    private lateinit var tablayoutTabs: TabLayout
    private var arrayTab = arrayListOf(R.string.txt_accueilTab1, R.string.txt_accueilTab2, R.string.txt_accueilTab3)

    private var posTab = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // viewbinding
        val accueilFrag = AccueilBinding.inflate(inflater, container, false)
        binding = accueilFrag

        return accueilFrag.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            binding.fragAccueil = this@AccueilFragment
        }

        // databinding
        val dao = DB_sante.getInstance(requireContext()).tabRelationnelStats
        val repo = StatsRepo(dao)
        val factory = VmAccueilFactory(repo)
        vmaccueil = ViewModelProvider(requireActivity(), factory).get(VmAccueil::class.java)
        binding.vmaccueil = vmaccueil

        tablayoutTabs = binding.tabLayout

        binding.btnGraph1.setOnClickListener {
            binding.chart1AccueilDiabete.visibility = View.VISIBLE
            binding.chart2AccueilDiabete.visibility = View.VISIBLE
            binding.chart1AccueilAlimentation.visibility = View.GONE
        }

        binding.btnGraph2.setOnClickListener {
            binding.chart1AccueilDiabete.visibility = View.GONE
            binding.chart2AccueilDiabete.visibility = View.GONE
            binding.chart1AccueilAlimentation.visibility = View.VISIBLE
        }

        configTablelayout(arrayTab)
        loadCharts()
    }

    fun configTablelayout(array: ArrayList<Int>) {
        tablayoutTabs.apply {
            for (i in array.indices) {
                tablayoutTabs.addTab(tablayoutTabs.newTab().setText(array[i]))
            }

            tablayoutTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.position?.let {
                        when (tab.position) {
                            0 -> {
                                posTab = 0
                                binding.datepickeraccueil.visibility = View.VISIBLE
                                binding.chart1AccueilDiabete.visibility = View.VISIBLE
                                binding.chart2AccueilDiabete.visibility = View.VISIBLE
                                binding.chart1AccueilAlimentation.visibility = View.GONE
                                binding.chart2AccueilAlimentation.visibility = View.GONE
                            }

                            1 -> {
                                posTab = 1
                                binding.datepickeraccueil.visibility = View.VISIBLE
                                binding.chart1AccueilDiabete.visibility = View.GONE
                                binding.chart2AccueilDiabete.visibility = View.GONE
                                binding.chart1AccueilAlimentation.visibility = View.VISIBLE
                                binding.chart2AccueilAlimentation.visibility = View.VISIBLE
                            }

                            else -> {

                            }
                        }

                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    fun recupDate(year: Int, month: Int, day: Int): String {

        val jourString: String = day.toString()
        val monthString: String = month.toString()

        // TODO a copier dans les insert dans toutes l'appli
        /*        if (month<10){
                    monthString = "0$month"
                }
                if (day<10){
                    jourString = "0$day"
                }*/

        // Toast.makeText(context, "la date séléctionnée est $jourString-$monthString-$year", Toast.LENGTH_LONG).show()

        val dateenstring = "$jourString-$monthString-$year"

        return dateenstring
    }

    fun loadCharts() {
        val valuesBdd = ArrayList<Int>()
        val valuesPoids = ArrayList<Float>()
        val arrayData = ArrayList<Float>()

        //
        // Date
        //
        val jourDuJour = binding.datepickeraccueil.dayOfMonth
        val moisDuJour = binding.datepickeraccueil.month
        val yearDuJour = binding.datepickeraccueil.year

        binding.datepickeraccueil.init(
            yearDuJour,
            moisDuJour,
            jourDuJour
        ) { view, year, monthOfYear, dayOfMonth ->
            val monthreel = monthOfYear + 1
            val date = recupDate(
                year,
                monthreel,
                dayOfMonth
            )

            when (posTab) {
                0 -> {
                    vmaccueil.getSpecGly(date).observe(viewLifecycleOwner) {
                        binding.chart1AccueilAlimentation.invalidate()
                        binding.chart2AccueilAlimentation.invalidate()

                        valuesBdd.clear()
                        valuesBdd.addAll(it)

                        if (valuesBdd.size > 0) {
                            binding.chart1AccueilDiabete.visibility = View.VISIBLE
                            binding.chart2AccueilDiabete.visibility = View.VISIBLE
                            binding.chart1AccueilAlimentation.visibility = View.GONE
                            binding.chart2AccueilAlimentation.visibility = View.GONE

                            binding.llAvertissement.visibility = View.GONE
                            binding.llAvertissement2.visibility = View.GONE

                            createLineChart(
                                requireContext(),
                                binding.chart1AccueilDiabete,
                                recupDataChart(valuesBdd)
                            )

                            binding.chart1AccueilDiabete.invalidate()
                        } else {
                            binding.llAvertissement.visibility = View.VISIBLE
                            binding.llAvertissement2.visibility = View.VISIBLE

                            binding.chart1AccueilDiabete.visibility = View.GONE
                            binding.chart2AccueilDiabete.visibility = View.GONE
                            binding.chart1AccueilAlimentation.visibility = View.GONE
                            binding.chart2AccueilAlimentation.visibility = View.GONE
                        }

                        var hypo = 0
                        var cible = 0
                        var fort = 0
                        var hyper = 0

                        for (i in 0 until it.size) {
                            when (valuesBdd.get(i)) {
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

                        creationPieChart(binding.chart2AccueilDiabete, arrayData, couleurs)

                    }
                }

                1 -> {
                    vmaccueil.getSpecPoids(date).observe(viewLifecycleOwner) {
                        binding.chart1AccueilAlimentation.invalidate()
                        binding.chart2AccueilAlimentation.invalidate()

                        valuesPoids.clear()
                        valuesPoids.addAll(it)

                        if (valuesPoids.size > 0) {
                            binding.chart1AccueilDiabete.visibility = View.GONE
                            binding.chart2AccueilDiabete.visibility = View.GONE
                            binding.chart1AccueilAlimentation.visibility = View.VISIBLE
                            binding.chart2AccueilAlimentation.visibility = View.VISIBLE

                            binding.llAvertissement.visibility = View.GONE
                            binding.llAvertissement2.visibility = View.GONE

                            recupDataBarChart(date)

                        } else {

                            binding.llAvertissement.visibility = View.VISIBLE
                            binding.llAvertissement2.visibility = View.VISIBLE

                            binding.chart1AccueilDiabete.visibility = View.GONE
                            binding.chart2AccueilDiabete.visibility = View.GONE
                            binding.chart1AccueilAlimentation.visibility = View.GONE
                            binding.chart2AccueilAlimentation.visibility = View.GONE

                        }

                    }
                }

                else -> {}
            }

        }
    }
    fun recupDataBarChart(date: String): ArrayList<BarEntry> {
        val data = ArrayList<BarEntry>()
        val tabValeur = ArrayList<Float>()
        val barChart = binding.chart1AccueilAlimentation
        val stringValue = ArrayList<String>()

        vmaccueil.getSpecPoids(date).observe(viewLifecycleOwner) {
            tabValeur.clear()
            tabValeur.addAll(it)

            val r = tabValeur.size - 1
            for (i in 0..r) {
                stringValue.add("")
                data.add(BarEntry(i.toFloat(), tabValeur[i]))
            }
            createBarChart(barChart, data, stringValue, "Poids")
        }

        return data
    }

}