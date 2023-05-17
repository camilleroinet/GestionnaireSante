@file: JvmName("ChartsUtils")
@file: JvmMultifileClass

package com.example.gestionnairesante.utils

import android.content.Context
import android.graphics.Color
import com.example.gestionnairesante.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.animation.Easing.EasingFunction
import com.github.mikephil.charting.charts.*
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

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
    context.let { lineDataSet2.color = it.getColor(R.color.color02) }            // Couleur de la ligne reliant les valeurs
    lineDataSet2.mode = LineDataSet.Mode.LINEAR                                  // Style de la courbe
    lineDataSet2.lineWidth = 2.5F                                                // Epaisseur de la ligne reliant les valeurs
    lineDataSet2.setDrawValues(false)                                            // On affiche les valeurs : oui
    lineDataSet2.valueTextSize = 12F                                             // Taille de la police de caractere
    context.let { lineDataSet2.setCircleColor(it.getColor(R.color.color02)) }    // Couleur des cercles de data dans le graph
    lineDataSet2.circleRadius = 0f

    val lineDataSet3 = LineDataSet(entri3, "Lente")
    context.let { lineDataSet3.color = it.getColor(R.color.color01) }            // Couleur de la ligne reliant les valeurs
    lineDataSet3.mode = LineDataSet.Mode.LINEAR                                  // Style de la courbe
    lineDataSet3.lineWidth = 2.5F                                                // Epaisseur de la ligne reliant les valeurs
    lineDataSet3.setDrawValues(false)                                            // On affiche les valeurs : oui
    lineDataSet3.valueTextSize = 12F                                             // Taille de la police de caractere
    context.let { lineDataSet3.setCircleColor(it.getColor(R.color.color01)) }    // Couleur des cercles de data dans le graph
    lineDataSet3.circleRadius = 0f

    val iLineDataSet = ArrayList<ILineDataSet>()
    iLineDataSet.add(lineDataSet)                                               // Creer les valeurs et leur config
    iLineDataSet.add(lineDataSet2)                                              // Creer les valeurs et leur config
    iLineDataSet.add(lineDataSet3)                                              // Creer les valeurs et leur config


    linechart.animateX(2000, Easing.EaseOutBack)
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

fun recupDataBarChart(array: ArrayList<Float>): ArrayList<BarEntry> {
    val valu = ArrayList<BarEntry>()
    val r = array.size - 1
    for(i in 0..r){
        valu.add(BarEntry(i.toFloat(), array[i]))
    }
    return valu
}

/**
 * Fonction du pie chart pour le diabete
 */
fun creationPieChart(pie: PieChart, arrayData: ArrayList<Float>, couleurs: ArrayList<Int>){
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
    pie.maxAngle = 360f
    pie.rotationAngle = 360f
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

    pie.data = pieData(arrayData, couleurs)
    pie.invalidate()

}

