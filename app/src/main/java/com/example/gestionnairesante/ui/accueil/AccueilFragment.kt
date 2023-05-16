package com.example.gestionnairesante.ui.accueil

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import com.example.gestionnairesante.MainActivity
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.DB_sante
import com.example.gestionnairesante.database.dao.innerStats.StatsRepo
import com.example.gestionnairesante.databinding.AccueilBinding
import com.example.gestionnairesante.ui.accueil.vm.VmAccueil
import com.example.gestionnairesante.ui.accueil.vm.VmAccueilFactory
import com.example.gestionnairesante.ui.diabete.service.DiabeteDialogGlycemie
import com.example.gestionnairesante.utils.createBarChart
import com.example.gestionnairesante.utils.createColorTab
import com.example.gestionnairesante.utils.createHorizBarchart
import com.example.gestionnairesante.utils.creationPieChart1
import com.example.gestionnairesante.utils.creationPieChart2
import com.example.gestionnairesante.utils.recupDataBarChart
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class AccueilFragment : Fragment() {

    private lateinit var binding: AccueilBinding
    private lateinit var vmaccueil: VmAccueil

    private lateinit var tablayoutTabs: TabLayout
    private var arrayTab = arrayListOf(R.string.txt_accueilTab1, R.string.txt_accueilTab2, R.string.txt_accueilTab3)

    val tabData = ArrayList<Int> ()
    private var posTab = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // viewbinding
        val accueilFrag = AccueilBinding.inflate(inflater, container, false)
        binding = accueilFrag

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            binding.fragAccueil = this@AccueilFragment
        }

        //
        // databinding
        //
        val dao = DB_sante.getInstance(requireContext()).tabRelationnelStats
        val repo = StatsRepo(dao)
        val factory = VmAccueilFactory(repo)
        vmaccueil = ViewModelProvider(requireActivity(), factory).get(VmAccueil::class.java)
        binding.vmaccueil = vmaccueil



        return accueilFrag.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabGlycemie = ArrayList<Int>()
        val arrayDataHYypoper = ArrayList<Float>()        // ArrayList des hypo/hyper
        val arrayDataCiblePresq = ArrayList<Float>()      // ArrayList des cibles/fort

        val valuesPoids = ArrayList<Float>()
        val valueRapide = ArrayList<Int>()
        val valueLente = ArrayList<Int>()

        //
        // Date
        //
        val jourDuJour = binding.datepickeraccueil.dayOfMonth
        val moisDuJour = binding.datepickeraccueil.month+1
        val yearDuJour = binding.datepickeraccueil.year
        val date = "$jourDuJour-$moisDuJour-$yearDuJour"

        vmaccueil.getGlycemiePeriode(date).observe(viewLifecycleOwner) {
            tabGlycemie.clear()
            tabGlycemie.addAll(it)
            if (tabGlycemie.size > 0) {
                binding.llAvertissement.visibility = View.GONE
                binding.llAvertissement2.visibility = View.GONE
                binding.chart1AccueilDiabete.visibility = View.VISIBLE
                binding.diabeteDualPie.visibility = View.VISIBLE
                binding.chart1AccueilAlimentation.visibility = View.GONE
                binding.chart2AccueilAlimentation.visibility = View.GONE

                createLineChart(
                    requireContext(),
                    binding.chart1AccueilDiabete,
                    recupDataChart(tabGlycemie),
                    recupDataChart(valueRapide),
                    recupDataChart(valueLente)
                )
                var hypo = 0
                var cible = 0
                var fort = 0
                var hyper = 0

                for (i in 0 until it.size) {
                    when (tabGlycemie.get(i)) {
                        in 0..79 -> hypo += 1
                        in 80..179 -> cible += 1
                        in 180..249 -> fort += 1
                        else -> hyper += 1
                    }
                }

                arrayDataHYypoper.add(hypo.toFloat())
                arrayDataCiblePresq.add(cible.toFloat())
                arrayDataCiblePresq.add(fort.toFloat())
                arrayDataHYypoper.add(hyper.toFloat())

                val couleurs = createColorTab(requireContext(), arrayDataHYypoper.size)
                val couleurs2 = createColorTab(requireContext(), arrayDataCiblePresq.size)

                creationPieChart1(binding.piechart1Dia, arrayDataHYypoper, couleurs)
                creationPieChart2(binding.piechart2Dia, arrayDataCiblePresq, couleurs2)

            } else {
                binding.llAvertissement.visibility = View.VISIBLE
                binding.llAvertissement2.visibility = View.VISIBLE
                binding.chart1AccueilDiabete.visibility = View.GONE
                binding.diabeteDualPie.visibility = View.GONE
                binding.chart1AccueilAlimentation.visibility = View.GONE
                binding.chart2AccueilAlimentation.visibility = View.GONE
            }
        }

        vmaccueil.getSpecPoids(date).observe(viewLifecycleOwner) {

            valuesPoids.clear()
            valuesPoids.addAll(it)
            Toast.makeText(requireContext(), "taille = ${valuesPoids.size}", Toast.LENGTH_LONG).show()
            if (valuesPoids.size > 0) {
                loadCharts2(date)

            } else {
                binding.llAvertissement.visibility = View.VISIBLE
                binding.llAvertissement2.visibility = View.VISIBLE
                binding.chart1AccueilDiabete.visibility = View.GONE
                binding.diabeteDualPie.visibility = View.GONE
                binding.chart1AccueilAlimentation.visibility = View.GONE
                binding.chart2AccueilAlimentation.visibility = View.GONE
            }

        }



        tablayoutTabs = binding.tabLayout

        lifecycleScope.launch {
            lifecycle.withStarted {
                val fab: FloatingActionButton = (activity as MainActivity).findViewById(R.id.fab)
                fab.isVisible = false
            }
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
                                binding.llAvertissement.visibility = View.GONE
                                binding.llAvertissement2.visibility = View.GONE
                                binding.datepickeraccueil.visibility = View.VISIBLE
                                binding.chart1AccueilDiabete.visibility = View.VISIBLE
                                binding.diabeteDualPie.visibility = View.VISIBLE
                                binding.chart1AccueilAlimentation.visibility = View.GONE
                                binding.chart2AccueilAlimentation.visibility = View.GONE


                            }

                            1 -> {
                                posTab = 1
                                binding.llAvertissement.visibility = View.GONE
                                binding.llAvertissement2.visibility = View.GONE
                                binding.datepickeraccueil.visibility = View.VISIBLE
                                binding.chart1AccueilDiabete.visibility = View.GONE
                                binding.diabeteDualPie.visibility = View.GONE
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

        val dateenstring = "$jourString-$monthString-$year"

        return dateenstring
    }

    fun loadCharts() {
        val valuesBdd = ArrayList<Int>()
        val valueRapide = ArrayList<Int>()
        val valueLente = ArrayList<Int>()

        val valuesPoids = ArrayList<Float>()

        val arrayDataHYypoper = ArrayList<Float>()      // ArrayList des hypo/hyper
        val arrayDataCiblePresq = ArrayList<Float>()      // ArrayList des cibles/fort

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
                    vmaccueil.getSpecRapide(date).observe(viewLifecycleOwner) {
                        valueRapide.clear()
                        valueRapide.addAll(it)
                    }
                    vmaccueil.getSpecLente(date).observe(viewLifecycleOwner) {
                        valueLente.clear()
                        valueLente.addAll(it)
                    }
                    vmaccueil.getSpecGly(date).observe(viewLifecycleOwner) {
                        valuesBdd.clear()
                        valuesBdd.addAll(it)

                        if (valuesBdd.size > 0) {
                            binding.llAvertissement.visibility = View.GONE
                            binding.llAvertissement2.visibility = View.GONE
                            binding.chart1AccueilDiabete.visibility = View.VISIBLE
                            binding.diabeteDualPie.visibility = View.VISIBLE
                            binding.chart1AccueilAlimentation.visibility = View.GONE
                            binding.chart2AccueilAlimentation.visibility = View.GONE

                            createLineChart(
                                requireContext(),
                                binding.chart1AccueilDiabete,
                                recupDataChart(valuesBdd),
                                recupDataChart(valueRapide),
                                recupDataChart(valueLente)
                            )
                        } else {
                            binding.llAvertissement.visibility = View.VISIBLE
                            binding.llAvertissement2.visibility = View.VISIBLE
                            binding.chart1AccueilDiabete.visibility = View.GONE
                            binding.diabeteDualPie.visibility = View.GONE
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

                        arrayDataHYypoper.add(hypo.toFloat())
                        arrayDataCiblePresq.add(cible.toFloat())
                        arrayDataCiblePresq.add(fort.toFloat())
                        arrayDataHYypoper.add(hyper.toFloat())

                        val couleurs = createColorTab(requireContext(), arrayDataHYypoper.size)
                        val couleurs2 = createColorTab(requireContext(), arrayDataCiblePresq.size)

                        creationPieChart1(binding.piechart1Dia, arrayDataHYypoper, couleurs)
                        creationPieChart2(binding.piechart2Dia, arrayDataCiblePresq, couleurs2)

                    }
                }

                1 -> {
                    vmaccueil.getSpecPoids(date).observe(viewLifecycleOwner) {

                        valuesPoids.clear()
                        valuesPoids.addAll(it)

                        if (valuesPoids.size > 0) {
                            binding.llAvertissement.visibility = View.GONE
                            binding.llAvertissement2.visibility = View.GONE
                            binding.chart1AccueilDiabete.visibility = View.GONE
                            binding.diabeteDualPie.visibility = View.GONE
                            binding.chart1AccueilAlimentation.visibility = View.VISIBLE
                            binding.chart2AccueilAlimentation.visibility = View.VISIBLE

                            loadCharts2(date)

                        } else {
                            binding.llAvertissement.visibility = View.VISIBLE
                            binding.llAvertissement2.visibility = View.VISIBLE
                            binding.chart1AccueilDiabete.visibility = View.GONE
                            binding.diabeteDualPie.visibility = View.GONE
                            binding.chart1AccueilAlimentation.visibility = View.GONE
                            binding.chart2AccueilAlimentation.visibility = View.GONE
                        }

                    }
                }

                else -> {}
            }

        }
    }

    fun createLineChart(
        context: Context,
        linechart: LineChart,
        entri: ArrayList<Entry>,
        entri2: ArrayList<Entry>,
        entri3: ArrayList<Entry>
    ) {

        configGraphs(linechart)                                             // Configuration du linechart

        val lineDataSet = LineDataSet(entri, "Glycemie")
        context.let {
            lineDataSet.color = it.getColor(R.color.black)
        }              // Couleur de la ligne reliant les valeurs
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER                                   // Style de la courbe
        lineDataSet.lineWidth = 2.5F                                                       // Epaisseur de la ligne reliant les valeurs
        lineDataSet.setDrawValues(false)                                                   // On affiche les valeurs : oui
        lineDataSet.valueTextSize = 12F                                                    // Taille de la police de caractere
        context.let { lineDataSet.setCircleColor(it.getColor(R.color.black)) }             // Couleur des cercles de data dans le graph
        lineDataSet.circleRadius = 0f                                                      // Taille des cerlces des valeurs dans le graph

        val lineDataSet2 = LineDataSet(entri2, "Rapide")
        context.let { lineDataSet2.color = it.getColor(R.color.color02) }                  // Couleur de la ligne reliant les valeurs
        lineDataSet2.mode = LineDataSet.Mode.LINEAR                                        // Style de la courbe
        lineDataSet2.lineWidth = 2.5F                                                      // Epaisseur de la ligne reliant les valeurs
        lineDataSet2.setDrawValues(false)                                                  // On affiche les valeurs : oui
        lineDataSet2.valueTextSize = 12F                                                   // Taille de la police de caractere
        context.let { lineDataSet2.setCircleColor(it.getColor(R.color.color02)) }          // Couleur des cercles de data dans le graph
        lineDataSet2.circleRadius = 0f

        val lineDataSet3 = LineDataSet(entri3, "Lente")
        context.let { lineDataSet3.color = it.getColor(R.color.color01) }                  // Couleur de la ligne reliant les valeurs
        lineDataSet3.mode = LineDataSet.Mode.LINEAR                                        // Style de la courbe
        lineDataSet3.lineWidth = 2.5F                                                      // Epaisseur de la ligne reliant les valeurs
        lineDataSet3.setDrawValues(false)                                                  // On affiche les valeurs : oui
        lineDataSet3.valueTextSize = 12F                                                   // Taille de la police de caractere
        context.let { lineDataSet3.setCircleColor(it.getColor(R.color.color01)) }          // Couleur des cercles de data dans le graph
        lineDataSet3.circleRadius = 0f

        val iLineDataSet = ArrayList<ILineDataSet>()
        iLineDataSet.add(lineDataSet)                                                      // Creer les valeurs et leur config (line 1)
        iLineDataSet.add(lineDataSet2)                                                     // Creer les valeurs et leur config (line 2)
        iLineDataSet.add(lineDataSet3)                                                     // Creer les valeurs et leur config (line 3)

        val ld = LineData(iLineDataSet)
        linechart.data = ld                                                                // Associe le chart avec les valeurs

        linechart.animateX(1500, Easing.EaseInSine)

        linechart.invalidate()                                                             // Rafraichit le chart(en fait on lui dit de se reafficher entierement)
    }

    fun configGraphs(linechart: LineChart) {
        linechart.setVisibleXRangeMaximum(10F)
        linechart.description.isEnabled = false             // Affichage description : non
        linechart.setPinchZoom(true)                        // Possible de zoom ds le graph = oui
        linechart.setDrawGridBackground(false)              // Affichage du grillage moche = non
        linechart.axisRight.isEnabled = false               // Affichage des data à droites du graph = non
        val axisRight = linechart.xAxis                     // Recup de axe droit (linechart.yAxis si on veut l'autre coté)
        axisRight.axisMinimum = 0F                          // Valeur mini en principe la val de début du linechart
        axisRight.position = XAxis.XAxisPosition.BOTTOM     // Position ds le graph
        axisRight.textSize = 10F                            // Taille police de caractere
        axisRight.textColor = Color.BLACK                   // Couleur police de caractere
        axisRight.setDrawGridLines(false)                   // Affichage de la grille
        axisRight.setDrawLabels(false)                      // Affichage des datas = oui
        axisRight.labelRotationAngle = 45F                  // Rotation des datas pour plus de lisibilité = oui
        linechart.axisLeft.isEnabled = true                 // Affichage de l'axe de gauche = non
        val legend = linechart.legend                       // Legend
        legend.isEnabled = false                            // Affichage de la legende = non
    }

    fun recupDataChart(array: ArrayList<Int>): ArrayList<Entry> {
        val valu = ArrayList<Entry>()
        val r = array.size - 1
        for (i in 0..r) {
            valu.add(Entry(i.toFloat(), array[i].toFloat()))
        }
        return valu
    }


    fun loadCharts2(date: String): ArrayList<BarEntry> {
        val data = ArrayList<BarEntry>()
        val tabValeur = ArrayList<Float>()
        val barChart = binding.chart1AccueilAlimentation
        val stringValue = ArrayList<String>()

        val array = ArrayList<Float>()

        val totalGlu = vmaccueil.getSpecGlucides(date)
        val totalCal = vmaccueil.getSpecCalories(date)

        array.clear()
        array.add(totalGlu)
        array.add(totalCal)

        val stringValeur = ArrayList<String>()
        stringValeur.add("Calories")
        stringValeur.add("Glucides")

        createHorizBarchart(
            binding.chart2AccueilAlimentation,
            recupDataBarChart(array),
            stringValeur,
            "Calories et glucides ingéré aujourd'hui"
        )

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