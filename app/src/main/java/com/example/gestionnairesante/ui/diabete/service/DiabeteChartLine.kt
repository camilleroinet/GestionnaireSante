package com.example.gestionnairesante.ui.diabete.service

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.gestionnairesante.R
import com.example.gestionnairesante.database.dao.innerDiabete.DataInner
import com.example.gestionnairesante.databinding.FragChartLineBinding
import com.example.gestionnairesante.ui.diabete.vm.VMDiabete
import com.example.gestionnairesante.utils.configGraphs
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class DiabeteChartLine : Fragment() {

    private var binding: FragChartLineBinding? = null
    private val vmdiabete: VMDiabete by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // View binding
        val fragBinding = FragChartLineBinding.inflate(inflater, container, false)
        binding = fragBinding

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            binding?.fragChartLine = this@DiabeteChartLine
        }
        binding?.vmdiabete = vmdiabete

        vmdiabete.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        // Inflate the layout for this fragment
        return fragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recupDataLineChart()

    }

    fun recupDataLineChart() {
        vmdiabete.getAllGlycemieDESC().observe(viewLifecycleOwner) {
            val valuesBdd = ArrayList<DataInner>()

            valuesBdd.clear()
            valuesBdd.addAll(it)

            if (valuesBdd.size == 0) {
                binding?.chart0?.visibility = View.GONE
                binding?.llAvertissement?.visibility = View.VISIBLE
            } else {
                binding?.tvrienchart?.visibility = View.GONE
                binding?.chart0?.visibility = View.VISIBLE
                createLineChart(requireContext(), binding!!.chart0, recupDataChart(valuesBdd))
            }
        }
    }

    fun createLineChart(context: Context, linechart: LineChart, entri: ArrayList<Entry>){
        configGraphs(linechart)
        val lineDataSet = LineDataSet(entri, "Glycemie")
        context.let { lineDataSet.color = it.getColor(R.color.black) }              // Couleur de la ligne reliant les valeurs
        lineDataSet.mode = LineDataSet.Mode.LINEAR                                  // Style de la courbe
        lineDataSet.lineWidth = 2.5F                                                // Epaisseur de la ligne reliant les valeurs
        lineDataSet.setDrawValues(false)                                            // On affiche les valeurs : oui
        lineDataSet.valueTextSize = 12F                                             // Taille de la police de caractere
        context.let { lineDataSet.setCircleColor(it.getColor(R.color.black)) }      // Couleur des cercles de data dans le graph
        lineDataSet.circleRadius = 0f                                               // Taille des cerlces des valeurs dans le graph

        linechart.animateX(1500)
        linechart.isHorizontalFadingEdgeEnabled

        val iLineDataSet = ArrayList<ILineDataSet>()
        iLineDataSet.add(lineDataSet)                                               // Creer les valeurs et leur config
        val ld = LineData(iLineDataSet)
        linechart.data = ld                                                         // Associe le chart avec les valeurs
        linechart.invalidate()                                                      // Rafraichit le chart(en fait on lui dit de se reafficher entierement)
    }

    fun recupDataChart(array: ArrayList<DataInner>): ArrayList<Entry>{
        val valu = ArrayList<Entry>()
        val r = array.size - 1
        for (i in 0..r){
            valu.add(Entry(i.toFloat(), array[i].glycemie.toFloat()))
        }
        return valu
    }

}