fun pieData(arrayData: ArrayList<Float>, couleurs: ArrayList<Int>) : PieData {
    val stringLegend = ArrayList<String>()

    /*    for (i in 0 until arrayData.size){
            stringLegend.add("Faible$i")
        }*/
    stringLegend.add("hypo")
    stringLegend.add("cible")
    stringLegend.add("fort")
    stringLegend.add("hyper")

    val entries = ArrayList<PieEntry>()

    for(i in 0 until arrayData.size  ){
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
/**
 * Fonction du demi pie chart pour l'accueil
 * graph de gauche
 */
fun creationPieChart1(pie: PieChart, arrayData: ArrayList<Float>, couleurs: ArrayList<Int>){
    pie.description.isEnabled = false
    pie.setExtraOffsets(5f, 10f, 5f, 5f)
    pie.dragDecelerationFrictionCoef = 0.95f
    pie.isDrawHoleEnabled = true
    pie.setHoleColor(Color.WHITE)
    pie.setTransparentCircleColor(Color.WHITE)
    pie.setTransparentCircleAlpha(110)
    pie.holeRadius = 43f
    pie.transparentCircleRadius = 41f
    pie.setDrawCenterText(false)
    pie.isRotationEnabled = false
    pie.isHighlightPerTapEnabled = false
    pie.animateY(1500, Easing.EaseInOutQuad)
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
    legend.isEnabled = false


    pie.setEntryLabelColor(Color.WHITE)
    pie.setEntryLabelTextSize(12f)
    pie.setDrawCenterText(false)

    pie.data = pieData1(arrayData, couleurs)
    pie.invalidate()

}

fun pieData1(arrayData: ArrayList<Float>, couleurs: ArrayList<Int>) : PieData {
    val stringLegend = ArrayList<String>()

    stringLegend.add("hypo")
    stringLegend.add("hyper")

    val entries = ArrayList<PieEntry>()

    for(i in 0 until arrayData.size){
        entries.add(PieEntry(arrayData[i], stringLegend[i]))
    }
    arrayData.clear()
    val set = PieDataSet(entries, "Hypo / Hyper")
    set.colors = couleurs

    val data = PieData(set)
    data.setValueTextColor(Color.WHITE)
    data.setValueTextSize(13f)

    return data

}
/**
 * Fonction du demi pie chart pour l'accueil
 * graph de droite
 */
fun creationPieChart2(pie: PieChart, arrayData: ArrayList<Float>, couleurs: ArrayList<Int>){
    pie.description.isEnabled = false
    pie.setExtraOffsets(5f, 10f, 5f, 5f)
    pie.dragDecelerationFrictionCoef = 0.95f
    pie.isDrawHoleEnabled = true
    pie.setHoleColor(Color.WHITE)
    pie.setTransparentCircleColor(Color.WHITE)
    pie.setTransparentCircleAlpha(110)
    pie.holeRadius = 43f
    pie.transparentCircleRadius = 41f
    pie.setDrawCenterText(false)
    pie.isRotationEnabled = false
    pie.isHighlightPerTapEnabled = false
    pie.animateY(1400, Easing.EaseInOutQuad)
    pie.maxAngle = 360f
    pie.rotationAngle = 360f
    pie.setCenterTextOffset(0f, -20f)


    val legend = pie.legend
    legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
    legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
    legend.orientation = Legend.LegendOrientation.VERTICAL
    legend.setDrawInside(false)
    legend.xEntrySpace = 7f
    legend.yEntrySpace = 0f
    legend.yOffset = 0f
    legend.isEnabled = false

    pie.setEntryLabelColor(Color.WHITE)
    pie.setEntryLabelTextSize(12f)
    pie.setDrawCenterText(false)

    pie.data = pieData2(arrayData, couleurs)
    pie.invalidate()

}

fun pieData2(arrayData: ArrayList<Float>, couleurs: ArrayList<Int>) : PieData {
    val stringLegend = ArrayList<String>()

    /*    for (i in 0 until arrayData.size){
            stringLegend.add("Faible$i")
        }*/
    stringLegend.add("cible")
    stringLegend.add("fort")

    val entries = ArrayList<PieEntry>()

    for(i in 0 until arrayData.size){
        entries.add(PieEntry(arrayData[i], stringLegend[i]))
    }
    arrayData.clear()
    val set = PieDataSet(entries, "pie chart 2")
    set.colors = couleurs

    val data = PieData(set)
    data.setValueTextColor(Color.WHITE)
    data.setValueTextSize(13f)

    return data

}


fun barData(barEntry: ArrayList<BarEntry>, titreGraph: String): BarData {
    val bardataset = BarDataSet(barEntry, titreGraph)
    bardataset.color = Color.rgb(121, 134, 203)
    bardataset.valueTextSize = 12f
    bardataset.valueTextColor = Color.BLACK
    bardataset.axisDependency = YAxis.AxisDependency.LEFT
    bardataset.setDrawValues(true)
    val bardata = BarData(bardataset)
    bardata.barWidth = 0.50f
    bardata.setValueTextColor(Color.BLACK)

    return bardata
}

fun createBarChart(barChart: BarChart, barentry: ArrayList<BarEntry>, stringValeur: ArrayList<String>, titreGraph: String){
    stringValeur.clear()
    for(i in 0 until barentry.size){
        stringValeur.add("")
    }

    val rightAxis = barChart.axisRight
    rightAxis.isEnabled = false

    barChart.setFitBars(false)
    barChart.animateY(2000)
    barChart.setDrawGridBackground(false)
    barChart.isHorizontalFadingEdgeEnabled
    barChart.setDrawBarShadow(false)
    barChart.setPinchZoom(true)
    barChart.setDrawGridBackground(false)
    barChart.setMaxVisibleValueCount(8)
    barChart.setFitBars(false)
    barChart.setDrawValueAboveBar(true)

    val leftAxis = barChart.axisLeft
    leftAxis.setDrawGridLines(false)
    leftAxis.axisMinimum = 0f
    rightAxis.isEnabled = false
    barChart.data = barData(barentry, titreGraph)

    val xAxis = barChart.xAxis
    xAxis.setDrawGridLines(false)
    xAxis.isEnabled = false
    barChart.setVisibleXRangeMaximum(6f)
    barChart.setNoDataText("Rien à afficher, Désolé")

    val description = Description()
    description.text = ""
    barChart.description = description
    barChart.invalidate()
}

fun recpDataBarChart(array: ArrayList<Int>): ArrayList<BarEntry>{
    val valu = ArrayList<BarEntry>()
    val r = array.size - 1
    for (i in 0..r){
        valu.add(BarEntry(i.toFloat(), array[i].toFloat()))
    }
    return valu
}

/*CombinedChart*/
fun createCombiedChart(c: Context, cc: CombinedChart, arrayDataLine: ArrayList<Entry>, arrayDataBar1: ArrayList<BarEntry>) {

    //linechart
    val lineDataSet = LineDataSet(arrayDataLine, "temp")
    c.let { lineDataSet.color = it.getColor(R.color.black) }            //coleur de la ligne reliant les valeurs
    lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER                           //style de la courbe
    lineDataSet.lineWidth = 2.5F                                         //epaisseur de la ligne reliant les valeurs
    lineDataSet.setDrawValues(true)                                      //on affiche les valeurs : oui
    lineDataSet.valueTextSize = 12F                                      //taille de la police de caractere<

    c.let { lineDataSet.setCircleColor(it.getColor(R.color.black)) }    //couleur des cercles de data dans le graph
    lineDataSet.circleRadius = 5f                                        //taille des cerlces des valeurs dans le graph
    val iLineDataSet = ArrayList<ILineDataSet>()
    iLineDataSet.add(lineDataSet)                                        //on creer les valeurs et leur config
    val ld = LineData(iLineDataSet)


    //barchart
    val bardataset = BarDataSet(arrayDataBar1, "")
    bardataset.color = Color.rgb(60f, 220f, 78f)
    bardataset.valueTextSize = 12F
    bardataset.valueTextColor = Color.BLACK
    bardataset.axisDependency = YAxis.AxisDependency.LEFT
    bardataset.setDrawValues(true)
    val bardata = BarData(bardataset)
    bardata.barWidth = 0.85f
    bardata.setValueTextColor(Color.GREEN)

    val combinedData = CombinedData()
    combinedData.setData(ld)
    combinedData.setData(bardata)

    cc.data = combinedData
    cc.invalidate()
}

//HorizontalBarchart
fun createHorizBarchart(indice: Int, hbarChart: HorizontalBarChart, barentri: ArrayList<BarEntry>,
                        stringValeur: ArrayList<String>, titreGraph: String){

    val rightAxis = hbarChart.axisRight
    rightAxis.isEnabled = false
    hbarChart.setFitBars(false)
    hbarChart.animateY(3000)
    hbarChart.setDrawGridBackground(false)
    hbarChart.isHorizontalFadingEdgeEnabled
    hbarChart.setDrawGridBackground(false)
    hbarChart.setDrawValueAboveBar(false)

    hbarChart.setDrawBarShadow(false)
    hbarChart.setPinchZoom(false)
    hbarChart.setDrawGridBackground(false)
    hbarChart.setMaxVisibleValueCount(5)
    hbarChart.setFitBars(true)
    hbarChart.setDrawValueAboveBar(true)
    hbarChart.setDrawBarShadow(false)

    val leftAxis = hbarChart.axisLeft

    when(indice){
        1 -> {
            stringValeur.clear()
            stringValeur.add("Calories")

            leftAxis.setDrawGridLines(false)
            leftAxis.axisMinimum = 0f
            leftAxis.axisMaximum = 2100f
        }
        2 -> {
            stringValeur.clear()
            stringValeur.add("Glucides")

            leftAxis.setDrawGridLines(false)
            leftAxis.axisMinimum = 0f
            leftAxis.axisMaximum = 325f
        }
        else -> {

        }
    }


    rightAxis.isEnabled = false

    hbarChart.data = barData(barentri, titreGraph)
    val xAxis = hbarChart.xAxis
    xAxis.isEnabled = false

    hbarChart.setVisibleXRangeMaximum(6f)
    hbarChart.setNoDataText("Rien à afficher, Désolé")
    val description = Description()
    description.text = ""
    hbarChart.description = description
    hbarChart.invalidate()
}