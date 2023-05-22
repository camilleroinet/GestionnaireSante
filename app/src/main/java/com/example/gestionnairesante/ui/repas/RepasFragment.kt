package com.example.gestionnairesante.ui.repas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.viewpager.widget.ViewPager
import com.example.gestionnairesante.MainActivity
import com.example.gestionnairesante.R
import com.example.gestionnairesante.adapter.AdapterViewPager
import com.example.gestionnairesante.adapter.ZoomOutPageTransformer
import com.example.gestionnairesante.database.DB_sante
import com.example.gestionnairesante.database.dao.innerPlat.InnerPlatMenuRepo
import com.example.gestionnairesante.database.dao.innerRepas.InnerPeriodeRepasRepo
import com.example.gestionnairesante.databinding.RepasBinding
import com.example.gestionnairesante.ui.repas.service.RepasDialogMenu
import com.example.gestionnairesante.ui.repas.service.RepasDialogPlat
import com.example.gestionnairesante.ui.repas.service.RepasTab1
import com.example.gestionnairesante.ui.repas.service.RepasTab2
import com.example.gestionnairesante.ui.repas.vm.VmRepas
import com.example.gestionnairesante.ui.repas.vm.VmRepasFactory
import com.example.gestionnairesante.utils.createHorizBarchart
import com.example.gestionnairesante.utils.recupDataBarChart
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class RepasFragment : Fragment() {
    private var binding: RepasBinding? = null
    private lateinit var viewmodelrepas: VmRepas

    private lateinit var tablayoutTabs: TabLayout
    private lateinit var viewPagerTabs: ViewPager

    private var arrayTab = arrayListOf<Int>(R.string.txt_fragmenu, R.string.txt_fragrepas)
    private var arrayFragTab = arrayListOf<Fragment>(RepasTab1(), RepasTab2())

    private var indiceTab = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // viewbinding
        val repasFrag = RepasBinding.inflate(inflater, container, false)
        binding = repasFrag

        return repasFrag.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            binding?.fragRepas = this@RepasFragment
        }

        // databinding
        val daoPlat = DB_sante.getInstance(requireContext()).tabPlat
        val daoMenu = DB_sante.getInstance(requireContext()).tabMenu
        val daoPeriode = DB_sante.getInstance(requireContext()).tabPeriode
        val daoRepas = DB_sante.getInstance(requireContext()).tabRelationnelPlat
        val daoPeriodeMenu = DB_sante.getInstance(requireContext()).tabRelationnelMenu

        val repositoryPlatMenu = InnerPlatMenuRepo(daoPlat, daoMenu, daoRepas, daoPeriodeMenu)
        val repositoryPeriodeMenu = InnerPeriodeRepasRepo(daoMenu, daoPeriode, daoPeriodeMenu)

        val factory = VmRepasFactory(repositoryPlatMenu, repositoryPeriodeMenu)
        viewmodelrepas = ViewModelProvider(requireActivity(), factory).get(VmRepas::class.java)
        binding?.viewmodelrepas = viewmodelrepas

        viewmodelrepas.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandle()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        lifecycleScope.launch {
            lifecycle.withStarted {
                val fab: FloatingActionButton = (activity as MainActivity).findViewById<FloatingActionButton?>(R.id.fab)
                fab.show()

                fab.setOnClickListener {
                    // Toast.makeText(requireContext(), " this is a test", Toast.LENGTH_LONG).show()
                    when (indiceTab) {
                        0 -> {
                            val repasDialogMenu = RepasDialogMenu()
                            repasDialogMenu.show(childFragmentManager, RepasDialogMenu.TAG)
                        }

                        1 -> {
                            val repasDialogPlat = RepasDialogPlat()
                            repasDialogPlat.show(childFragmentManager, RepasDialogPlat.TAG)
                        }

                        else -> {

                        }
                    }
                }
            }
        }

        tablayoutTabs = binding?.tabLayout!!
        viewPagerTabs = binding?.viewpagertabhost!!

        configTablelayout(arrayTab)
        //configViewPagerChart(viewPagerCharts, arrayFragChart, arrayTabCharts)
        configViewPager(viewPagerTabs, arrayFragTab, arrayTab, tablayoutTabs)

        loadCharts2()
    }

    fun configTablelayout(array: ArrayList<Int>) {
        tablayoutTabs.apply {
            for (i in array.indices) {
                tablayoutTabs.addTab(tablayoutTabs.newTab().setText(array[i]))
            }

            tablayoutTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.position?.let {
                        viewPagerTabs.currentItem = it

                        when (tab.position) {
                            0 -> {
                                indiceTab = 0
                            }

                            1 -> {
                                indiceTab = 1
                                Toast.makeText(requireContext(), "$indiceTab", Toast.LENGTH_SHORT)
                            }

                            else -> {}
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}

            })
        }
    }

    fun configViewPager(
        viewPager: ViewPager,
        arrayFrag: ArrayList<Fragment>,
        arrayTab: ArrayList<Int>,
        tablayout: TabLayout
    ) {
        viewPager.apply {
            viewPager.adapter = AdapterViewPager(
                arrayFrag, arrayTab,
                childFragmentManager, tablayout.tabCount, context
            )
        }
        tablayout.setupWithViewPager(viewPager, true)
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())
    }

    fun loadCharts2(): ArrayList<BarEntry> {
        val data = ArrayList<BarEntry>()

        val array = ArrayList<Float>()
        val array2 = ArrayList<Float>()

        //
        // Date
        //
        val current = LocalDateTime.now()
        val dateDuJour = android.icu.util.Calendar.getInstance()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        val date = current.format(formatter)



        //val date = LocalDate.parse(LocalDate.now().toString(), formatter)

        Toast.makeText(requireContext(),"$date" , Toast.LENGTH_LONG).show()
        val totalGlu = viewmodelrepas.getSpecGlucides("17-5-2023")
        val totalCal = viewmodelrepas.getSpecCalories("17-5-2023")

        array.clear()
        array.add(totalCal)
        array.add(totalGlu)

        val stringValeur = ArrayList<String>()
        stringValeur.add("Calories")
        stringValeur.add("Glucides")

        binding?.let {
            createHorizBarchart(
                1,
                it.chart1Journee,
                recupDataBarChart(array),
                stringValeur,
                "Calories de la journée"
            )
        }


        return data
    }

}