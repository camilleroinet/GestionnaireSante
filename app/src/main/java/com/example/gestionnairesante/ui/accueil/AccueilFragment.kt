package com.example.gestionnairesante.ui.accueil

import android.content.Context
import android.graphics.Color
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
import com.example.gestionnairesante.utils.creationPieChart
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
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
                                binding.alimDualPie.visibility = View.GONE
                            }

                            1 -> {
                                posTab = 1
                                binding.datepickeraccueil.visibility = View.VISIBLE
                                binding.chart1AccueilDiabete.visibility = View.GONE
                                binding.chart2AccueilDiabete.visibility = View.GONE
                                binding.chart1AccueilAlimentation.visibility = View.VISIBLE
                                binding.alimDualPie.visibility = View.VISIBLE
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
        val valueRapide = ArrayList<Int>()
        val valueLente = ArrayList<Int>()

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
                            binding.chart1AccueilDiabete.visibility = View.VISIBLE
                            binding.chart2AccueilDiabete.visibility = View.VISIBLE
                            binding.chart1AccueilAlimentation.visibility = View.GONE
                            binding.alimDualPie.visibility = View.GONE

                            binding.llAvertissement.visibility = View.GONE
                            binding.llAvertissement2.visibility = View.GONE

                            //Toast.makeText(requireContext(), "size de lente : ${valueLente.size}", Toast.LENGTH_SHORT).show()

                            createLineChart(
                                requireContext(),
                                binding.chart1AccueilDiabete,
                                recupDataChart(valuesBdd),
                                recupDataChart(valueRapide),
                                recupDataChart(valueLente)
                            )

                            binding.chart1AccueilDiabete.invalidate()
                        } else {
                            binding.llAvertissement.visibility = View.VISIBLE
                            binding.llAvertissement2.visibility = View.VISIBLE

                            binding.chart1AccueilDiabete.visibility = View.GONE
                            binding.chart2AccueilDiabete.visibility = View.GONE
                            binding.chart1AccueilAlimentation.visibility = View.GONE
                            binding.alimDualPie.visibility = View.GONE
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

                        valuesPoids.clear()
                        valuesPoids.addAll(it)

                        if (valuesPoids.size > 0) {
                            binding.chart1AccueilDiabete.visibility = View.GONE
                            binding.chart2AccueilDiabete.visibility = View.GONE
                            binding.chart1AccueilAlimentation.visibility = View.VISIBLE
                            binding.alimDualPie.visibility = View.VISIBLE

                            binding.llAvertissement.visibility = View.GONE
                            binding.llAvertissement2.visibility = View.GONE

                            loadCharts2(date)

                        } else {

                            binding.llAvertissement.visibility = View.VISIBLE
                            binding.llAvertissement2.visibility = View.VISIBLE

                            binding.chart1AccueilDiabete.visibility = View.GONE
                            binding.chart2AccueilDiabete.visibility = View.GONE
                            binding.chart1AccueilAlimentation.visibility = View.GONE
                            binding.alimDualPie.visibility = View.GONE

                        }

                    }
                }

                else -> {}
            }

        }
    }

    fun createLineChart(context: Context,
                        linechart: LineChart,
                        entri: ArrayList<Entry>,
                        entri2: ArrayList<Entry>,
                        entri3: ArrayList<Entry>){

        configGraphs(linechart)                                                     // Configuration du linechart

        val lineDataSet = LineDataSet(entri, "Glycemie")
        context.let { lineDataSet.color = it.getColor(R.color.black) }              // Couleur de la ligne reliant les valeurs
        lineDataSet.mode = LineDataSet.Mode.LINEAR                                  // Style de la courbe
        lineDataSet.lineWidth = 2.5F                                                // Epaisseur de la ligne reliant les valeurs
        lineDataSet.setDrawValues(false)                                            // On affiche les valeurs : oui
        lineDataSet.valueTextSize = 12F                                             // Taille de la police de caractere
        context.let { lineDataSet.setCircleColor(it.getColor(R.color.black)) }      // Couleur des cercles de data dans le graph
        lineDataSet.circleRadius = 0f                                               // Taille des cerlces des valeurs dans le graph

        val lineDataSet2 = LineDataSet(entri2, "Rapide")
        context.let { lineDataSet2.color = it.getColor(R.color.color02) }           // Couleur de la ligne reliant les valeurs
        lineDataSet2.mode = LineDataSet.Mode.LINEAR                                 // Style de la courbe
        lineDataSet2.lineWidth = 2.5F                                               // Epaisseur de la ligne reliant les valeurs
        lineDataSet2.setDrawValues(false)                                           // On affiche les valeurs : oui
        lineDataSet2.valueTextSize = 12F                                            // Taille de la police de caractere
        context.let { lineDataSet2.setCircleColor(it.getColor(R.color.color02)) }   // Couleur des cercles de data dans le graph
        lineDataSet2.circleRadius = 0f

        val lineDataSet3 = LineDataSet(entri3, "Lente")
        context.let { lineDataSet3.color = it.getColor(R.color.color01) }           // Couleur de la ligne reliant les valeurs
        lineDataSet3.mode = LineDataSet.Mode.LINEAR                                 // Style de la courbe
        lineDataSet3.lineWidth = 2.5F                                               // Epaisseur de la ligne reliant les valeurs
        lineDataSet3.setDrawValues(false)                                           // On affiche les valeurs : oui
        lineDataSet3.valueTextSize = 12F                                            // Taille de la police de caractere
        context.let { lineDataSet3.setCircleColor(it.getColor(R.color.color01)) }   // Couleur des cercles de data dans le graph
        lineDataSet3.circleRadius = 0f

        val iLineDataSet = ArrayList<ILineDataSet>()
        iLineDataSet.add(lineDataSet)                                               // Creer les valeurs et leur config
        iLineDataSet.add(lineDataSet2)                                              // Creer les valeurs et leur config
        iLineDataSet.add(lineDataSet3)                                              // Creer les valeurs et leur config

        val ld = LineData(iLineDataSet)
        linechart.data = ld                                                         // Associe le chart avec les valeurs
        linechart.invalidate()                                                      // Rafraichit le chart(en fait on lui dit de se
        // reafficher entierement)
    }

    fun configGraphs(linechart: LineChart){
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

    fun recupDataChart(array: ArrayList<Int>): ArrayList<Entry>{
        val valu = ArrayList<Entry>()
        val r = array.size - 1
        for (i in 0..r){
            valu.add(Entry(i.toFloat(), array[i].toFloat()))
        }
        return valu
    }

    fun loadCharts2(date: String): ArrayList<BarEntry> {
        val data = ArrayList<BarEntry>()
        val tabValeur = ArrayList<Float>()
        val barChart = binding.chart1AccueilAlimentation
        val stringValue = ArrayList<String>()

        val totalCalories = vmaccueil.getSpecCalories(date)
        val arrayData = ArrayList<Float>()

        vmaccueil.getSpecCalories(date).observe(viewLifecycleOwner) {
            arrayData.clear()
            arrayData.addAll(it)
        }
        vmaccueil.getSpecPoids(date).observe(viewLifecycleOwner) {
            tabValeur.clear()
            tabValeur.addAll(it)

            val r = tabValeur.size - 1
            for (i in 0..r) {
                stringValue.add("")
                data.add(BarEntry(i.toFloat(), tabValeur[i]))
            }
            createBarChart(barChart, data, stringValue, "Poids")

            val couleurs = createColorTab(requireContext(), arrayData.size)
            creationDemiPieChart(binding!!.chart2Alim, arrayData, couleurs)
        }

        return data
    }

    fun creationDemiPieChart(pie: PieChart, arrayData: ArrayList<Float>, couleurs: ArrayList<Int>){
        pie.description.isEnabled = false
        pie.setExtraOffsets(5f, 10f, 5f, 5f)
        pie.dragDecelerationFrictionCoef = 0.95f
        pie.isDrawHoleEnabled = true
        pie.setHoleColor(Color.WHITE)
        pie.setTransparentCircleColor(Color.YELLOW)
        pie.setTransparentCircleAlpha(110)
        pie.holeRadius = 38f
        pie.transparentCircleRadius = 41f
        pie.setDrawCenterText(true)
        pie.isRotationEnabled = false
        pie.isHighlightPerTapEnabled = true
        pie.animateY(1400, Easing.EaseInOutQuad)
        pie.maxAngle = 180f
        pie.rotationAngle = 180f
        pie.setCenterTextOffset(0f, -20f)

        val legend = pie.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false)
        legend.xEntrySpace = 7f
        legend.yEntrySpace = 0f
        legend.yOffset = 0f
        legend.isEnabled = true

        pie.setEntryLabelColor(Color.BLACK)
        pie.setEntryLabelTextSize(12f)
        pie.setDrawCenterText(false)

        pie.data = demipieData(arrayData, couleurs)
        pie.invalidate()

    }

    fun demipieData(arrayData: ArrayList<Float>, couleurs: ArrayList<Int>) : PieData {
        val stringLegend = ArrayList<String>()

        /*    for (i in 0 until arrayData.size){
                stringLegend.add("Faible$i")
            }*/
        stringLegend.add("Calories")


        val entries = ArrayList<PieEntry>()

        for(i in 0 until arrayData.size){
            entries.add(PieEntry(arrayData[i], stringLegend[i]))
        }
        arrayData.clear()
        val set = PieDataSet(entries, "pie chart")
        set.valueLinePart1OffsetPercentage = 80f
        set.valueLinePart1Length = 0.70f
        set.valueLinePart2Length = 0.30f
        set.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        set.colors = couleurs

        val data = PieData(set)
        data.setValueTextColor(Color.BLUE)
        data.setValueTextSize(11f)

        return data

    }


}